import {any, func} from "prop-types";
import {FormControl} from "react-bootstrap";
import React from "react";

function SimpleText({value,updateValue}){
    const updateText = (text)=>{
        const padded = text?"'"+text+"'":null;
        updateValue(padded);
    };
    const formatText = (s)=>{
        if(!s) return '';
        const match = s.match(/'(.*)'/);
        if(!match) throw "Bug";
        return match[1];
    };
    return <FormControl value={formatText(value)} onChange={(e)=>updateText(e.target.value)}/>;
}
SimpleText.propTypes = {
    value: any,
    updateValue: func
};
export default SimpleText;