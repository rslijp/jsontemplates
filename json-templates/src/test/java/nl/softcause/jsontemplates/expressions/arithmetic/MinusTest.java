package nl.softcause.jsontemplates.expressions.arithmetic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class MinusTest {

    @Test
    public void should_substract_values() {
        var minus = new Minus();
        minus.setArguments(Arrays.asList(new Constant(17), new Constant(7)));

        var r = minus.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_auto_cast_values() {
        var minus = new Minus();
        minus.setArguments(Arrays.asList(new Constant(17), new Constant(7.0)));

        var r = minus.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_consume_variables() {
        var minus = new Minus();
        minus.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 17).put("R", 7);

        var r = minus.evaluate(model);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var minus = new Minus();
        minus.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(minus);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(minus, is(obj));
    }
}
