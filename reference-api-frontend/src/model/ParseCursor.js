import {ExceptionWithSuggestion} from "./ExceptionWithSuggestion";
import SuggestionModelType from "./SuggestionModelType";

export function ParseCursor(text){
    this.cursorIndex=0;
    this.text=text;
    this.full=text;
    this.lastBlock=null;

}
function expectInstruction(instruction){
    return new SuggestionModelType('expected', [
        {name: instruction}
    ]);
}
ParseCursor.prototype.more=function(){
    return this.text!=="";
};

ParseCursor.prototype.at=function(patternOrInstruction){
    if(Array.isArray(patternOrInstruction)){
        return patternOrInstruction.some(i=>this.at(i));
    } else if(typeof(patternOrInstruction) === 'string'){
        return this.text.startsWith(patternOrInstruction);
    } else {
        return patternOrInstruction.test(this.text);
    }
};

ParseCursor.prototype.read=function(patternOrInstruction) {
    if(Array.isArray(patternOrInstruction)){
        //errrrm
        for(let i=0;i<patternOrInstruction.length;i++){
            if(this.at(patternOrInstruction[i])) {
                this.move(patternOrInstruction.length);
                return;
            }
            throw "Expected "+patternOrInstruction.join("|");
        }
    } else if(typeof(patternOrInstruction) === 'string') {
        if(!this.at(patternOrInstruction)) throw new ExceptionWithSuggestion("Expected "+patternOrInstruction, expectInstruction(patternOrInstruction));
        this.move(patternOrInstruction.length);
    } else {
        const match = patternOrInstruction.exec(this.text);
        if (match.length===0) throw "Expected pattern "+patternOrInstruction;
        const chunk = match[0];//match.group(match[0]);
        this.move(chunk.length);
        return chunk;
    }
};



ParseCursor.prototype.move=function(length){
    var start = this.cursorIndex;
    this.text=this.text.substring(length);
    this.cursorIndex+=length+(this.text.length-this.text.trimLeft().length);
    this.lastBlock = {
        start: start,
        end: this.cursorIndex
    };
    this.text=this.text.trimLeft();
};

ParseCursor.prototype.getLastBlock=function(){
    return this.lastBlock;
};

ParseCursor.prototype.toString=function(){
    return this.text;
};

ParseCursor.prototype.getLeft=function(){
    return this.full.substring(this.cursorIndex);
};

ParseCursor.prototype.getFull=function(){
    return this.full;
};

ParseCursor.prototype.getIndex=function(){
    return this.cursorIndex;
};

export default ParseCursor;