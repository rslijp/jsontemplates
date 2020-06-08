
export function ExceptionWithSuggestion(text, suggestions){

    this.toString=function () {
        return text;
    };

    this.getSuggestions=function(){
        return suggestions;
    };
}
export default ExceptionWithSuggestion;