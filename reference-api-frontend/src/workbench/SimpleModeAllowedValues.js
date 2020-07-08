import {Form,InputGroup} from "react-bootstrap";
import React, {useState} from "react";
import {bool, string} from "prop-types";
import {faCheck, faTimes} from "@fortawesome/free-solid-svg-icons";
import {getAllowedValues, getConstantArgumentValue, updateExpression} from "../model/JsonTemplate";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

function SimpleModeAllowedValues({optional,path,argumentName,type}){
    const baseType= optional? type.substr(0,type.length-1):type;
    const shouldPad = (baseType==='text' || baseType==='enum');
    const convertTo=(v)=>{
        if(v==='') return '';
        if(shouldPad) return "'"+v+"'";
        return v;
    };
    const convertFrom=(v)=>{
        if(v==='') return '';
        if(shouldPad) return v.substring(1, v.length-1);
        return v;
    };
    const [value, setValue] = useState(convertFrom(getConstantArgumentValue(path, argumentName)||''));
    const [allowedValues,setAllowedValues]=useState(value?getAllowedValues(path, argumentName, baseType):[]);
    const [valid, setValid] = useState(!!(value!==null||optional));
    const refreshList = ()=>{
        setAllowedValues(getAllowedValues(path, argumentName, baseType));
    };


    const updateValue = (option)=>{
        const padded=(option===null)?'':option;
        updateExpression(path, argumentName, convertTo(padded));
        setValue(option);
        setValid(!!(padded!==""||optional));
    };
    let selectOptions = allowedValues.map(o=><option key={o} value={o}>{o}</option>);
    return (<div>
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text id="basic-addon1"><div style={{minWidth: "100px"}}>{baseType}</div></InputGroup.Text>
            </InputGroup.Prepend>
            <Form.Control as="select" value={value} onChange={(e)=>updateValue(e.target.value)} onFocus={refreshList}>
                <option key="-" value={''}>-</option>
                {selectOptions}
            </Form.Control>
            <InputGroup.Append>
                <InputGroup.Text id="basic-addon2"><FontAwesomeIcon style={{width: "24px", color: valid?"green":"red"}} icon={valid?faCheck:faTimes} /></InputGroup.Text>
            </InputGroup.Append>
        </InputGroup>
    </div>);
}
SimpleModeAllowedValues.propTypes = {
    optional: bool,
    type: string,
    path: string,
    argumentName: string
};
export default SimpleModeAllowedValues;