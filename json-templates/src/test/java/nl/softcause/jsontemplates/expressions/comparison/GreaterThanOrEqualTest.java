package nl.softcause.jsontemplates.expressions.comparison;

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

public class GreaterThanOrEqualTest {

    @Test
    public void should_return_true_for_4_being_greater_than_3() {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Constant(4), new Constant(3)));

        var r = gte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_3_being_equal_to_3() {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Constant(3), new Constant(3)));

        var r = gte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_2_being_less_than_3() {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Constant(2), new Constant(3)));

        var r = gte.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_auto_cast_values() {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Constant(30), new Constant(7.0)));

        var r = gte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_consume_variables() {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 8).put("R", 7);

        var r = gte.evaluate(model);

        assertThat(r, is(true));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var gte = new GreaterThanOrEqual();
        gte.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(gte);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(gte, is(obj));
    }
}
