import React from 'react';
import {InputGroup} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faTimes, faBolt} from "@fortawesome/free-solid-svg-icons";
import {parse} from '../model/ExpressionParser';
import {getReturnType} from '../model/ExpressionModel';
import {checkExpression} from '../model/ExpressionTypeChecker';
import {getModelDefinition} from '../model/ModelDefinition';
import CaretPositioning from '../common/EditCaretPositioning'
import _ from 'underscore';
// import {HighlightTypes} from '../Constants';


let id = 0;
class Expression extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: "id"+(id++),
            valid: true,
            typeValid: props.optional,
            text: "",
            blocks: [],
            caretPosition : {
                start : 0,
                end : 0
            }
        };
        this.expectedType = getReturnType(props.type);
        this.displayType= props.optional?props.type.substr(0,props.type.length-1):props.type;
        this.onKeyUpWeb = this._onKeyUpWeb.bind(this);
        this.onChange = this._onChange.bind(this);
    }


     _onChange(text){
         const result = parse(text||"");
         console.log(result);
         let valid = false;
         let typeValid = false;
         if(result.success){
            if(result.result===null) {
                valid=this.props.optional;
                typeValid=true;
            } else {
                valid = true;
                console.log(result.expression);
                typeValid=checkExpression(result.expression,getModelDefinition(),this.expectedType,false);
            }
        } else {
            valid=false;
            typeValid=true;
        }
        return {
            valid: valid,
            typeValid: typeValid,
            text: text,
            blocks: result.blocks
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


    render() {
        var p = 0;
        var newHTML = [];
        //     // Loop through words
        // console.log(this.state.blocks);
        var end = 0;
        _.forEach(this.state.blocks,(block)=>{
            // console.log(block);
            var val = this.state.text.substr(block.start, block.end-block.start);
            var type = block.type;
            // console.log("key", val);
            let style = '';
            if(type.font==='bold'){
                style = 'font-weight: bold'
            } else if(type.font==='italic'){
                style = 'font-style: italic'
            } else if(type.font==='italic-bold'){
                style = 'font-style: italic; font-weight: bold'
            }
            newHTML.push("<span style='color: "+type.color+"; "+style+"'>"+val+"</span>");
            end = block.end;
        });

        const html = newHTML.join("")+this.state.text.substr(end);
        console.log(html);

        const input = <div id={this.state.id} tabIndex="0" style={{backgroundColor: 'darkgrey'}} suppressContentEditableWarning={true} contentEditable={true} type="text" className="form-control " aria-label="Amount (to the nearest dollar)" onInput={this.onKeyUpWeb} dangerouslySetInnerHTML={{__html: html}}>{}</div>;

        return (<InputGroup className="mb-3">
                    <InputGroup.Prepend>
                        <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px",color: this.state.typeValid?"#212529":"red"}}>{this.displayType}</div></InputGroup.Text>
                    </InputGroup.Prepend>
                    {input}
                    <InputGroup.Append>
                        <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: this.state.valid?(this.state.typeValid?"green":"orange"):"red"}} icon={this.state.valid?(this.state.typeValid?faCheck:faBolt):faTimes} /></InputGroup.Text>
                    </InputGroup.Append>
                </InputGroup>
        )
    }
}

export default Expression