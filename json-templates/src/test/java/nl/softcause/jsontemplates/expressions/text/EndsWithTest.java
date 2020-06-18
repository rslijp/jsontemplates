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

public class EndsWithTest {

    @Test
    public void should_return_true_for_string_abc_ending_with_string_c() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("c")));

        var r = endsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_ending_with_string_c() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")), new Constant("c")));

        var r = endsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_ending_with_stringbuffer_c() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")),
                new Constant(new StringBuffer().append("c"))));

        var r = endsWith.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_string_abc_ending_with_stringbuffer_c() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant(new StringBuffer().append("c"))));

        var r = endsWith.evaluate(null);

        assertThat(r, is(true));
    }


    @Test
    public void should_return_false_for_string_abc_not_containing_string_d() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("d")));

        var r = endsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_false_for_string_abc_not_ending_with_string_b() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant("abc"), new Constant("b")));

        var r = endsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_be_null_safe_1st() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant(null), new Constant("a")));

        var r = endsWith.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_no_be_null_safe_2nd() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Constant("a"), new Constant(null)));

        try {
            var r = endsWith.evaluate(null);
            fail();
        } catch (TypeException Te) {
            assertThat(Te.getMessage(), is(TypeException.invalidCast(null, Types.TEXT).getMessage()));
        }

    }

    @Test
    public void should_consume_variables() {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", "abc").put("R", "c");

        var r = endsWith.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var endsWith = new EndsWith();
        endsWith.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(endsWith);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(endsWith, is(obj));
    }
}
