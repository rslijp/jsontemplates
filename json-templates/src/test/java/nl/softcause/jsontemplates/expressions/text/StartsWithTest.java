package nl.softcause.jsontemplates.expressions.text;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class StartsWithTest {

    @Test
    public void should_return_true_for_string_abc_starting_with_string_a() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("a")));

        var r = startsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_starting_with_string_a() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")), new Constant("a")));

        var r = startsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_starting_with_stringbuffer_a() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")),
                new Constant(new StringBuffer().append("a"))));

        var r = startsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_string_abc_starting_with_stringbuffer_a() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant(new StringBuffer().append("a"))));

        var r = startsWith.evaluate(null);

        assertThat(r, is(true));
    }


    @Test
    public void should_return_false_for_string_abc_not_containing_string_d() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("d")));

        var r = startsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_false_for_string_abc_not_starting_with_string_b() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("b")));

        var r = startsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_be_null_safe_1st() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant(null), new Constant("a")));

        var r = startsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_no_be_null_safe_2nd() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Constant("a"), new Constant(null)));

        try {
            var r = startsWith.evaluate(null);
            fail();
        } catch (TypeException Te) {
            assertThat(Te.getMessage(), is(TypeException.invalidCast(null, Types.TEXT).getMessage()));
        }

    }

    @Test
    public void should_consume_variables() {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", "abc").put("R", "a");

        var r = startsWith.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var startsWith = new StartsWith();
        startsWith.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(startsWith);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(startsWith, is(obj));
    }
}
