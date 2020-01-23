import React from 'react';
import {ListGroup} from "react-bootstrap";
import _ from 'underscore';

class SuggestionBox extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let suggestions = null;
        const model  = this.props.suggestions;
        if(model) {
            console.log(model);
            let options = model.options||[];
            const metaOptions = model.metaOptions||[];
            const partialMatch= model.partialMatch;
            if(partialMatch && partialMatch.length>0) {
                options = _.filter(options, (option) => option.startsWith(partialMatch));
                options=options.map(item=>[item,partialMatch,item.substr(partialMatch.length)]);
            } else {
                options=options.map(item=>[item,'',item]);
            }
            console.log(options);
            suggestions = (<ListGroup style={{zIndex: 1, position: 'absolute'}}>
                <ListGroup.Item variant="secondary" key={model.type}>{model.type}</ListGroup.Item>
                {options.map(item => <ListGroup.Item key={model.type+"-"+item[0]}><span style={{textDecoration: 'underline'}}>{item[1]}</span>{item[2]}</ListGroup.Item>)}
                {metaOptions.map(item => <ListGroup.Item key={model.type+"-"+item[0]}><i>{item}</i></ListGroup.Item>)}
            </ListGroup>)
        }
        return suggestions;
    }
}

export default SuggestionBox