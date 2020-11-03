package nl.softcause.jsontemplates.expressions.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.util.DateFormatterUtils;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParseDateTest {

    @Before
    public void setUp() {
        DateFormatterUtils.FORCE_DEFAULT_LOCALE = TimeZone.getTimeZone("Europe/Amsterdam");
    }

    @After
    public void tearDown() {
        DateFormatterUtils.FORCE_DEFAULT_LOCALE = null;
    }

    @Test
    public void should_parse_constant() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("Aug 26, 2019, 8:22:10 AM")));
        var r = parseDate.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(d));
    }

    private Instant trim(Instant d) {
        return d.minus(d.get(ChronoField.NANO_OF_SECOND), ChronoUnit.NANOS);
    }

    @Test
    public void should_parse_constant_using_model_locale() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("26 août 2019 à 08:22:10")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseDate.evaluate(model);

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_with_pattern_short() {
        var d = trim(Instant.parse("2019-08-26T06:22:00.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("Aug 26, 2019, 8:22 AM"), new Constant("SHORT")));
        var r = parseDate.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_using_model_locale_with_pattern_short() {
        var d = trim(Instant.parse("2019-08-26T06:22:00.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("26 août 2019 08:22"), new Constant("SHORT")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseDate.evaluate(model);

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_with_pattern_medium() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("Aug 26, 2019, 8:22:10 AM"), new Constant("MEDIUM")));
        var r = parseDate.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_using_model_locale_with_pattern_medium() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(Arrays.asList(new Constant("26 août 2019 à 08:22:10"), new Constant("MEDIUM")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseDate.evaluate(model);

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_with_pattern_long() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate =
                new ParseDate(Arrays.asList(new Constant("Aug 26, 2019, 8:22:10 AM CEST"), new Constant("LONG")));
        var r = parseDate.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_using_model_locale_with_pattern_long() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate =
                new ParseDate(Arrays.asList(new Constant("26 août 2019 à 08:22:10 CEST"), new Constant("LONG")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseDate.evaluate(model);

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_with_pattern() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(
                Arrays.asList(new Constant("26-08-2019T08:22:10"), new Constant("dd-MM-yyyy'T'HH:mm:ss")));
        var r = parseDate.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(d));
    }

    @Test
    public void should_parse_constant_using_model_locale_with_pattern() {
        var d = trim(Instant.parse("2019-08-26T06:22:10.533293Z"));
        var parseDate = new ParseDate(
                Arrays.asList(new Constant("26-août-2019T08:22:10"), new Constant("dd-MMM-yyyy'T'HH:mm:ss")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseDate.evaluate(model);

        assertThat(r, is(d));
    }


    @Test
    public void should_accept_null() {
        var parseDate = new ParseDate(Arrays.asList(new Constant(null)));

        var r = parseDate.evaluate(null);

        assertThat(r, nullValue());
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var parseDate = new ParseDate(Arrays.asList(new Constant(37)));

        var json = new ObjectMapper().writeValueAsString(parseDate);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(parseDate, is(obj));
    }
}
