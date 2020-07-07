import {any, arrayOf} from "prop-types";
import {getGlobalNodes, setAvailableNodes} from "../available/AllowNodesProvider";
import {getSlots, observe, slotNodes} from "../model/JsonTemplate";
import EmptySlot from "./EmptySlot";
import React from 'react';
import Slot from "./Slot";

class WorkBench extends React.Component {

    constructor(props) {
        super(props);
        this.state = {slots: getSlots()};
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
            <>
                <h5>For</h5>
                {slots}
                <EmptySlot path="push"/>
            </>
        );
    }

}
WorkBench.propTypes = {
    allNodes: arrayOf(any)
};
export default WorkBench;