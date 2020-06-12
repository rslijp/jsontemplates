package nl.softcause.jsontemplates.expressions.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import org.junit.Test;

public class FormatDoubleTest {

    @Test
    public void should_format_constant() {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(37)));
        var r = formatNumber.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is("37"));
    }

    @Test
    public void should_format_constant_fraction() {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(12337.324)));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.ENGLISH);
        var r = formatNumber.evaluate(model);

        assertThat(r, is("12,337.324"));
    }

    @Test
    public void should_format_constant_fraction_using_model_locale() {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(12337.324)));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.GERMANY);
        var r = formatNumber.evaluate(model);

        assertThat(r, is("12.337,324"));
    }


    @Test
    public void should_format_big_integer() {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(371312321321312L)));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.ENGLISH);

        var r = formatNumber.evaluate(model);

        assertThat(r, is("371,312,321,321,312"));
    }

    @Test
    public void should_accept_null() {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(null)));

        var r = formatNumber.evaluate(null);

        assertThat(r, nullValue());
    }

    @Test
    public void should_format_variable() {
        var td = new TestDefinition();
        td.setAge(37);
        var formatNumber = new FormatDouble(Arrays.asList(new Variable("age")));

        var r = formatNumber.evaluate(new TemplateModel<>(new DefinedModel<>(td)));

        assertThat(r, is("37"));
    }

    @Test
    public void should_format_optional_number() {
        var td = new TestDefinition();
        td.setMentalAge(37);
        var formatNumber = new FormatDouble(Arrays.asList(new Variable("mentalAge")));

        var r = formatNumber.evaluate(new TemplateModel<>(new DefinedModel<>(td)));

        assertThat(r, is("37"));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var formatNumber = new FormatDouble(Arrays.asList(new Constant(37)));

        var json = new ObjectMapper().writeValueAsString(formatNumber);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(formatNumber, is(obj));
    }
}
