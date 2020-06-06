import _ from "underscore";
import {HighlightTypes} from "../Constants";

function renderBlock(val, type, error){
    let style = '';
    if(type.font==='bold'){
        style = 'font-weight: bold'
    } else if(type.font==='italic'){
        style = 'font-style: italic'
    } else if(type.font==='italic-bold'){
        style = 'font-style: italic; font-weight: bold'
    }
    let className = '';
    if(error){
        className="class='errorBlock'";
    }
    return "<span "+className+" style='color: "+type.color+"; "+style+"'>"+val+"</span>";
}


function convertToHtml(text, blocks){
    var newHTML = [];
    var end = 0;
    _.forEach(blocks,(block)=>{
        var val = text.substr(block.start, block.end-block.start);
        newHTML.push(renderBlock(val, block.type));
        end = block.end;
    });

    var unparsed=text.substr(end);
    if(unparsed.length>0){
        newHTML.push(renderBlock(unparsed, HighlightTypes['UNKNOWN'],true));
    }
    return newHTML.join("");
}

export default convertToHtml;