const React = require('react');
import Slot from "./Slot";
import EmptySlot from "./EmptySlot";
import {observe} from "./JsonTemplate";

class WorkBench extends React.Component {

    constructor(props) {
        super(props);
        this.state = {slots: []};
        observe(this.updateState.bind(this));
    }

    updateState(slots){
        console.log("UPDATE STATE")
        this.setState({slots:slots});
    }

    render() {
        const slots = this.state.slots.map((node,i) =>{
            // console.log(node.name);
            return (<Slot key={node.name} path={i} node={node} allNodes={this.props.allNodes}/>);
        });
        return (
            <div className={"workBench"}>
                <h2>Workbench</h2>
                {slots}
                <EmptySlot path="push"/>
            </div>
        )
    }

}

export default WorkBench