const React = require('react');
import Node from './Node'

function NodeList({nodes, allNodes}) {

    const nodesList = nodes.map(node =>
        <Node key={node.id} node={node} allNodes={allNodes}/>
    );


    return (
        <div>
            <h2>Available</h2>
            {nodesList}
        </div>
    );

}

export default NodeList