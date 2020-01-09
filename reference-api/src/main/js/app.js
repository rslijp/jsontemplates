'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const _ = require('underscore');
import NodeList from './available/NodeList'
import WorkBench from './workbench/WorkBench'
import { DndProvider } from 'react-dnd'
import Backend from 'react-dnd-html5-backend'
import {Col, Container, Row} from "react-bootstrap";
import { setAvailableNodes, setGlobalNodes } from './available/AllowedNodes'

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {mainNodeIds: [], mainModelId: null, modelDescriptions: {}, expressionDescriptions: [], nodeDescriptions:[]};

	}

	loadFromServer() {
		client("/api/node/main",null,data => {
			// var applicableNodes = _.filter(data.nodeDescriptions, node=>data.mainNodeIds.includes(node.id));
			setGlobalNodes(data.mainNodeIds);
			setAvailableNodes(data.nodeDescriptions, data.mainNodeIds);
			this.setState({
				mainNodeIds: data.mainNodeIds,
				mainModelId: data.mainModelId,
				modelDescriptions: data.modelDescriptions,
				expressionDescriptions: data.expressionDescriptions,
				nodeDescriptions: data.nodeDescriptions,
			});
		});
	}

	componentDidMount() {
		this.loadFromServer();
	}

	render() {
		var workbench = (
			<Container id="root" fluid={true}>
				<Row>
					<Col xs={3}>
						<NodeList allNodes={this.state.nodeDescriptions}/>
					</Col>
					<Col xs={9}>
						<WorkBench allNodes={this.state.nodeDescriptions} />
					</Col>
				</Row>
			</Container>
		)
		return <DndProvider backend={Backend}>{workbench}</DndProvider>
	}
}



ReactDOM.render(
	<App />,
	document.getElementById('react')
)