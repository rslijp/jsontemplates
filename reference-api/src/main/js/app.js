'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const _ = require('underscore');
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronCircleUp, faChevronCircleDown } from '@fortawesome/free-solid-svg-icons'

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

// tag::create-dialog[]
class CreateDialog extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(e) {
		e.preventDefault();
		const newEmployee = {};
		this.props.attributes.forEach(attribute => {
			newEmployee[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
		});
		this.props.onCreate(newEmployee);

		// clear out the dialog's inputs
		this.props.attributes.forEach(attribute => {
			ReactDOM.findDOMNode(this.refs[attribute]).value = '';
		});

		// Navigate away from the dialog to hide it.
		window.location = "#";
	}

	render() {
		const inputs = this.props.attributes.map(attribute =>
			<p key={attribute}>
				<input type="text" placeholder={attribute} ref={attribute} className="field"/>
			</p>
		);

		return (
			<div>
				<a href="#createEmployee">Create</a>

				<div id="createEmployee" className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Create new employee</h2>

						<form>
							{inputs}
							<button onClick={this.handleSubmit}>Create</button>
						</form>
					</div>
				</div>
			</div>
		)
	}

}
// end::create-dialog[]

class NodeList extends React.Component {

	constructor(props) {
		super(props);
		// this.handleNavFirst = this.handleNavFirst.bind(this);
		// this.handleNavPrev = this.handleNavPrev.bind(this);
		// this.handleNavNext = this.handleNavNext.bind(this);
		// this.handleNavLast = this.handleNavLast.bind(this);
		// this.handleInput = this.handleInput.bind(this);
	}

	// handleInput(e) {
	// 	e.preventDefault();
	// 	const pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
	// 	if (/^[0-9]+$/.test(pageSize)) {
	// 		this.props.updatePageSize(pageSize);
	// 	} else {
	// 		ReactDOM.findDOMNode(this.refs.pageSize).value =
	// 			pageSize.substring(0, pageSize.length - 1);
	// 	}
	// }

	// // tag::handle-nav[]
	// handleNavFirst(e){
	// 	e.preventDefault();
	// 	this.props.onNavigate(this.props.links.first.href);
	// }
	//
	// handleNavPrev(e) {
	// 	e.preventDefault();
	// 	this.props.onNavigate(this.props.links.prev.href);
	// }
	//
	// handleNavNext(e) {
	// 	e.preventDefault();
	// 	this.props.onNavigate(this.props.links.next.href);
	// }
	//
	// handleNavLast(e) {
	// 	e.preventDefault();
	// 	this.props.onNavigate(this.props.links.last.href);
	// }
	// // end::handle-nav[]

	// tag::employee-list-render[]
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
	// end::employee-list-render[]
}

class Node extends React.Component {

	constructor(props) {
		super(props);
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
			<div id={'card'+this.props.node.name}>
				<div className="card mb-3" style={{width: '18rem'}}>
					<div id={'heading'+this.props.node.name} className="card-header">
						{this.props.node.name}
						<button className="btn btn-sm float-right collapsed" data-toggle="collapse" data-target={'#collapse'+this.props.node.name}
								aria-expanded="false" aria-controls={'collapse'+this.props.node.name}>
							<FontAwesomeIcon className="if-not-collapsed" icon={faChevronCircleUp} />
							<FontAwesomeIcon className="if-collapsed" icon={faChevronCircleDown} />
						</button>
					</div>
					<div id={'collapse'+this.props.node.name} className="collapse">
						<div className="card-body">
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
					</div>
					</div>
				</div>
			</div>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)