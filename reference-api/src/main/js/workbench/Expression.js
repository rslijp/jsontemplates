import React, { useState } from 'react';
import {InputGroup,FormControl} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faTimes} from "@fortawesome/free-solid-svg-icons";
import {parse} from '../model/ExpressionParser'

function Expression({type}) {
    const [isValid, setValid] = useState(
        true
    );

    function onChange(e,v){
        e.stopPropagation();
        setValid(parse(e.target.value||"").success);
    }



    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px"}}>{type}</div></InputGroup.Text>
            </InputGroup.Prepend>
            <FormControl
                placeholder="Username"
                aria-label="Username"
                aria-describedby="basic-addon1"
                onChange={onChange}
            />
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: isValid?"green":"red"}} icon={isValid?faCheck:faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>

    )
}

export default Expression