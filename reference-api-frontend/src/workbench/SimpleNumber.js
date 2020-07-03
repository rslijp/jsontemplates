import {any, func} from "prop-types";
import {FormControl} from "react-bootstrap";
import React from "react";

function SimpleNumber({value,updateValue}){
    const updateNumber = (text)=>{
        if(text!==null || text!==undefined) {
            const match = text.toString().match(/^[0-9]*$/);
            if (!match) {
                text=value;
            }
        }
        var checked = text?text.toString():'';
        updateValue(checked);
    };
    const formatNumber = (s)=>{
        if(!s) return '';
        if(typeof s === "number") s=s.toString();
        const match = s.match(/^[0-9]+$/);
        if(!match) throw "Bug";
        return s;
    };

    return <FormControl value={formatNumber(value)} onChange={(e)=>updateNumber(e.target.value)}/>;
}
SimpleNumber.propTypes = {
    value: any,
    updateValue: func
};
export default SimpleNumber;