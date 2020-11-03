package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class TailTest {

    @Test
    public void should_return_tail_elements() {
        var tail = new Tail(Arrays.asList(new Constant(new int[] {1, 2, 3})));

        var r = tail.evaluate(null);

        assertThat(r, is(Arrays.asList(2L, 3L)));
    }

    @Test
    public void should_return_tail_elements_from_list() {
        var tail = new Tail(Arrays.asList(new Constant(new IntegerList(1, 2, 3))));

        var r = tail.evaluate(null);

        assertThat(r, is(Arrays.asList(2L, 3L)));
    }

    @Test
    public void should_return_emptylist_on_empty_list() {
        var tail = new Tail(Arrays.asList(new Constant(new int[] {})));

        var r = tail.evaluate(null);

        assertThat(r, is(new ArrayList()));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var tail = new Tail(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(tail);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(tail, is(obj));
    }
}
