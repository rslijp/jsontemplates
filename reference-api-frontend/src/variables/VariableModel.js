import {Accordion, Button, Card, Col, Container, Row} from "react-bootstrap";
import React, { useState } from 'react';
import {faChevronCircleDown, faChevronCircleUp} from "@fortawesome/free-solid-svg-icons";
import Flag from "../common/Flag";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {any} from "prop-types";
import {last} from "../utils/ArrayUtil";

function VariableModel({model}) {

    function renderProperties(properties){
        if(!properties) return (<Row className="mb-2"><Col>-</Col></Row>);
        return properties.map((property) => {
            let flags = null;
            if(!property.readable || !property.writable){
                flags=<Flag name={!property.readable?"read only":"write only"}/>;
            }
            return (<Row className="mb-2" key={property.name}><Col sm>{property.name} <br/>{flags}</Col><Col className='font-weight-light'>{property.type}</Col></Row>);
        });
    }

    const cardStyle = { width: '18rem' };
    const properties = renderProperties(model.propertyDescriptions);

    const [isCollapsed, setCollapsed] = useState(
        true
    );


    function toggleIcon(){
        setCollapsed(!isCollapsed);
    }

    function simpleTypeName(name){
        const parts = name.split('.');
        return last(parts);
    }
    return (
        <Accordion bsPrefix={"_"}>
            <Card className="mb-3"
                style={{
                    ...cardStyle,
                }}>
                <Card.Header>
                    <span style={{lineHeight: "1.7em", verticalAlign: "middle",padding:"0.375em", display: "inline-block"}}>{simpleTypeName(model.type)}</span>
                    <Accordion.Toggle className="float-right" as={Button} variant="link" eventKey={'card'+model.type} onClick={toggleIcon}>
                        <FontAwesomeIcon icon={isCollapsed?faChevronCircleDown:faChevronCircleUp} />
                    </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey={'card'+model.type}>
                    <Card.Body>
                        <dt>Properties</dt>
                        <dd>
                            <Container style={{paddingLeft: "0px", paddingRight: "0px"}}>
                                {properties}
                            </Container>
                        </dd>
                    </Card.Body>
                </Accordion.Collapse>
            </Card>
        </Accordion>);
}
VariableModel.propTypes = {
    model: any
};
export default VariableModel;