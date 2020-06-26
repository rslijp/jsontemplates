import {Brackets, Constant, Ternary,Variable, createExpression, getReturnType} from "./ExpressionModel";
import {HighlightTypes, NodeTypes,ReturnTypes} from '../Constants';
import {last, sortOn} from "../utils/ArrayUtil";
import {ParseCursor} from "./ParseCursor";
import SuggestionModel from "./SuggestionModel";
import SuggestionModelType from './SuggestionModelType';
import {validateCompletenessOfArguments} from './ParseUtil';


const LOG = false;
const LONG_PATTERN = /^(-?[0-9]+)/;
const DOUBLE_PATTERN = /^(-?[0-9]+\.[0-9]+)/;
const TEXT_PATTERN  = /^'((\S|\s|[-!?.";:])+?)'/;
const VARIABLE_PATTERN  = /^\$([0-9A-Za-z\\.]+)/;
const BRACKET_OPEN = "(";
const PARAMETER_SEPARATOR = ",";
const BRACKET_CLOSE = ")";

function log(msg){
    // eslint-disable-next-line no-console
    if(LOG) console.log(msg);
}


export function ParseContext(text){
    const cursor= new ParseCursor(text);
    const parseStackStash = this.parseStackStash = [];
    let parseStack = [];
    // noinspection JSMismatchedCollectionQueryUpdate
    const blocks = this.blocks = [];
    this.suggestions = new SuggestionModel();
    this.cursor= cursor;
    this.unaryLib={};
    this.infixLib={};
    this.functionLib={};

    function push(item){
        parseStack.push(item);
    }

    function popStack() {
        if(parseStack.length!==0){
            throw "Bug";
        }
        parseStack=parseStackStash.pop();
    }

    function stashStack() {
        parseStackStash.push(parseStack);
        parseStack=[];

    }

    function addBlock(block, type){
        blocks.push({start: block.start, end: block.end, type: type});
    }


    function justSawConstant() {
        if(parseStack.length === 0) return false;
        return parseStack[parseStack.length-1].type === NodeTypes.CONSTANT;
    }

    this.done=function() {
        return !this.cursor.more();
    };

    this.libScan=function(lib, tryParse, addSuggestions){
        const operators = Object.keys(lib).sort(sortOn("-length"));
        const any = operators.some(e=>{
            if(tryParse.call(this, e)) return true;
        });
        this.suggestions.add(addSuggestions.call(this));
        return any;
    };

    this.tryVariable=function() {
        if (cursor.at(VARIABLE_PATTERN)) {
            const txt = cursor.read(VARIABLE_PATTERN);
            push(new Variable(txt.substring(1)),cursor.getLastBlock());
            addBlock(cursor.getLastBlock(),HighlightTypes.VARIABLE);
            return true;
        }
        return false;
    };

    this.tryBooleanConstant=function() {
        if (cursor.at("true")){
            cursor.read("true");
            push(new Constant(true, ReturnTypes.BOOLEAN));
            addBlock(cursor.getLastBlock(),HighlightTypes.CONSTANT);
            return true;
        }
        if (cursor.at("false")){
            cursor.read("false");
            push(new Constant(false, ReturnTypes.BOOLEAN));
            addBlock(cursor.getLastBlock(),HighlightTypes.CONSTANT);
            return true;
        }
        return false;
    };

    this.tryLongConstant = function() {
        if (!justSawConstant() &&
            !cursor.at(DOUBLE_PATTERN) &&
            cursor.at(LONG_PATTERN)) {
            const txt = cursor.read(LONG_PATTERN);
            push(new Constant(parseInt(txt), ReturnTypes.INTEGER));
            addBlock(cursor.getLastBlock(),HighlightTypes.CONSTANT);
            return true;
        }
        return false;
    };

    this.tryDoubleConstant=function() {
        if (!justSawConstant() &&
            cursor.at(DOUBLE_PATTERN)) {
            const txt = cursor.read(DOUBLE_PATTERN);
            push(new Constant(parseFloat(txt), ReturnTypes.DECIMAL));
            addBlock(cursor.getLastBlock(),HighlightTypes.CONSTANT);
            return true;
        }
        return false;
    };

    this.tryStringConstant=function() {
        if (cursor.at(TEXT_PATTERN)) {
            const txt = cursor.read(TEXT_PATTERN);
            push(new Constant(txt, ReturnTypes.TEXT));
            addBlock(cursor.getLastBlock(),HighlightTypes.CONSTANT);
            return true;
        }
        return false;
    };

    this.tryFunction=function(functionName) {
        if (cursor.at(functionName)) {
            cursor.read(functionName);
            addBlock(cursor.getLastBlock(),HighlightTypes.FUNCTION);
            cursor.read(BRACKET_OPEN);
            addBlock(cursor.getLastBlock(),HighlightTypes.FUNCTION_SYMBOLS);
            const expr = createExpression(this.functionLib[functionName]);
            const argumentsTypes = expr.argumentsTypes;
            stashStack();
            for (let i = 0; i< argumentsTypes.length; i++){
                const separators = [BRACKET_CLOSE, PARAMETER_SEPARATOR];
                this.parseExpression(separators);
                expr.arguments.push(this.yield(expr.priority()));
                if(cursor.at(BRACKET_CLOSE)){
                    validateCompletenessOfArguments(expr);
                    cursor.read(BRACKET_CLOSE);
                    addBlock(cursor.getLastBlock(),HighlightTypes.FUNCTION_SYMBOLS);
                    break;
                }
                cursor.read(PARAMETER_SEPARATOR);
                addBlock(cursor.getLastBlock(),HighlightTypes.FUNCTION_SYMBOLS);
            }
            popStack();
            push(expr);
            return true;
        }
        return false;
    };

    this.tryInfix=function(operator){
        if (cursor.at(operator) && !this.empty()) {
            cursor.read(operator);
            const expr = createExpression(this.infixLib[operator]);
            addBlock(cursor.getLastBlock(),HighlightTypes.INFIX);
            this.reduce(expr.priority());
            const lhs = parseStack.pop();
            log("POP "+lhs.type);
            expr.arguments.push(lhs);
            push(expr);
            return true;
        }
        return false;
    };

    this.tryUnary=function(operator){
        if (cursor.at(operator)) {
            cursor.read(operator);
            addBlock(cursor.getLastBlock(),HighlightTypes.INFIX);
            const expr = createExpression(this.unaryLib[operator]);
            push(expr);
            return true;
        }
        return false;
    };

    this.tryBrackets=function() {
        if (cursor.at(BRACKET_OPEN)) {
            cursor.read(BRACKET_OPEN);
            addBlock(cursor.getLastBlock(),HighlightTypes.BRACKETS);
            this.parseExpression(BRACKET_CLOSE);
            const inner = this.yield();
            const brackets = Brackets();
            brackets.arguments.push(inner);
            push(brackets, "()");
            cursor.read(BRACKET_CLOSE);
            addBlock(cursor.getLastBlock(),HighlightTypes.BRACKETS);
            return true;
        }
        return false;
    };

    this.tryTernary=function() {
        if (cursor.at("?") && !this.empty()) {
            cursor.read("?");
            addBlock(cursor.getLastBlock(),HighlightTypes.TERNARY);
            const ternary = Ternary();
            const condition = this.yield(ternary.priority());
            ternary.arguments.push(condition);
            push(ternary);
            this.parseExpression(":");
            cursor.read(":");
            addBlock(cursor.getLastBlock(),HighlightTypes.TERNARY);
            return true;
        }
        return false;
    };

    this.reduce=function(targetPrio)
    {
        if (parseStack.length >= 2)
        {
            const argument = parseStack.pop();
            const operator = last(parseStack);
            let argumentPriority = argument.priority();
            let operatorPriority = operator.priority();
            let reduce = operatorPriority>=targetPrio && operator.argumentsTypes!==undefined;

            if(argument.type === NodeTypes.TERNARY &&
                operator.type === NodeTypes.TERNARY &&
                targetPrio === 0
            ){
                reduce = false;
            }

            if(LOG) {
                log(">>>>>>>>>");
                log("NEW: prio " + targetPrio+"");
                log("ARG:" + argument.operator + "(prio " + argumentPriority+")");
                log("OPR:" + operator.operator + "(prio " + operatorPriority+")");
                if(reduce) log("REDUCE");
                log("<<<<<<<<");

            }
            if(reduce){
                operator.arguments.push(argument);
                this.reduce(targetPrio);
            } else {
                parseStack.push(argument);
                //consume argument
            }
        }
    };


    this.withUnaryLib=function(unaryLib){
        this.unaryLib=unaryLib;
        return this;
    };
    this.withFunctionLib=function(functionLib){
        this.functionLib=functionLib;
        return this;
    };

    this.withInfixLib=function(infixLib){
        this.infixLib=infixLib;
        return this;
    };

    this.parseExpression=function(until){
        while(!this.done() && (until===null ||  until===undefined || !this.cursor.at(until))) {
            log("========");
            this.suggestions.clear();
            if( this.tryLongConstant() ||
                this.tryBooleanConstant() ||
                this.tryDoubleConstant() ||
                this.tryStringConstant() ||
                this.tryVariable() ||
                this.tryBrackets() ||
                this.tryTernary() ||
                this.libScan(this.unaryLib, this.tryUnary, this.unarySuggestions) ||
                this.libScan(this.functionLib, this.tryFunction, this.functionSuggestions) ||
                this.libScan(this.infixLib, this.tryInfix, this.infixSuggestions)){
                this.suggestions.clear();
                continue;
            }
            throw "Can't match head";
        }
        this.reduce(until==null?-1:0);
    };
    this.getBlocks=function(){
        return this.blocks;
    };
    this.getSuggestions=function(){
        return this.suggestions;
    };
    this.yield=function(prio){
        this.reduce(prio||0);
        return parseStack.pop();
    };

    this.empty=function(){
        return parseStack.length===0;
    };

    this.functionSuggestions=function(){
        var names = Object.keys(this.functionLib).sort();
        const suggestions =   names.map(name=>{ return {name: name, type: getReturnType(this.functionLib[name].returnType)};});
        return new SuggestionModelType('functions', suggestions);
    };

    this.infixSuggestions=function(){
        var names = Object.keys(this.infixLib).sort();
        const suggestions =   names.map(name=>{ return {name: name, type: getReturnType(this.infixLib[name].returnType)};});
        return new SuggestionModelType('infix', suggestions);
    };

    this.unarySuggestions=function(){
        const names = Object.keys(this.unaryLib).sort();
        const suggestions =  names.map(name=>{ return {name: name, type: getReturnType(this.unaryLib[name].returnType)};});
        return new SuggestionModelType('unary', suggestions);
    };

    this.constantSuggestions=function(){
        return new SuggestionModelType('constants', null, [
            {name: 'integer', type:  ReturnTypes.INTEGER, pattern: LONG_PATTERN},
            {name: 'decimal', type: ReturnTypes.DECIMAL, pattern: DOUBLE_PATTERN},
            {name: 'text', type: ReturnTypes.TEXT, pattern: TEXT_PATTERN},
            {name: 'boolean', type: ReturnTypes.BOOLEAN, pattern: /^(true|false)/}
        ]);
    };
}



export default ParseContext;
