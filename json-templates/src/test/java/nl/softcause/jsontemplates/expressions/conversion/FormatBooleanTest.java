package nl.softcause.jsontemplates.expressions.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import org.junit.Test;

public class FormatBooleanTest {

    @Test
    public void should_format_constant_true() {
        var formatBoolean = new FormatBoolean(Arrays.asList(new Constant(true)));

        var r = formatBoolean.evaluate(null);

        assertThat(r, is("Y"));
    }

    @Test
    public void should_format_constant_false() {
        var formatBoolean = new FormatBoolean(Arrays.asList(new Constant(false)));

        var r = formatBoolean.evaluate(null);

        assertThat(r, is("N"));
    }

    @Test
    public void should_accept_null() {
        var formatBoolean = new FormatBoolean(Arrays.asList(new Constant(null)));

        var r = formatBoolean.evaluate(null);

        assertThat(r, nullValue());
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var formatBoolean = new FormatBoolean(Arrays.asList(new Constant(37)));

        var json = new ObjectMapper().writeValueAsString(formatBoolean);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(formatBoolean, is(obj));
    }
}
