package nl.softcause.jsontemplates.expressions.arithmetic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class RandomTest {

    @Test
    public void should_generate_random_value() {
        var random = new Random(Collections.emptyList());

        var r = (double) random.evaluate(null);

        assertThat(r >= 0.0, is(true));
        assertThat(r < 1.0, is(true));

    }

    @Test
    public void should_generate_random_value_with_request_range() {
        var random = new Random(Arrays.asList(new Constant(10)));

        var r = (double) random.evaluate(null);

        assertThat(r >= 0.0, is(true));
        assertThat(r < 10.0, is(true));
    }

    @Test
    public void should_consume_variables() {
        var random = new Random(Arrays.asList(new Variable("V")));

        var model = new TestModel().put("V", -10);

        var r = (double) random.evaluate(model);

        assertThat(r <= 0.0, is(true));
        assertThat(r > -10.0, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var random = new Random(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(random);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(random, is(obj));
    }
}
