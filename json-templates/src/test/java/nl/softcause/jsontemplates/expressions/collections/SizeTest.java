package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class SizeTest {
    @Test
    public void should_get_size_of_array_value() {
        var size = new Size(Arrays.asList(new Constant(new int[] {1, 2, 3})));

        var r = size.evaluate(new TestModel());

        assertThat(r, is(3));
    }

    @Test
    public void should_get_size_of_list_value() {
        var size = new Size(Arrays.asList(new Constant(new IntegerList(1, 2, 3))));

        var r = size.evaluate(new TestModel());

        assertThat(r, is(3));
    }

    @Test
    public void should_be_null_safe() {
        var size = new Size(Arrays.asList(new Constant(null)));

        var r = size.evaluate(new TestModel());

        assertThat(r, is(0));
    }


    @Test
    public void should_consume_variables() {
        var size = new Size(Arrays.asList(new Variable("V")));
        var model = new TestModel().put("V", new IntegerList(1, 2, 3));

        var r = size.evaluate(model);

        assertThat(r, is(3));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var size = new Size(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(size);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(size, is(obj));
    }
}
