import {Button, Col, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import {bool, string} from "prop-types";
import ErrorBoundary from "../common/ErrorBoundary";
import Expression from "./Expression";
import SimpleModeExpression from "./SimpleModeExpression";
import SimpleModeVariable from "./SimpleModeVariable";
import Optional from "../common/Optional";
import {isArgumentAConstant,isArgumentAVariable} from "../model/JsonTemplate";
import {faCode, faFont, faPuzzlePiece, faList} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

function Argument({optional,path,argumentName,type}) {
    let initialState = 0;
    const [isVariable,setIsVariable]=useState(isArgumentAVariable(path, argumentName));
    const [isConstant,setIsConstant]=useState(isArgumentAConstant(path, argumentName, ['text', 'boolean', 'integer']));
    if(isConstant) initialState=1;
    else if(isVariable) initialState=2;

    const [state, setState] = useState(initialState);
    const optionalIndicator = optional?<Optional/>:null;
    const calcNextState = (next)=>{
        if(next === 3) next=0;
        if(next === 1 && !isArgumentAConstant(path, argumentName,['text', 'boolean', 'integer'])) return calcNextState(next+1);
        if(next === 2 && !isArgumentAVariable(path, argumentName)) return calcNextState(next+1);
        setState(next);
    };
    const toggle = ()=>{
        setIsVariable(isArgumentAVariable(path, argumentName));
        setIsConstant(isArgumentAConstant(path, argumentName, ['text', 'boolean', 'integer']));
        calcNextState(state+1);
    };

    const switchToggle = <FontAwesomeIcon className="text-secondary" icon={
        [faCode,faFont,faPuzzlePiece,faList][state]} style={{minWidth: '24px', textAlign: 'left'}}/>;

    const input = [
        <Expression optional={optional} path={path} argumentName={argumentName} type={type}/>,
        <SimpleModeExpression optional={optional} path={path} argumentName={argumentName} type={type}/>,
        <SimpleModeVariable optional={optional} path={path} argumentName={argumentName} type={type}/>
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