package nl.softcause.jsontemplates.expressions.conversion;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class ParseIntegerTest {

    @Test
    public void should_parse_constant() {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant("37")));

        var r = parseNumber.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));

        assertThat(r, is(37L));
    }

    @Test
    public void should_parse_bignumber() {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant("371,312")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.ENGLISH);

        var r = parseNumber.evaluate(model);

        assertThat(r, is(371312L));
    }


    @Test
    public void should_parse_bignumber_using_locale() {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant("371,312")));
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        model.setLocale(Locale.FRANCE);

        var r = parseNumber.evaluate(model);

        assertThat(r, is(371L));
    }


    @Test
    public void should_accept_null() {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant(null)));

        var r = parseNumber.evaluate(null);

        assertThat(r, nullValue());
    }

    @Test
    public void should_parse_variable() {
        var td = new TestDefinition();
        td.setName("37");
        var parseNumber = new ParseInteger(Arrays.asList(new Variable("name")));

        var r = parseNumber.evaluate(new TemplateModel<>(new DefinedModel<>(td)));

        assertThat(r, is(37L));
    }

    @Test
    public void should_report_conversion_error() {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant("aap")));
        try {
            var r = parseNumber.evaluate(new TemplateModel<>(new DefinedModel<>(TestDefinition.class)));
            fail();
        } catch (TypeException Te) {
            assertThat(Te.getMessage(), is(TypeException.conversionError("aap", Types.OPTIONAL_INTEGER).getMessage()));
        }
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var parseNumber = new ParseInteger(Arrays.asList(new Constant("37")));

        var json = new ObjectMapper().writeValueAsString(parseNumber);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(parseNumber, is(obj));
    }
}
