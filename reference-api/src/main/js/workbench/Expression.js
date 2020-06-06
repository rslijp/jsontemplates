import React from 'react';
import {InputGroup, Alert} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faTimes, faBolt} from "@fortawesome/free-solid-svg-icons";
import {parse} from '../model/ExpressionParser';
import {getReturnType} from '../model/ExpressionModel';
import {checkExpression} from '../model/ExpressionTypeChecker';
import {getModelDefinition} from '../model/ModelDefinition';
import CaretPositioning from '../common/EditCaretPositioning'
import { ReturnTypes} from "../Constants";
import highlight from './SyntaxHighlighting';
import SuggestionBox from './SuggestionBox';
import SuggestionModel from "../model/SuggestionModel";

let id = 0;
class Expression extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: "id" + (id++),
            valid: true,
            error: null,
            typeValid: props.optional,
            text: "",
            partialMatch: "",
            blocks: [],
            suggestions: null,
            caretPosition: {
                start: 0,
                end: 0
            }
        };
        this.expectedType = getReturnType(props.type);
        this.displayType= props.optional?props.type.substr(0,props.type.length-1):props.type;
        this.onKeyUpWeb = this._onKeyUpWeb.bind(this);
        this.onChange = this._onChange.bind(this);
        this.onBlur = this._onBlur.bind(this);
        this.onFocus = this._onFocus.bind(this);
        this.onKeyDown = this._onKeyDown.bind(this);
    }

    _onBlur(){
        this.setState({
            blocks: [],
            suggestions: null,
            caretPosition: {
                start: 0,
                end: 0
            }
        });
    }

    _onFocus(e){
        const result = this.onChange(e.target.innerText);

        this.setState(
            {
                ...result,
            }
        )
    }


    _onChange(text){
         const result = parse(text||"", getModelDefinition());
         let valid = false;
         let typeValid = false;
         if(result.success){
            if(result.result===null) {
                valid=this.props.optional;
                typeValid=true;
            } else {
                valid = true;
                // console.log(result.expression);
                const expectedType = this.expectedType !== ReturnTypes.OBJECT?this.expectedType:ReturnTypes.GENERICOPTIONAL;
                const typeResult = checkExpression(result.expression, getModelDefinition(), expectedType, false);
                typeValid = typeResult.succes;
                result.error = typeResult.error;
                if(typeResult.suggestions) result.suggestions.add(typeResult.suggestions);
            }
        } else {
            valid=false;
            typeValid=true;
        }
        return {
            valid: valid,
            typeValid: typeValid,
            text: text,
            partialMath: result.partialMatch,
            blocks: result.blocks,
            error: result.error,
            suggestions: result.suggestions.filterOnReturnType(this.expectedType)
        };
    }

    _onKeyUpWeb(e) {
        // e.stopPropagation();
        const savedCaretPosition = CaretPositioning.saveSelection(e.currentTarget);
        const result = this.onChange(e.target.innerText);
        this.setState(
            {
                    ...result,
                caretPosition: savedCaretPosition
            },() => {
                //restore caret position(s)
                CaretPositioning.restoreSelection(document.getElementById(this.state.id), this.state.caretPosition);
            }
        )
    }

    _onKeyDown(e){
        if(e.ctrlKey && e.keyCode===32 && this.state.suggestions.single()){
            var completion = this.state.suggestions.completion();
            const savedCaretPosition = CaretPositioning.saveSelection(e.currentTarget);
            const result = this.onChange(this.state.text+completion);
            savedCaretPosition.start+=completion.length;
            savedCaretPosition.end+=completion.length;
            this.setState(
                {
                    ...result,
                    caretPosition: savedCaretPosition
                },() => {
                    //restore caret position(s)
                    CaretPositioning.restoreSelection(document.getElementById(this.state.id), this.state.caretPosition);
                }
            )
            // this.onChange(e.target.innerText);
        }
    }

    render() {
        const html = highlight(this.state.text, this.state.blocks);

        const input = <div id={this.state.id}
                           tabIndex="0" style={{backgroundColor: 'darkgrey'}}
                           suppressContentEditableWarning={true} contentEditable={true}
                           type="text" className="form-control " aria-label="Amount (to the nearest dollar)"
                           onInput={this.onKeyUpWeb}
                           onKeyDown={this.onKeyDown}
                           onBlur={this.onBlur}
                           onFocus={this.onFocus}
                           dangerouslySetInnerHTML={{__html: html}}>{}</div>;
        let errorAlert = null;
        if(this.state.error){
            errorAlert = <Alert variant="danger">
                    {this.state.error.toString()}
            </Alert>
        }
        return (
            <div>
            <InputGroup className="mb-3">
                    <InputGroup.Prepend>
                        <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px",color: this.state.typeValid?"#212529":"red"}}>{this.displayType}</div></InputGroup.Text>
                    </InputGroup.Prepend>
                    {input}
                    <InputGroup.Append>
                        <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: this.state.valid?(this.state.typeValid?"green":"orange"):"red"}} icon={this.state.valid?(this.state.typeValid?faCheck:faBolt):faTimes} /></InputGroup.Text>
                    </InputGroup.Append>
                </InputGroup>
                {errorAlert}
                <SuggestionBox suggestions={this.state.suggestions} expectedType={this.expectedType}/>
            </div>
        )
    }
}

export default Expression