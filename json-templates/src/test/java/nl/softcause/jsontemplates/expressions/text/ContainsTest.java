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

public class ContainsTest {

    @Test
    public void should_return_true_for_string_abc_containing_string_b() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant("abc"), new Constant("b")));

        var r = contains.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_containing_string_b() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")), new Constant("b")));

        var r = contains.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_stringbuffer_abc_containing_stringbuffer_b() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant(new StringBuffer().append("abc")),
                new Constant(new StringBuffer().append("b"))));

        var r = contains.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_string_abc_containing_stringbuffer_b() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant("abc"), new Constant(new StringBuffer().append("b"))));

        var r = contains.evaluate(null);

        assertThat(r, is(true));
    }


    @Test
    public void should_return_false_for_string_abc_not_containing_string_d() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant("abc"), new Constant("d")));

        var r = contains.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_be_null_safe_1st() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant(null), new Constant("a")));

        var r = contains.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_no_be_null_safe_2nd() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Constant("a"), new Constant(null)));

        try {
            var r = contains.evaluate(null);
            fail();
        } catch (TypeException Te) {
            assertThat(Te.getMessage(), is(TypeException.invalidCast(null, Types.TEXT).getMessage()));
        }

    }

    @Test
    public void should_consume_variables() {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", "abc").put("R", "b");

        var r = contains.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var contains = new Contains();
        contains.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(contains);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(contains, is(obj));
    }
}
