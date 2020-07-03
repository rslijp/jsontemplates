import React, {useState} from "react";
import {bool, string} from "prop-types";
import {faCheck, faTimes} from "@fortawesome/free-solid-svg-icons";
import {getConstantArgumentValue, updateExpression} from "../model/JsonTemplate";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {InputGroup} from "react-bootstrap";
import SimpleBoolean from "./SimpleBoolean";
import SimpleNumber from "./SimpleNumber";
import SimpleText from "./SimpleText";

function SimpleModeExpression({optional,path,argumentName,type}){
    const baseType= optional? type.substr(0,type.length-1):type;

    const [value, setValue] = useState(getConstantArgumentValue(path, argumentName));
    const [valid, setValid] = useState(!!(value!==null||optional));

    const updateValue = (checked)=>{
        updateExpression(path, argumentName, checked);
        setValue(checked);
        setValid(!!(checked!==null||optional));
    };

    let input = null;
    if(baseType==='text') input=<SimpleText value={value} updateValue={updateValue}/>;
    else if(baseType==='boolean') input=<SimpleBoolean value={value} updateValue={updateValue}/>;
    else if(baseType==='integer') input=<SimpleNumber value={value} updateValue={updateValue}/>;
    return (<div>
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px"}}>{baseType}</div></InputGroup.Text>
            </InputGroup.Prepend>
            {input}
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: valid?"green":"red"}} icon={valid?faCheck:faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>
    </div>);
}
SimpleModeExpression.propTypes = {
    optional: bool,
    type: string,
    path: string,
    argumentName: string
};
export default SimpleModeExpression;