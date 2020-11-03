package nl.softcause.jsontemplates.expressions.arithmetic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class AbsTest {

    @Test
    public void should_make_value_absolute() {
        var abs = new Abs(Arrays.asList(new Constant(3)));

        var r = abs.evaluate(null);

        assertThat(r, is(3.0));
    }

    @Test
    public void should_make_value_absolute_negative_case() {
        var abs = new Abs(Arrays.asList(new Constant(-3)));

        var r = abs.evaluate(null);

        assertThat(r, is(3.0));
    }

    @Test
    public void should_consume_variables() {
        var abs = new Abs(Arrays.asList(new Variable("V")));

        var model = new TestModel().put("V", -3);

        var r = abs.evaluate(model);

        assertThat(r, is(3.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var abs = new Abs(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(abs);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(abs, is(obj));
    }
}
