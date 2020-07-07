import {Col, Row} from "react-bootstrap";
import React, {useState} from "react";
import {bool, string} from "prop-types";
import {faCode, faFont, faList, faPuzzlePiece} from "@fortawesome/free-solid-svg-icons";
import {hasAllowedValues, isArgumentAConstant, isArgumentAVariable} from "../model/JsonTemplate";
import ErrorBoundary from "../common/ErrorBoundary";
import Expression from "./Expression";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Optional from "../common/Optional";
import SimpleModeAllowedValues from "./SimpleModeAllowedValues";
import SimpleModeExpression from "./SimpleModeExpression";
import SimpleModeVariable from "./SimpleModeVariable";

function Argument({optional,path,argumentName,type}) {
    let initialState = 0;
    const [allowedValues]=useState(hasAllowedValues(path, argumentName));
    const [isVariable,setIsVariable]=useState(isArgumentAVariable(path, argumentName));
    const [isConstant,setIsConstant]=useState(isArgumentAConstant(path, argumentName));
    if(isConstant) initialState=allowedValues?3:1;
    else if(isVariable) initialState=2;

    const [state, setState] = useState(initialState);
    const optionalIndicator = optional?<Optional/>:null;
    const calcNextState = (next)=>{
        if(next === 4) next=0;
        if(next === 1 && !isArgumentAConstant(path, argumentName)) return calcNextState(next+1);
        if(next === 2 && !isArgumentAVariable(path, argumentName)) return calcNextState(next+1);
        if(next === 3 && !(isArgumentAConstant(path, argumentName) && allowedValues)) return calcNextState(next+1);
        setState(next);
    };
    const toggle = ()=>{
        setIsVariable(isArgumentAVariable(path, argumentName));
        setIsConstant(isArgumentAConstant(path, argumentName));
        calcNextState(state+1);
    };

    const switchToggle = <FontAwesomeIcon className="text-secondary" icon={
        [faCode,faFont,faPuzzlePiece,faList][state]} style={{minWidth: '24px', textAlign: 'left'}}/>;
    const input = [
        <Expression key={"expresion"} optional={optional} path={path} argumentName={argumentName} type={type}/>,
        <SimpleModeExpression key={"simple"} optional={optional} path={path} argumentName={argumentName} type={type}/>,
        <SimpleModeVariable  key={"variable"} optional={optional} path={path} argumentName={argumentName} type={type}/>,
        <SimpleModeAllowedValues  key={"variable"} optional={optional} path={path} argumentName={argumentName} type={type}/>,
    ][state];

    return(<Row className="mb-2" >
        <Col sm={"2"} onClick={toggle} style={{cursor: 'pointer', paddingLeft: '0px'}}>{switchToggle} {argumentName}<br/>{optionalIndicator}</Col>
        <Col className='font-weight-light'>
            <ErrorBoundary>
                {input}
            </ErrorBoundary>
        </Col>
    </Row>);
}
Argument.propTypes = {
    optional: bool,
    type: string,
    path: string,
    argumentName: string
};
export default Argument;