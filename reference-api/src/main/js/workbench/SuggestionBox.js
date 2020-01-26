import React from 'react';
import {ListGroup} from "react-bootstrap";
import _ from 'underscore';

class SuggestionBox extends React.Component {

    constructor(props) {
        super(props);
    }
    renderList(model){
        let suggestions = [];
        if(model) {
            console.log(model);
            let options = model.options||[];
            let patternOptions = model.patternOptions||[];
            const partialMatch= model.partialMatch;
            if(partialMatch && partialMatch.length>0) {
                options = _.filter(options, (option) => option.name.startsWith(partialMatch));
                options=options.map(item=>[item.name,partialMatch,item.name.substr(partialMatch.length)]);
            } else {
                options=options.map(item=>[item.name,'',item.name]);
            }
            patternOptions=patternOptions.map(item=>[item.name,'',item.name]);
            console.log(options);
            suggestions.push(<ListGroup.Item variant="secondary" key={model.type}>{model.type}</ListGroup.Item>);
            suggestions=suggestions.concat(options.map(item => <ListGroup.Item key={model.type+"-"+item[0]}><span style={{textDecoration: 'underline'}}>{item[1]}</span>{item[2]}</ListGroup.Item>));
            suggestions=suggestions.concat(patternOptions.map(item => <ListGroup.Item key={model.type+"-"+item[0]}><i>{item[2]}</i></ListGroup.Item>));
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