import {any, arrayOf} from "prop-types";
import {getGlobalNodes, setAvailableNodes} from "../available/AllowedNodes";
import {observe, slotNodes} from "../model/JsonTemplate";
import EmptySlot from "./EmptySlot";
import React from 'react';
import Slot from "./Slot";

class WorkBench extends React.Component {

    constructor(props) {
        super(props);
        this.state = {slots: []};
        observe(this.updateState.bind(this));
    }

    updateState(slots){
        setAvailableNodes(this.props.allNodes,slotNodes()||getGlobalNodes());
        this.setState({slots:slots});
    }

    render() {
        const slots = this.state.slots.map((node,i) =>{
            const path = i.toString();
            return (<Slot key={i} path={path} node={node} allNodes={this.props.allNodes}/>);
        });
        return (
            <div className={"workBench"}>
                <h2>Workbench</h2>
                {slots}
                <EmptySlot path="push"/>
            </div>
        );
    }

}
WorkBench.propTypes = {
    allNodes: arrayOf(any)
};
export default WorkBench;