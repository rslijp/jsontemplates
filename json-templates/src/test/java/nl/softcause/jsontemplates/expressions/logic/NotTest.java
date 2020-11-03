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

public class NotTest {

    @Test
    public void should_apply_boolean_not_to_values_t() {
        var not = new Not(Arrays.asList(new Constant(true)));

        var r = not.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_apply_boolean_not_to_values_f() {
        var not = new Not(Arrays.asList(new Constant(true)));

        var r = not.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_consume_variables() {
        var not = new Not(Arrays.asList(new Variable("V")));
        var model = new TestModel().put("V", true);

        var r = not.evaluate(model);

        assertThat(r, is(false));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var not = new Not(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(not);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(not, is(obj));
    }
}
