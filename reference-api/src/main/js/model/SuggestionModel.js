import _ from "underscore";
import { typesMatch } from "./ExpressionTypeChecker"
import {ReturnTypes} from "../Constants";

class SuggestionModel {
    constructor(typeModels){
        this.typeModels=typeModels||[];
    }
    any(){
        if(this.typeModels.length===0) return false;
        return _.any(this.typeModels, model=>model.any());
    }
    single(){
        if(this.typeModels.length!==1) return false;
        return this.typeModels[0].single();
    }
    completion(){
        if(!this.single()) return null;
        return this.typeModels[0].completion();
    }
    filterOnPartial(partialMatch){
        const displayTypeModels=this.typeModels.map(typeModel=>typeModel.filterOnPartial(partialMatch));
        return new SuggestionModel(_.filter(displayTypeModels,item=>item.any()));
    }

    filterOnReturnType(type){
        const displayTypeModels=this.typeModels.map(typeModel=>typeModel.filterOnReturnType(type));
        return new SuggestionModel(_.filter(displayTypeModels,item=>item.any()));
    }

    asDisplayModel(){
        let displayTypeModels=this.typeModels.map(typeModel=>typeModel.asDisplayModel());
        const single = displayTypeModels.length===1;
        displayTypeModels.forEach(model=>model.single=model.single&&single);
        return {
            single: _.all(displayTypeModels, model=>model.single),
            models: displayTypeModels
        }
    }

    add(typeModel){
        if(typeModel) {
            this.typeModels.push(typeModel);
        }
        return this;
    }
    clear(){
        this.typeModels=[];
    }
}

export default SuggestionModel