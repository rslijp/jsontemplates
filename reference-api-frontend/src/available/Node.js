import {Accordion, Button, Card, Col, Container, Row} from "react-bootstrap";
import React, { useState } from 'react';
import {any, arrayOf} from "prop-types";
import {faChevronCircleDown, faChevronCircleUp} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { ItemTypes } from '../Constants';
import Optional from "../common/Optional";
import { useDrag } from 'react-dnd';

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
            const name = k.endsWith("Node")?k.substr(0,k.length-4):k;
            const optional = v.endsWith("?")?<Optional/>:null;
            if(optional){
                value=value.substr(0,value.length-1);
            }
            if(value==='limited'){
                const refIds = node.nodeSlotLimits[k];
                const refNodes = allNodes.filter(node=>refIds.includes(node.id));
                value =refNodes.map(node=>node.name).join(", ");
            } else if(value==='*'){
                value = "any";
            }
            return (<Row className="mb-2" key={k}><Col sm>{name} <br/>{optional}</Col><Col className='font-weight-light'>{value}</Col></Row>);
        });
    }

    const cardStyle = { width: '18rem' };
    const argumentTypes = renderArguments(node.argumentTypes);
    const nodeSlots = renderNodeSlots(node.nodeSlots);
    const [{isDragging}, drag] = useDrag({
        item: { type: ItemTypes.NODE, payload: node },
        collect: monitor => ({
            isDragging: !!monitor.isDragging(),
        }),
    });

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
                <Card.Header>
                    <span style={{lineHeight: "1.7em", verticalAlign: "middle",padding:"0.375em", display: "inline-block"}}>{node.name}</span>
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
        </Accordion>);
}
Node.propTypes = {
    node: any,
    allNodes: arrayOf(any)
};
export default Node;