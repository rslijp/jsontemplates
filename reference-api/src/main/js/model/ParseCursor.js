
export function ParseCursor(text){
    this.cursorIndex=0;
    this.text=text;
    this.full=text;

}

ParseCursor.prototype.more=function(){
    return this.text!=="";
}

ParseCursor.prototype.at=function(patternOrInstruction){
    if(typeof(patternOrInstruction) === 'string'){
        return this.text.startsWith(patternOrInstruction);
    } else {
        return patternOrInstruction.test(this.text);
    }
}

ParseCursor.prototype.read=function(patternOrInstruction) {
    if(typeof(patternOrInstruction) === 'string') {
        if(!this.at(patternOrInstruction)) throw "Expected "+patternOrInstruction;
        this.move(patternOrInstruction.length);
    } else {
        const match = patternOrInstruction.exec(this.text);
        if (match.length===0) throw "Expected pattern "+patternOrInstruction;
        var chunk = match[0];//match.group(match[0]);
        // this.move(match.end());
        this.move(chunk.length);
        return chunk;
    }
}

ParseCursor.prototype.move=function(length){
    this.text=this.text.substring(length);
    this.cursorIndex+=(length+(this.text.length-this.text.trim().length));
    this.text=this.text.trim();
}

ParseCursor.prototype.toString=function(){
    return this.text;
}

ParseCursor.prototype.getLeft=function(){
    return this.full.substring(this.cursorIndex);
}

ParseCursor.prototype.getFull=function(){
    return this.full;
}

ParseCursor.prototype.getIndex=function(){
    return this.cursorIndex;
}

export default ParseCursor;