import React from 'react';
import {ListGroup} from "react-bootstrap";
import SuggestionModel from '../model/SuggestionModel';

class SuggestionBox extends React.Component {

    constructor(props) {
        super(props);
    }
    renderList(model, single){
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
                {displayModel.models.map(item => this.renderList(item,displayModel.single))}
                </ListGroup>)
        // if(models[0]) return this.renderList(models[0]);
        // else return null;
    }
}

export default SuggestionBox