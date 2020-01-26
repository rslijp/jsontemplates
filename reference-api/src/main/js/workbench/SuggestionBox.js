import React from 'react';
import {ListGroup} from "react-bootstrap";
import SuggestionModel from '../model/SugesstionModel';

class SuggestionBox extends React.Component {

    constructor(props) {
        super(props);
    }
    renderList(model){
        let suggestions = [];
        if(model) {
            const partialMatch=model.partialMatch;
            model = new SuggestionModel(model.type, model.options, model.patternOptions)
                .filterOnPartial(partialMatch)
                .filterOnReturnType(this.props.expectedType);
            const displayModel = model.asDisplayModel(partialMatch);
            if(displayModel.hits) {
                suggestions.push(<ListGroup.Item variant="secondary" key={displayModel.type}>{displayModel.type}</ListGroup.Item>);
                suggestions=suggestions.concat(displayModel.options.map(item => <ListGroup.Item key={displayModel.type+"-"+item[0]}><span style={{textDecoration: 'underline'}}>{item[1]}</span>{item[2]}</ListGroup.Item>));
                suggestions=suggestions.concat(displayModel.patternOptions.map(item => <ListGroup.Item key={displayModel.type+"-"+item[0]}><i>{item[2]}</i></ListGroup.Item>));
            }
        }
        return suggestions;
    }
    render() {
        const models  = this.props.suggestions||[];
        if(models.length===0) return null;

        return (<ListGroup style={{zIndex: 1, position: 'absolute'}}>
                {models.map(model => this.renderList(model))}
                </ListGroup>)
        // if(models[0]) return this.renderList(models[0]);
        // else return null;
    }
}

export default SuggestionBox