package nl.softcause.jsontemplates.expressions.util;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;

public class DateFormatterUtils {

    private static Integer parseEnum(String patternValue) {
        if(patternValue == null) return null;
        if(patternValue.equals("SHORT")) return DateFormat.SHORT;
        if(patternValue.equals("MEDIUM")) return DateFormat.MEDIUM;
        if(patternValue.equals("LONG")) return DateFormat.LONG;
        return null;
    }

    public static DateFormat buildFormatter(String pattern, Locale locale){
        var mode = parseEnum(pattern);
        if(pattern==null){
            return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
        }
        if(mode!=null){
            return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, mode, locale);
        }
        return new SimpleDateFormat(pattern, locale);
    }

    SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT);

    public static String formatToIso(Instant instant){
        return new StdDateFormat().format(instant);
    }
}
