import _ from 'underscore';
import ParseCursor from "./ParseCursor";
const LOG = true;
const LONG_PATTERN = /^(-?[0-9]+)/;
const DOUBLE_PATTERN = /^(-?[0-9]+\.[0-9]+)/;
const TEXT_PATTERN  = /^'((\S|\s)+)'/;
const VARIABLE_PATTERN  = /^\$([0-9A-Za-z\\.]+)/;
const BRACKET_OPEN = "(";
const PARAMETER_SEPARATOR = ",";
const BRACKET_CLOSE = ")";

function log(msg){
    if(LOG) console.log(msg);
}


export function ParseContext(text){
    const cursor= new ParseCursor(text);
    const parseStack = this.parseStack = [];
    this.cursor= cursor;
    this.unaryLib={};
    this.infixLib={};
    this.functionLib={};

    function push(type, x){
        console.log(x);
        parseStack.push(type);
    }

    function createExpression(type){
        console.log(type);
        return {arguments: [], argumentsTypes:[], priority: 9}
    }

    function justSawConstant() {
        if(parseStack.length === 0) return false;
        return _.last(parseStack) === "Constant";
    }

    this.done=function() {
        return !this.cursor.more();
    };

    this.libScan=function(lib, tryParse){
        const operators = _.sortBy(_.keys(lib), e => -e.length);
        _.each(operators, e=>{
            if(tryParse.call(this, e)) return true;
        });
        return false;
    };

    this.tryVariable=function() {
        if (cursor.at(VARIABLE_PATTERN)) {
            const txt = cursor.read(VARIABLE_PATTERN);
            push("Variable","new Variable("+txt+")");
            return true;
        }
        return false;
    };

    this.tryBooleanConstant=function() {
        if (cursor.at("true")){
            cursor.read("true");
            push("Constant","new Constant(true)");
            return true;
        }
        if (cursor.at("false")){
            cursor.read("false");
            push("Constant","new Constant(false)");
            return true;
        }
        return false;
    };

    this.tryLongConstant = function() {
        if (!justSawConstant() &&
            !cursor.at(DOUBLE_PATTERN) &&
            cursor.at(LONG_PATTERN)) {
            const txt = cursor.read(LONG_PATTERN);
            push("Constant","new Constant("+parseInt(txt)+"L)");
            return true;
        }
        return false;
    };

    this.tryDoubleConstant=function() {
        if (!justSawConstant() &&
            cursor.at(DOUBLE_PATTERN)) {
            console.log("x");
            const txt = cursor.read(DOUBLE_PATTERN);
            push("Constant","new Constant("+parseFloat(txt)+")");
            return true;
        }
        return false;
    };

    this.tryStringConstant=function() {
        if (cursor.at(TEXT_PATTERN)) {
            console.log("ÿ");
            const txt = cursor.read(TEXT_PATTERN);
            push("Constant","new Constant("+txt+")");
            return true;
        }
        return false;
    };

    this.tryFunction=function(functionName) {
        console.log("tryFunction",functionName)
        if (cursor.at(functionName)) {
            cursor.read(functionName);
            cursor.read(BRACKET_OPEN);
            const expr = createExpression(this.functionLib[functionName]);
            const argumentsTypes = expr.argumentsTypes;
            for (let i = 0; i< argumentsTypes.length; i++){
                const separators = [BRACKET_CLOSE, PARAMETER_SEPARATOR];
                this.parseExpression(separators);
                expr.arguments.push(this.yield());
                if(cursor.at(BRACKET_CLOSE)){
                    for (let j = expr.arguments.length; j < argumentsTypes.length; j++){
                        if(!(argumentsTypes[j].type === "Optional")){
                            throw "Expected more arguments"
                        }
                    }
                    cursor.read(BRACKET_CLOSE);
                    break;
                }
                cursor.read(PARAMETER_SEPARATOR);
            }
            push(expr);
            return true;
        }
        return false;
    };

    this.tryInfix=function(operator){
        console.log("tryInfix",operator)
        if (cursor.at(operator) && !this.empty()) {
            cursor.read(operator);
            const expr = createExpression(this.infixLib[operator]);
            this.reduce(expr.priority());
            const lhs = parseStack.pop();
            log("POP "+operator(lhs));
            expr.arguments.push(lhs);
            push(expr);
            return true;
        }
        return false;
    };

    this.tryUnary=function(operator){
        console.log("tryUnary",operator)
        if (cursor.at(operator)) {
            cursor.read(operator);
            const expr = createExpression(this.unaryLib[operator]);
            push(expr);
            return true;
        }
        return false;
    };

    this.tryBrackets=function() {
        if (cursor.at(BRACKET_OPEN)) {
            cursor.read(BRACKET_OPEN);
            this.parseExpression([BRACKET_CLOSE]);
            const inner = this.yield();
            const brackets = {arguments:[]};//new Brackets();//broken
            brackets.arguments.push(inner);
            push(brackets);
            cursor.read(BRACKET_CLOSE);
            return true;
        }
        return false;
    };

     this.tryTernary=function() {
        if (cursor.at("?") && !this.empty()) {
            cursor.read("?");
            const ternary = {arguments: [], priority: 3};
            const condition = this.yield(ternary.priority);//broken
            ternary.arguments.push(condition);
            push(ternary);
            this.parseExpression([":"]);
            cursor.read(":");
            return true;
        }
        return false;
    };

    this.reduce=function(targetPrio)
    {
        console.log(targetPrio, parseStack)
        if (parseStack.length >= 2)
        {
            const argument = parseStack.pop();
            const operator = _.last(parseStack);
            let argumentPriority = argument.priority;
            let operatorPriority = operator.priority;
            let reduce = operatorPriority>=targetPrio && operator.type === 'IExpressionWithArguments';

            if(argument.type === 'Ternary' &&
                operator.type === 'Ternary' &&
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
}

ParseContext.prototype.withUnaryLib=function(unaryLib){
    this.unaryLib=unaryLib;
    return this;
};
ParseContext.prototype.withFunctionLib=function(functionLib){
    this.functionLib=functionLib;
    return this;
};

ParseContext.prototype.withInfixLib=function(infixLib){
    this.infixLib=infixLib;
    return this;
};

ParseContext.prototype.parseExpression=function(until){
    while(!this.done() && (until===null ||  until===undefined || !this.cursor.at(until))) {
        log("========");
        if( this.tryLongConstant() || //tested
            this.tryBooleanConstant() || //tested
            this.tryDoubleConstant() || //tested
            this.tryStringConstant() || //tested
            this.tryVariable() ||  //tested
            this.tryBrackets() ||
            this.tryTernary() ||
            this.libScan(this.unaryLib, this.tryUnary) ||
            this.libScan(this.functionLib, this.tryFunction) ||
            this.libScan(this.infixLib, this.tryInfix)){
            continue;
        }
        throw "Can't match head"
    }
    this.reduce(until==null?-1:0);
};

ParseContext.prototype.yield=function(){
    this.reduce(0);
    return this.parseStack.pop();
};

ParseContext.prototype.empty=function(){
    return this.parseStack.length===0;
};

export default ParseContext