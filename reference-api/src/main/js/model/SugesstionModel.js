import _ from "underscore";
import { typesMatch } from "./ExpressionTypeChecker"
import {ReturnTypes} from "../Constants";

class SuggestionModel {
    constructor(type, options, patternOptions){
        this.type=type;
        this.options=options;
        this.patternOptions=patternOptions;
        // this.collectVariableSuggestions=this.collectVariableSuggestions.bind(this);
    }

    static collectVariableSuggestions(model){
        return _.forEach(_.filter(model.propertyDescriptions, i=>i.readable) ,i=>{return {name: i.name, type: i.type}});
    }

    filterOnPartial(partialMatch){
        if(!partialMatch || partialMatch.length===0) return this;
        let options = this.options||[];
        options=_.filter(options, (option) => option.name.startsWith(partialMatch));
        return new SuggestionModel(this.type, options, this.patternOptions||[]);
    }

    filterOnReturnType(type){
        const expectedType = type !== ReturnTypes.OBJECT?type:ReturnTypes.GENERICOPTIONAL;
        // console.log("Looking for",type,"in",this.type);
        let options = this.options||[];
        options=_.filter(options, (option) => {
            // console.log("option",option);
            return typesMatch(option.type,expectedType)
        });
        let patternOptions = this.patternOptions||[];
        patternOptions=_.filter(patternOptions, (option) => {
            // console.log("patternOption",option);
            return typesMatch(option.type,expectedType)
        });
        return new SuggestionModel(this.type, options, patternOptions);
    }

    asDisplayModel(partialMatch){
        let options = this.options||[];
        let patternOptions = this.patternOptions||[];
        if(partialMatch && partialMatch.length>0) {
            options=options.map(item=>[item.name,partialMatch,item.name.substr(partialMatch.length)]);
        } else {
            options=options.map(item=>[item.name,'',item.name]);
        }
        patternOptions=patternOptions.map(item=>[item.name,'',item.name]);
        return {
            type: this.type,
            hits: options.length>0  || patternOptions.length>0,
            options: options,
            patternOptions: patternOptions
        }
    }
}

export default SuggestionModel