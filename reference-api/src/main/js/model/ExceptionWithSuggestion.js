
export function ExceptionWithSuggestion(text, partialMatch, suggestionType, suggestions){

    this.toString=function () {
        return text;
    }

    this.getSuggestions=function(){
        if(!suggestionType) return null;
        return {
            partialMatch: partialMatch,
            type: suggestionType,
            options: suggestions
        }
    }
}
export default ExceptionWithSuggestion