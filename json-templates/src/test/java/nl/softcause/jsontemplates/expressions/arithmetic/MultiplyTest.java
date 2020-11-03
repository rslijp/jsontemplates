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

public class MultiplyTest {

    @Test
    public void should_substract_values() {
        var multiply = new Multiply();
        multiply.setArguments(Arrays.asList(new Constant(2), new Constant(5)));

        var r = multiply.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_auto_cast_values() {
        var multiply = new Multiply();
        multiply.setArguments(Arrays.asList(new Constant(2), new Constant(5.0)));

        var r = multiply.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_consume_variables() {
        var multiply = new Multiply();
        multiply.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 2).put("R", 5);

        var r = multiply.evaluate(model);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var multiply = new Multiply();
        multiply.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(multiply);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(multiply, is(obj));
    }
}
