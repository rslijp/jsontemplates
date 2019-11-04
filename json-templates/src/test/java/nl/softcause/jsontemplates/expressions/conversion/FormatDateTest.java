package nl.softcause.jsontemplates.expressions.conversion;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FormatDateTest {

    @Test
    public void should_format_constant(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d)));
        var r = formatDate.evaluate( new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("Aug 26, 2019, 8:22:10 AM"));
    }

    @Test
    public void should_format_constant_using_model_locale(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d)));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = formatDate.evaluate(model);

        assertThat(r, is("26 août 2019 à 08:22:10"));
    }

    @Test
    public void should_format_constant_with_pattern_short(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("SHORT")));
        var r = formatDate.evaluate( new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("Aug 26, 2019, 8:22 AM"));
    }

    @Test
    public void should_format_constant_using_model_locale_with_pattern_short(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("SHORT")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = formatDate.evaluate(model);

        assertThat(r, is("26 août 2019 08:22"));
    }

    @Test
    public void should_format_constant_with_pattern_medium(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("MEDIUM")));
        var r = formatDate.evaluate( new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("Aug 26, 2019, 8:22:10 AM"));
    }

    @Test
    public void should_format_constant_using_model_locale_with_pattern_medium(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("MEDIUM")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = formatDate.evaluate(model);

        assertThat(r, is("26 août 2019 à 08:22:10"));
    }

    @Test
    public void should_format_constant_with_pattern_long(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("LONG")));
        var r = formatDate.evaluate( new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("Aug 26, 2019, 8:22:10 AM CEST"));
    }

    @Test
    public void should_format_constant_using_model_locale_with_pattern_long(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("LONG")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = formatDate.evaluate(model);

        assertThat(r, is("26 août 2019 à 08:22:10 CEST"));
    }

    @Test
    public void should_format_constant_with_pattern(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("dd-MM-yyyy'T'HH:mm:ss")));
        var r = formatDate.evaluate( new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("26-08-2019T08:22:10"));
    }

    @Test
    public void should_format_constant_using_model_locale_with_pattern(){
        var d = Instant.parse("2019-08-26T06:22:10.533293Z");
        var formatDate = new FormatDate(Arrays.asList(new Constant(d),new Constant("dd-MMM-yyyy'T'HH:mm:ss")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = formatDate.evaluate(model);

        assertThat(r, is("26-août-2019T08:22:10"));
    }


    @Test
    public void should_accept_null(){
        var formatDate = new FormatDate(Arrays.asList(new Constant(null)));

        var r = formatDate.evaluate(null);

        assertThat(r, nullValue());
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var formatDate = new FormatDate(Arrays.asList(new Constant(37)));

        var json = new ObjectMapper().writeValueAsString(formatDate);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(formatDate, is(obj));
    }
}
