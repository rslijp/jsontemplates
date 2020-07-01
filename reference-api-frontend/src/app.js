'use strict';

import './css/main.css';
import { AllowNodesProvider, setAvailableNodes, setGlobalNodes } from './available/AllowNodesProvider'
import { VariablesProvider, setVariables } from './variables/VariableProvider'
import React from "react";
import ReactDOM from "react-dom";
import {get} from './client';
import {initExpressionLibrary} from './model/ExpressionParser'
import {initModelDefinition} from './model/ModelDefinition'
import MainApp from "./MainApp";
import {setSlots} from "./model/JsonTemplate";

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {initialized: false, mainNodeIds: [], mainModelId: null, modelDescriptions: {}, expressionDescriptions: [], nodeDescriptions:[]};

	}

	loadFromServer() {
		var id = document.location.hash.substring(1);
		get(id,data => {
			const description = data.description;
			initExpressionLibrary(description.expressionDescriptions);
			setGlobalNodes(description.mainNodeIds);
			setAvailableNodes(description.nodeDescriptions, description.mainNodeIds);
			setVariables(description.modelDescriptions);
			initModelDefinition(description.modelDescriptions[0]);
            setSlots(data.template,description.nodeDescriptions);
            this.setState({
				id: id,
				initialized: true,
				commitUrl: data.commitUrl,
				cancelUrl: data.cancelUrl,
				mainNodeIds: description.mainNodeIds,
				mainModelId: description.mainModelId,
				modelDescriptions: description.modelDescriptions,
				expressionDescriptions: description.expressionDescriptions,
				nodeDescriptions: description.nodeDescriptions,
			});

		});
	}

	componentDidMount() {
		this.loadFromServer();
	}

	render() {
		let app = <></>;
		if(this.state.initialized) {
			app=<MainApp id={this.state.id} nodeDescriptions={this.state.nodeDescriptions} commitUrl={this.state.commitUrl} cancelUrl={this.state.cancelUrl}/>;
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