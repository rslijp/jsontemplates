import {ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import {any, func} from "prop-types";
import React from "react";

function SimpleBoolean({value, updateValue}){
    const updateBoolean = (v)=>{
        const checked = v===''?null:v.toString();
        updateValue(checked);
    };

    const formatBoolean = (s)=>{
        if(s===null || s===undefined) return '';
        if(s==='true'||s==='false') return s==='true';
        return s;
    };
    return <div className={"form-control"} style={{padding: "3px"}}>
        <ToggleButtonGroup size='sm' type="radio" name="options" defaultValue={formatBoolean(value)} onChange={(option) => updateBoolean(option)}>
            <ToggleButton style={{minWidth: "60px"}} size='sm' value={''} > - </ToggleButton>
            <ToggleButton style={{minWidth: "60px"}} size='sm' value={true} >yes</ToggleButton>
            <ToggleButton style={{minWidth: "60px"}} size='sm' value={false}>no </ToggleButton>
        </ToggleButtonGroup>
    </div>;
}
SimpleBoolean.propTypes = {
    value: any,
    updateValue: func
};
export default SimpleBoolean;