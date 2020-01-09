import React, { useState } from 'react';

const _ = require('underscore');
import {Accordion, Button, Card, Col, Container, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronCircleDown, faChevronCircleUp} from "@fortawesome/free-solid-svg-icons";
import Optional from "../common/Optional";
import { useDrag } from 'react-dnd'
import { ItemTypes } from '../Constants'

function Node({node, allNodes}) {

    function renderArguments(argumentTypes){
        if(!argumentTypes) return (<Row className="mb-2"><Col>-</Col></Row>);
        return Object.entries(argumentTypes).map(([k,v]) => {
            let value = v;
            const optional = v.endsWith("?")?<Optional/>:null;
            if(optional){
                value=value.substr(0,value.length-1);
            }
            return (<Row className="mb-2" key={k}><Col sm>{k} <br/>{optional}</Col><Col className='font-weight-light'>{value}</Col></Row>);
        });
    }

    function renderNodeSlots(nodeSlots){
        if(!nodeSlots) return (<Row className="mb-2"><Col>-</Col></Row>);
        return Object.entries(nodeSlots).map(([k,v]) => {
            let value = v;
            const name = k.endsWith("Node")?k.substr(0,k.length-4):k
            const optional = v.endsWith("?")?<Optional/>:null;
            if(optional){
                value=value.substr(0,value.length-1);
            }
            if(value==='limited'){
                const refIds = node.nodeSlotLimits[k];
                var refNodes = _.filter(allNodes, node=>refIds.includes(node.id));
                value = _.pluck(refNodes,"name").join(", ");
            } else if(value==='*'){
                value = "any";
            }
            return (<Row className="mb-2" key={k}><Col sm>{name} <br/>{optional}</Col><Col className='font-weight-light'>{value}</Col></Row>);
        });
    }

    const cardStyle = { width: '18rem' };
    const argumentTypes = renderArguments(node.argumentTypes);
    const nodeSlots = renderNodeSlots(node.nodeSlots);
    const [{isDragging}, drag, preview] = useDrag({
        item: { type: ItemTypes.NODE, payload: node },
        collect: monitor => ({
            isDragging: !!monitor.isDragging(),
        }),
    })

    const [isCollapsed, setCollapsed] = useState(
        true
    );


    function toggleIcon(){
        setCollapsed(!isCollapsed);
    }

    return (
        <Accordion bsPrefix={"_"}>
            <Card className="mb-3"
                  ref={drag}
                  style={{
                      ...cardStyle,
                      opacity: isDragging ? 0.5 : 1,
                  }}>
                <Card.Header>{node.name}
                    <Accordion.Toggle className="float-right" as={Button} variant="link" eventKey={'card'+node.name} onClick={toggleIcon}>
                        <FontAwesomeIcon icon={isCollapsed?faChevronCircleDown:faChevronCircleUp} />
                    </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey={'card'+node.name}>
                    <Card.Body>
                        <dt>Arguments</dt>
                        <dd>
                            <Container>
                                {argumentTypes}
                            </Container>
                        </dd>
                        <dt>Slots</dt>
                        <dd>
                            <Container>
                                {nodeSlots}
                            </Container>
                        </dd>
                    </Card.Body>
                </Accordion.Collapse>
            </Card>
        </Accordion>)
}

export default Node