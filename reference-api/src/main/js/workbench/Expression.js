import React, { useState } from 'react';
import {InputGroup,FormControl} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faTimes, faBolt} from "@fortawesome/free-solid-svg-icons";
import {parse} from '../model/ExpressionParser';
import {getReturnType} from '../model/ExpressionModel'

function Expression({type, optional}) {
    const [isValid, setValid] = useState(
        optional
    );
    const [isTypeValid, setTypeValid] = useState(
        true
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
                const expected = getReturnType(type);
                const actual = result.result.returnType();
                console.log(expected, actual);
                setTypeValid(expected === actual);
            }
        } else {
            setValid(false);
            setTypeValid(true);
        }

    }



    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px",color: isTypeValid?"#212529":"red"}}>{type}</div></InputGroup.Text>
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