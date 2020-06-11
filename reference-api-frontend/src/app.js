'use strict';

import './css/main.css';
import { AllowNodesProvider, setAvailableNodes, setGlobalNodes } from './available/AllowNodesProvider'
import { VariablesProvider, setVariables } from './variables/VariableProvider'
import React from "react";
import ReactDOM from "react-dom";
import client from './client';
import {initExpressionLibrary} from './model/ExpressionParser'
import {initModelDefinition} from './model/ModelDefinition'
import MainApp from "./MainApp";

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {initialized: false, mainNodeIds: [], mainModelId: null, modelDescriptions: {}, expressionDescriptions: [], nodeDescriptions:[]};

	}

	loadFromServer() {
		client("/api/node/main",null,data => {
			initExpressionLibrary(data.expressionDescriptions);
			setGlobalNodes(data.mainNodeIds);
			setAvailableNodes(data.nodeDescriptions, data.mainNodeIds);
			setVariables(data.modelDescriptions);
			initModelDefinition(data.modelDescriptions[0]);
			this.setState({
				initialized: true,
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
		let app = <></>;
		if(this.state.initialized) {
			app=<MainApp nodeDescriptions={this.state.nodeDescriptions}/>;
		}
		return (<AllowNodesProvider>
					<VariablesProvider>
					{app}
					</VariablesProvider>
				</AllowNodesProvider>);
	}
}



ReactDOM.render(
	<App />,
	document.getElementById('react')
)