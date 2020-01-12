export function isOptional(type) {
    return !(type===null || type===undefined) && type.endsWith("?");
}
export function validateCompletenessOfArguments(expr) {
    if(expr===null || expr===undefined) return;
    const argumentsTypes = expr.argumentsTypes;
    if(argumentsTypes===undefined) return;
    for (var j = expr.arguments.length; j < argumentsTypes.length; j++) {
        if (!isOptional(argumentsTypes[j])) {
            throw "Expected more arguments";
        }
    }
}