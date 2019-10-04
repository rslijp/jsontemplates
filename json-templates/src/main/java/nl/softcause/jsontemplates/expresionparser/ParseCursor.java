package nl.softcause.jsontemplates.expresionparser;

import lombok.NonNull;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ParseCursor {

    private int cursorIndex=0;
    private String text;
    private String full;

    ParseCursor(@NonNull String text){
        this.text=text.trim();
        this.full=this.text;
    }

    boolean at(Pattern pattern){
        var match = pattern.matcher(text);
        return match.find();
    }

    boolean at(String instruction) {
        return text.startsWith(instruction);
    }

    String read(Pattern pattern) {
        var match = pattern.matcher(text);
        if(!match.find()) throw ParseException.expected(pattern).at(this);
        var chunk = match.group(1);
        move(match.end());
        return chunk;
    }

    void read(String instruction) {
         if(!at(instruction)) throw ParseException.expected(instruction).at(this);
        move(instruction.length());
    }

    private void move(int length) {
        text=text.substring(length);
        cursorIndex+=(length+(text.length()-text.trim().length()));
        text=text.trim();
    }

    boolean more(){
        return !text.isEmpty();
    }


    boolean at(String[] until) {
        return Arrays.stream(until).anyMatch(this::at);
    }

    @Override
    public String toString(){
        return text;
    }

    String getLeft() {
        return full.substring(cursorIndex);
    }

    String getFull() {
        return full;
    }

    int getIndex() {
        return cursorIndex;
    }
}
