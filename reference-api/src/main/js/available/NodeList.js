const _ = require('underscore');

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
        console.log("UPDATE STATE",nodes);
        this.setState({nodes:nodes});
    }


    render() {
        const bundles = _.groupBy(this.state.nodes, 'packageName');
        const groupsList = _.map(bundles, (nodes, bundle) => {
                console.log(bundle, nodes);
                const nodesList = nodes.map(node =>
                    <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
                );
                var name = _.last(bundle.split("."));
                return  <div key={bundle}>
                    <h5>{name}</h5>
                    {nodesList}
                </div>
            }
        );
        // const nodesList = this.state.nodes.map(node =>
        //     <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
        // );


        return (
            <div className="position-fixed" style={{height: "100%", overflowY: "scroll", paddingRight: "15px"}}>
                <h2>Available</h2>
                {groupsList}
            </div>
        );
    }
}
// const bundles = _.groupBy(this.state.nodes, 'packageName');
// const groupsList = bundles.map((bundle,nodes) => {
//         const nodesList = nodes.map(node =>
//             <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
//         );
//         return  <div>
//             <h3>bundle</h3>
//             {nodesList}
//         </div>
//     }
// );
//
//
//
// return (
//     <div className="position-fixed">
//         <h2>Available</h2>
//         {groupsList}
//     </div>
// );

export default NodeList