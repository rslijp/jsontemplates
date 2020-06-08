import {func, shape, string} from "prop-types";
import {ListGroup} from "react-bootstrap";
import React from 'react';

class SuggestionBox extends React.Component {

    constructor(props) {
        super(props);
    }
    renderList(model){
        let suggestions = [];
        suggestions.push(<ListGroup.Item variant="secondary" key={model.type}>{model.type}</ListGroup.Item>);
        suggestions=suggestions.concat(model.options.map(item => <ListGroup.Item key={model.type+"-"+item[0]} variant={model.single?"primary":null}><span style={{textDecoration: 'underline'}}>{item[1]}</span>{item[2]}</ListGroup.Item>));
        suggestions=suggestions.concat(model.patternOptions.map(item => <ListGroup.Item key={model.type+"-"+item[0]} variant={model.single?"primary":null}><i>{item[2]}</i></ListGroup.Item>));
        return suggestions;
    }
    render() {
        if(!this.props.suggestions) return null;
        const model  = this.props.suggestions;
        if(!model.any()) return null;
        const displayModel = model.asDisplayModel(this.props.partialMatch);
        return (<ListGroup style={{zIndex: 1, position: 'absolute'}}>
            {displayModel.models.map(item => this.renderList(item))}
        </ListGroup>);
    }
}
SuggestionBox.propTypes = {
    suggestions: shape({
        asDisplayModel: func
    }),
    partialMatch: string

};
export default SuggestionBox;