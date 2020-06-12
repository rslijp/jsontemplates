package nl.softcause.jsontemplates.expressions.conversion;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class ParseBooleanTest {

    @Test
    public void should_parse_constant() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("true")));

        var r = parseBoolean.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_accept_null() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant(null)));

        var r = parseBoolean.evaluate(null);

        assertThat(r, nullValue());
    }

    @Test
    public void should_parse_constant_case_insensitive() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("trUe")));

        var r = parseBoolean.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_parse_Y_constant_as_true() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("Y")));

        var r = parseBoolean.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_parse_N_constant_as_false() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("N")));

        var r = parseBoolean.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_parse_false_constant_as_false() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("false")));

        var r = parseBoolean.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_parse_variable() {
        var td = new TestDefinition();
        td.setName("true");
        var parseBoolean = new ParseBoolean(Arrays.asList(new Variable("name")));

        var r = parseBoolean.evaluate(new DefinedModel<>(td));

        assertThat(r, is(true));
    }

    @Test
    public void should_report_conversion_error() {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("aap")));
        try {
            var r = parseBoolean.evaluate(null);
            fail();
        } catch (TypeException Te) {
            assertThat(Te.getMessage(), is(TypeException.conversionError("aap", Types.OPTIONAL_BOOLEAN).getMessage()));
        }
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var parseBoolean = new ParseBoolean(Arrays.asList(new Constant("37")));

        var json = new ObjectMapper().writeValueAsString(parseBoolean);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(parseBoolean, is(obj));
    }
}
