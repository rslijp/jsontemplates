const React = require('react');
import {Jumbotron,Container} from "react-bootstrap";

class WorkBench extends React.Component {

    constructor(props) {
        super(props);
    }


    render() {
        return (
            <div className={"workBench"}>
                <h2>Workbench</h2>
                <Jumbotron>
                <Container>
                    Slot
                </Container>
                </Jumbotron>
            </div>
        )
    }

}

export default WorkBench