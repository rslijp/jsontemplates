const React = require('react');
import Node from './Node'

class NodeList extends React.Component {

    constructor(props) {
        super(props);
    }


    render() {
        const nodes = this.props.nodes.map(node =>
            <Node key={node.id} node={node} allNodes={this.props.allNodes}/>
        );

        return (
            <div>
                <h2>Available</h2>
                {nodes}
            </div>
        )
    }

}

export default NodeList