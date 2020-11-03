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

public class AddTest {

    @Test
    public void should_add_to_values() {
        var add = new Add();
        add.setArguments(Arrays.asList(new Constant(3), new Constant(7)));

        var r = add.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_auto_cast_values() {
        var add = new Add();
        add.setArguments(Arrays.asList(new Constant(3), new Constant(7.0)));

        var r = add.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_consume_variables() {
        var add = new Add();
        add.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 3).put("R", 7);

        var r = add.evaluate(model);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var add = new Add();
        add.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(add);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(add, is(obj));
    }
}
