const React = require('react');
const _ = require('underscore');
import {Accordion, Button, Card, Col, Container, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronCircleDown, faChevronCircleUp} from "@fortawesome/free-solid-svg-icons";
import Optional from "./Optional";

class Node extends React.Component {

    constructor(props) {
        super(props);
        this.state={isCollapsed: false};
    }

    toggleIcon(){
        this.setState({isCollapsed: !this.state.isCollapsed});
    }

    renderArguments(argumentTypes){
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

    renderNodeSlots(nodeSlots){
        if(!nodeSlots) return (<Row className="mb-2"><Col>-</Col></Row>);
        return Object.entries(nodeSlots).map(([k,v]) => {
            let value = v;
            const optional = v.endsWith("?")?<Optional/>:null;
            if(optional){
                value=value.substr(0,value.length-1);
            }
            if(value==='limited'){
                const refIds = this.props.node.nodeSlotLimits[k];
                var refNodes = _.filter(this.props.allNodes, node=>refIds.includes(node.id));
                value = _.pluck(refNodes,"name").join(", ");
            } else if(value==='*'){
                value = "any";
            }
            return (<Row className="mb-2" key={k}><Col sm>{k} <br/>{optional}</Col><Col className='font-weight-light'>{value}</Col></Row>);
        });
    }

    render() {
        const argumentTypes = this.renderArguments(this.props.node.argumentTypes);
        const nodeSlots = this.renderNodeSlots(this.props.node.nodeSlots);

        return (
            <Accordion bsPrefix={"_"}>
                <Card className="mb-3" style={{ width: '18rem' }}>
                    <Card.Header>{this.props.node.name}
                        <Accordion.Toggle className="float-right" as={Button} variant="link" eventKey={'card'+this.props.node.name} onClick={this.toggleIcon.bind(this)}>
                            <FontAwesomeIcon className="if-not-collapsed" icon={this.state.isCollapsed?faChevronCircleUp:faChevronCircleDown} />
                        </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey={'card'+this.props.node.name}>
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
}

export default Node