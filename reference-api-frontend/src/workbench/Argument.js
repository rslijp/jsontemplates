import {Col, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import {bool, string} from "prop-types";
import ErrorBoundary from "../common/ErrorBoundary";
import Expression from "./Expression";
import Optional from "../common/Optional";
import SimpleModeExpression from "./SimpleModeExpression";
import {isArgumentAConstant} from "../model/JsonTemplate";

function Argument({optional,path,argumentName,type}) {

    const isConstant=isArgumentAConstant(path, argumentName, ['text', 'boolean', 'integer']);
    const [expressionMode, setExpressionMode] = useState(!isConstant);
    const optionalIndicator = optional?<Optional/>:null;
    const toggle = ()=>{
        if(!isConstant) return;
        setExpressionMode(!expressionMode);
    };

    const switchToggle = <Form.Check
        type={'switch'}
        disabled={!isConstant}
        readOnly={false}
        label={argumentName}
        checked={expressionMode}
        onChange={toggle}
    />;
    return(<Row className="mb-2" >
        <Col sm={"2"} onClick={toggle}>{switchToggle}<br/>{optionalIndicator}</Col>
        <Col className='font-weight-light'>
            <ErrorBoundary>
                {expressionMode ?
                    <Expression optional={optional} path={path} argumentName={argumentName} type={type}/> :
                    <SimpleModeExpression optional={optional} path={path} argumentName={argumentName} type={type}/>
                }
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