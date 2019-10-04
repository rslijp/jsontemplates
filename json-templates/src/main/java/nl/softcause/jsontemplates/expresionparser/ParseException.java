package nl.softcause.jsontemplates.expresionparser;


import lombok.Value;

import java.nio.file.WatchEvent;
import java.util.regex.Pattern;

public class ParseException extends RuntimeException {

    ParseException(String msg) {
        super(msg);
    }

    public static PartialParseException expected(Pattern pattern) {
        return new PartialParseException(String.format("Expected to find pattern %s",pattern.pattern()));
    }

    public static PartialParseException expected(String instruction) {
        return new PartialParseException(String.format("Expected to find text %s",instruction));
    }

    public static ParseException stackNotEmpty(String text) {
        return new ParseException(String.format("After parsing %s. The parse text is not empty. This is a bug for development."));
    }

    public static PartialParseException expectedMoreArguments() {
        return new PartialParseException("Expected more arguments");
    }

    public static PartialParseException cantMatchHead() {
        return new PartialParseException("Can't parse next segment.");
    }


    @Value
    static class PartialParseException {
        private String message;

        ParseException at(ParseCursor cursor){
            String phrase = cursor.getFull().substring(0,cursor.getIndex());
            phrase+="[ERROR]";
            phrase+=cursor.getFull().substring(cursor.getIndex());
            return new ParseException(String.format("%s. At index %d %s", message, cursor.getIndex(),phrase));
        }
    }
}
