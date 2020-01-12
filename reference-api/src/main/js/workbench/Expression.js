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
    const dislpayType=optional?type.substr(0,type.length-1):type;;
    const [isValid, setValid] = useState(
        true
    );
    const [isTypeValid, setTypeValid] = useState(
        optional
    );

    function onChange(e,v){
        e.stopPropagation();
        var result = parse(e.target.value||"");
        if(result.success){
            if(result.result===null) {
                setValid(optional);
                setTypeValid(true);
            } else {
                setValid(true);
                setTypeValid(checkExpression(result.expression,getModelDefinition(),expectedType,false));
                // const actualType = result.expression.returnType();
                // console.log(expectedType, actualType);
                // setTypeValid(typesMatch(expectedType,actualType));
            }
        } else {
            setValid(false);
            setTypeValid(true);
        }

    }



    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px",color: isTypeValid?"#212529":"red"}}>{dislpayType}</div></InputGroup.Text>
            </InputGroup.Prepend>
            <FormControl
                placeholder="expression"
                aria-label="expression"
                aria-describedby="basic-addon1"
                onChange={onChange}
            />
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: isValid?(isTypeValid?"green":"orange"):"red"}} icon={isValid?(isTypeValid?faCheck:faBolt):faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>

    )
}

export default Expression