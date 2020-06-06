import _ from "underscore";
import { typesMatch, resolveGenericType } from "./ExpressionTypeChecker"
import {ReturnTypes} from "../Constants";

class SuggestionModelType {
    constructor(type, options, patternOptions,partialMatch){
        this.type=type;
        this.options=options||[];
        this.patternOptions=patternOptions||[];
        this.partialMatch=partialMatch;
        // this.collectVariableSuggestions=this.collectVariableSuggestions.bind(this);
    }

    static collectVariableSuggestions(model){
        return new SuggestionModelType('variable',
            _.map(
                _.filter(model.propertyDescriptions, i=>i.readable) ,
                    i=>{return {name: i.name, type: i.type}})
            );
    }
    any(){
        return (this.options.length>0 || this.patternOptions.length>0)
    }
    single(){
        return this.options.length===1;
    }
    completion(){
        if(!this.single()) return null;
        var option = this.options[0];
        return option.name.substr((this.partialMatch||"").length);
    }
    filterOnPartial(partialMatch){
        this.partialMatch=partialMatch;
        if(!partialMatch || partialMatch.length===0) return this;
        let options = this.options||[];
        options=_.filter(options, (option) => option.name.startsWith(partialMatch));
        return new SuggestionModelType(this.type, options, this.patternOptions||[], this.partialMatch);
    }

    filterOnReturnType(type){
        const expectedType = type !== ReturnTypes.OBJECT?type:ReturnTypes.GENERICOPTIONAL;
        // console.log("Looking for",type,"in",this.type);
        let options = this.options||[];
        options=_.filter(options, (option) => {
            if(option.type===undefined) return true;
            return typesMatch(resolveGenericType(option.type, expectedType),expectedType)
        });
        let patternOptions = this.patternOptions||[];
        patternOptions=_.filter(patternOptions, (option) => {
            // console.log("patternOption",option);
            if(option.type===undefined) return true;
            return typesMatch(option.type,expectedType)
        });
        // console.log(options);
        return new SuggestionModelType(this.type, options, patternOptions, this.partialMatch);
    }

    asDisplayModel(){
        let options = this.options||[];
        let patternOptions = this.patternOptions||[];
        const partialMatch=this.partialMatch;
        if(partialMatch && partialMatch.length>0) {
            options=options.map(item=>[item.name,partialMatch,item.name.substr(partialMatch.length)]);
        } else {
            options=options.map(item=>[item.name,'',item.name]);
        }
        patternOptions=patternOptions.map(item=>[item.name,'',item.name]);
        return {
            type: this.type,
            hits: options.length>0  || patternOptions.length>0,
            single: (options.length+patternOptions.length)===1,
            options: options,
            patternOptions: patternOptions
        }
    }
}

export default SuggestionModelType