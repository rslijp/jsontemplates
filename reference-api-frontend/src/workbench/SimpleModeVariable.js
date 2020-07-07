import {Form,InputGroup} from "react-bootstrap";
import React, {useState} from "react";
import {bool, string} from "prop-types";
import {faCheck, faTimes} from "@fortawesome/free-solid-svg-icons";
import {getVariableArgumentValue, updateExpression} from "../model/JsonTemplate";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SuggestionModelType from "../model/SuggestionModelType";
import {getModelDefinition} from '../model/ModelDefinition';
import {typesMatch} from '../model/ExpressionTypeChecker';

function SimpleModeVariable({optional,path,argumentName,type}){
    const baseType= optional? type.substr(0,type.length-1):type;

    const [value, setValue] = useState(getVariableArgumentValue(path, argumentName)||'');
    const [valid, setValid] = useState(!!(value!==null||optional));

    const updateValue = (variableName)=>{
        variableName=variableName===null?"":variableName;
        updateExpression(path, argumentName, variableName);
        setValue(variableName);
        setValid(!!(variableName!==""||optional));
    };
    const options = SuggestionModelType.collectVariableSuggestions(getModelDefinition()).options;
    const applicable = options.filter(o=>typesMatch(o.type, type));
    let selectOptions = applicable.map(o=><option key={o.name} value={o.name}>${o.name}</option>);
    return (<div>
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px"}}>{baseType}</div></InputGroup.Text>
            </InputGroup.Prepend>
            <Form.Control as="select" value={value} onChange={(e)=>updateValue(e.target.value)}>
                <option key="-" value={""}>-</option>
                {selectOptions}
            </Form.Control>
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: valid?"green":"red"}} icon={valid?faCheck:faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>
    </div>);
}
SimpleModeVariable.propTypes = {
    optional: bool,
    type: string,
    path: string,
    argumentName: string
};
export default SimpleModeVariable;