package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class IsEmptyTest {

    @Test
    public void should_return_false_for_non_empty_array() {
        var isEmpty = new IsEmpty(Arrays.asList(new Constant(new int[] {1, 2, 3})));

        var r = isEmpty.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_false_for_non_empty_list() {
        var isEmpty = new IsEmpty(Arrays.asList(new Constant(new IntegerList(1, 2, 3))));

        var r = isEmpty.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_true_on_empty_array() {
        var isEmpty = new IsEmpty(Arrays.asList(new Constant(new int[] {})));

        var r = isEmpty.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_on_empty_list() {
        var isEmpty = new IsEmpty(Arrays.asList(new Constant(new IntegerList())));

        var r = isEmpty.evaluate(null);

        assertThat(r, is(true));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var isEmpty = new IsEmpty(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(isEmpty);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(isEmpty, is(obj));
    }
}
