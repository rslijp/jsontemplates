'use strict';

import {Col, Container, Row} from "react-bootstrap";
import { setAvailableNodes, setGlobalNodes } from './available/AllowedNodes'
import Backend from 'react-dnd-html5-backend'
import { DndProvider } from 'react-dnd'
import React from "react";
import ReactDOM from "react-dom";
import NodeList from './available/NodeList'
import WorkBench from './workbench/WorkBench'
import client from './client';
import {initExpressionLibrary} from './model/ExpressionParser'
import {initModelDefinition} from './model/ModelDefinition'

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {mainNodeIds: [], mainModelId: null, modelDescriptions: {}, expressionDescriptions: [], nodeDescriptions:[]};

	}

	loadFromServer() {
		client("/api/node/main",null,data => {
			initExpressionLibrary(data.expressionDescriptions);
			setGlobalNodes(data.mainNodeIds);
			setAvailableNodes(data.nodeDescriptions, data.mainNodeIds);
			initModelDefinition(data.modelDescriptions[0]);
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