'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const _ = require('underscore');
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronCircleUp, faChevronCircleDown } from '@fortawesome/free-solid-svg-icons'
import Accordion from 'react-bootstrap/Accordion';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {mainNodeIds: [], mainModelId: null, modelDescriptions: {}, expressionDescriptions: [], nodeDescriptions:[],applicableNodes:[]};
	}

	loadFromServer() {
		client("/api/node/main",null,data => {
			console.log(data);
			var applicableNodes = _.filter(data.nodeDescriptions, node=>data.mainNodeIds.includes(node.id));
			this.setState({
				mainNodeIds: data.mainNodeIds,
				mainModelId: data.mainModelId,
				modelDescriptions: data.modelDescriptions,
				expressionDescriptions: data.expressionDescriptions,
				nodeDescriptions: data.nodeDescriptions,
				applicableNodes: applicableNodes
			});
		});
	}

	componentDidMount() {
		this.loadFromServer();
	}

	render() {
		return (
			<div className="container">
				<NodeList nodes={this.state.applicableNodes} allNodes={this.state.nodeDescriptions}/>
			</div>
		)
	}
}

class NodeList extends React.Component {

	constructor(props) {
		super(props);
	}


	render() {
		const nodes = this.props.nodes.map(node =>
			<Node key={node.id} node={node} allNodes={this.props.allNodes}/>
		);

		return (
			<div>
					<h2>Available</h2>
					{nodes}
			</div>
		)
	}

}

class Node extends React.Component {

	constructor(props) {
		super(props);
		this.state={isCollapsed: false};
	}

	toggleIcon(){
		this.setState({isCollapsed: !this.state.isCollapsed});
	}

	render() {
		const argumentTypes = this.props.node.argumentTypes?Object.entries(this.props.node.argumentTypes).map(([k,v]) => {
			return (<div className="row mb-2" key={k}><div className='col-sm'>{k}</div><div className='col font-weight-light'>{v}</div></div>);
		}):(<div className="row mb-2"><div className='col'>-</div></div>);
		const nodeSlots = this.props.node.nodeSlots?Object.entries(this.props.node.nodeSlots).map(([k,v]) => {
			let value = v;
			const optional = v.endsWith("?")?(<i><br/>(optional)</i>):null;
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
			return (<div className="row mb-2" key={k}><div className='col-sm'>{k} {optional}</div><div className='col font-weight-light'>{value}</div></div>);
		}):(<div className="row mb-2"><div className='col'>-</div></div>);

		return (
			<Accordion>
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
								<div className="container">
									{argumentTypes}
								</div>
							</dd>
							<dt>Slots</dt>
							<dd><div className="container">
								{nodeSlots}
							</div></dd>
						</Card.Body>
					</Accordion.Collapse>
			</Card>
			</Accordion>)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)