const React = require('react');
import Node from './Node'
import { observe, getAvailableNodes } from './AllowedNodes'

class NodeList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {nodes: []};
        observe(this.updateState.bind(this));
    }

    updateState(nodes){
        console.log("UPDATE STATE")
        this.setState({nodes:nodes});
    }


    render() {
        const nodesList = this.state.nodes.map(node =>
            <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
        );


        return (
            <div className="position-fixed">
                <h2>Available</h2>
                {nodesList}
            </div>
        );
    }
}

// function NodeList({allNodes}) {
//     var nodes = getAvailableNodes();
//
//     const nodesList = nodes.map(node =>
//         <Node key={node.id} node={node} allNodes={allNodes}/>
//     );
//
//
//     return (
//         <div class="position-fixed">
//             <h2>Available</h2>
//             {nodesList}
//         </div>
//     );
//
// }

export default NodeList