package nl.softcause.jsontemplates.expressions.logic;

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

public class AndTest {

    @Test
    public void should_apply_boolean_and_to_values_t_t() {
        var and = new And();
        and.setArguments(Arrays.asList(new Constant(true), new Constant(true)));

        var r = and.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_apply_boolean_and_to_values_t_f() {
        var and = new And();
        and.setArguments(Arrays.asList(new Constant(true), new Constant(false)));

        var r = and.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_apply_boolean_and_to_values_f_t() {
        var and = new And();
        and.setArguments(Arrays.asList(new Constant(false), new Constant(true)));

        var r = and.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_apply_boolean_and_to_values_f_f() {
        var and = new And();
        and.setArguments(Arrays.asList(new Constant(false), new Constant(false)));

        var r = and.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_consume_variables() {
        var and = new And();
        and.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", true).put("R", true);

        var r = and.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var and = new And();
        and.setArguments(Arrays.asList(new Variable("V"), new Constant(true)));

        var json = new ObjectMapper().writeValueAsString(and);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(and, is(obj));
    }
}
