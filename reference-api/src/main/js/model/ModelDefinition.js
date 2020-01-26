let modelDefinition= null;

export function initModelDefinition(definition){
    console.log("initModelDefinition",definition);
    modelDefinition=definition;
}

export function getModelDefinition(){
    return modelDefinition;
}