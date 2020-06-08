
import {any, arrayOf} from "prop-types";
import { groupBy, last , sortOn} from '../utils/ArrayUtil';
import Node from './Node';
import React from 'react';
import { observe } from './AllowedNodes';

class NodeList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {nodes: []};
        observe(this.updateState.bind(this));
    }

    updateState(nodes){
        this.setState({nodes:nodes});
    }



    render() {
        const bundles = groupBy(this.state.nodes.sort(sortOn('packageName')), 'packageName');
        const groupsList = bundles.map((nodes, bundle) => {
            const nodesList = nodes.map(node =>
                <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
            );
            const names = bundle.split(".");
            const name = last(names);
            return  <div key={bundle}>
                <h5>{name}</h5>
                {nodesList}
            </div>;
        }
        );

        return (
            <div className="position-fixed" style={{height: "100%", overflowY: "scroll", paddingRight: "15px"}}>
                <h2>Available</h2>
                {groupsList}
            </div>
        );
    }
}
NodeList.propTypes = {
    allNodes: arrayOf(any)
};
export default NodeList;