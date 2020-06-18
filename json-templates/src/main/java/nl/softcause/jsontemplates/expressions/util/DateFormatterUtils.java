package nl.softcause.jsontemplates.expressions.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatterUtils {

    public static TimeZone FORCE_DEFAULT_LOCALE = null;

    private static Integer parseEnum(String patternValue) {
        if (patternValue == null) {
            return null;
        }
        if (patternValue.equals("SHORT")) {
            return DateFormat.SHORT;
        }
        if (patternValue.equals("MEDIUM")) {
            return DateFormat.MEDIUM;
        }
        if (patternValue.equals("LONG")) {
            return DateFormat.LONG;
        }
        return null;
    }

    public static DateFormat internalBuildFormatter(String pattern, Locale locale) {
        var mode = parseEnum(pattern);
        if (pattern == null) {
            return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
        }
        if (mode != null) {
            return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, mode, locale);
        }
        return new SimpleDateFormat(pattern, locale);
    }

    public static DateFormat buildFormatter(String pattern, String timeZone, Locale locale) {
        var formatter = internalBuildFormatter(pattern, locale);
        if (timeZone != null) {
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        } else if (FORCE_DEFAULT_LOCALE != null) {
            formatter.setTimeZone(FORCE_DEFAULT_LOCALE);
        }
        return formatter;
    }

//    SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT);
//
//    public static String formatToIso(Instant instant) {
//        return new StdDateFormat().format(instant);
//    }
}
