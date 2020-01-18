import React, { useState } from 'react';
import {InputGroup,FormControl} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faTimes, faBolt} from "@fortawesome/free-solid-svg-icons";
import {parse} from '../model/ExpressionParser';
import {getReturnType} from '../model/ExpressionModel';
import {checkExpression} from '../model/ExpressionTypeChecker';
import {getModelDefinition} from '../model/ModelDefinition';

function Expression({type, optional}) {
    const expectedType = getReturnType(type);
    const displayType=optional?type.substr(0,type.length-1):type;;
    const [isValid, setValid] = useState(
        true
    );
    const [isTypeValid, setTypeValid] = useState(
        optional
    );
    const [text, setText] = useState(
        ""
    );

    function onChange(text){
        var result = parse(text||"");
        if(result.success){
            if(result.result===null) {
                setValid(optional);
                setTypeValid(true);
            } else {
                setValid(true);
                setTypeValid(checkExpression(result.expression,getModelDefinition(),expectedType,false));
            }
        } else {
            setValid(false);
            setTypeValid(true);
        }
        console.log(text);
        setText(text);
    }

    // SQL keywords
    var keywords = ["SELECT","FROM","WHERE","LIKE","BETWEEN","NOT LIKE","FALSE","NULL","FROM","TRUE","NOT IN"];
// Keyup event
    function onKeyUpWeb(e){
        e.stopPropagation();
        var text = e.target.innerText;
        onChange(text)
        // setText(text);
        // Space key pressed
        // console.log(e.target.innerText);
        // console.log(e.currentTarget.innerText);
        // if (e.keyCode == 32){
        //     var newHTML = "";
        //     // Loop through words
        // text.replace(/[\s]+/g, " ").trim().split(" ").forEach(function(val){
        //         // If word is statement
        //         if (keywords.indexOf(val.trim().toUpperCase()) > -1)
        //             newHTML += "<span class='statement'>" + val + "&nbsp;</span>";
        //         else
        //             newHTML += "<span class='other'>" + val + "&nbsp;</span>";
        //         });
        //         $(this).html(newHTML);
        // //
        //     // Set cursor postion to end of text
        //     var child = $(this).children();
        //     var range = document.createRange();
        //     var sel = window.getSelection();
        //     range.setStart(child[child.length-1], 1);
        //     range.collapse(true);
        //     sel.removeAllRanges();
        //     sel.addRange(range);
        //     this.focus();
        // }
    };

    // var input = <FormControl
    //     placeholder="expression"
    //     aria-label="expression"
    //     aria-describedby="basic-addon1"
    //     onChange={onChange}
    // />
    // if(!optional){
    function render(){
        console.log("xxx", text);
        var newHTML = "";
        //     // Loop through words
        text.replace(/[\s]+/g, " ").trim().split(" ").forEach(function(val){
            // If word is statement
            if (keywords.indexOf(val.trim().toUpperCase()) > -1)
                newHTML += <span style='color: red'>{val}&nbsp;</span>;
            else
                newHTML += <span class='color: black'>{val}&nbsp;</span>;
        });
        console.log(newHTML);

        const input = <div tabIndex="0" suppressContentEditableWarning={true} contentEditable={true} type="text" className="form-control " aria-label="Amount (to the nearest dollar)" onKeyUp={onKeyUpWeb}>{text}</div>;
        return input
    }

    const input = render();

    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px",color: isTypeValid?"#212529":"red"}}>{displayType}</div></InputGroup.Text>
            </InputGroup.Prepend>
            {input}
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: isValid?(isTypeValid?"green":"orange"):"red"}} icon={isValid?(isTypeValid?faCheck:faBolt):faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>

    )
}

export default Expression