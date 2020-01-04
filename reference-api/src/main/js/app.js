'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const _ = require('underscore');
import NodeList from './NodeList'
import { DndProvider } from 'react-dnd'
import Backend from 'react-dnd-html5-backend'

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
		var workbench= (
			<div className="container">
				<NodeList nodes={this.state.applicableNodes} allNodes={this.state.nodeDescriptions}/>
			</div>
		)
		return <DndProvider backend={Backend}>{workbench}</DndProvider>
	}
}



ReactDOM.render(
	<App />,
	document.getElementById('react')
)