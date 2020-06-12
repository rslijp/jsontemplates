package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class AppendTest {

    @Test
    public void should_apply_append_array_value() {
        var append = new Append();
        append.setArguments(Arrays.asList(
                new Constant(new int[] {1, 2, 3}), new Constant(new int[] {4, 5, 6})
        ));

        var r = append.evaluate(null);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L)));
    }

    @Test
    public void should_apply_append_list_values() {
        var append = new Append();
        append.setArguments(Arrays.asList(
                new Constant(new IntegerList(1, 2, 3)),
                new Constant(new IntegerList(4, 5, 6))
        ));

        var r = append.evaluate(null);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L)));
    }

    @Test
    public void should_be_null_safe_1st() {
        var append = new Append();
        append.setArguments(Arrays.asList(new Constant(null), new Constant(new int[] {1, 2, 3})));

        var r = append.evaluate(null);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void should_be_null_safe_2nd() {
        var append = new Append();
        append.setArguments(Arrays.asList(new Constant(new int[] {1, 2, 3}), new Constant(null)));

        var r = append.evaluate(null);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void should_consume_variables() {
        var append = new Append();
        append.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", new int[] {1, 2, 3}).put("R", new int[] {4, 5, 6});

        var r = append.evaluate(model);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L)));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var append = new Append();
        append.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(append);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(append, is(obj));
    }
}
