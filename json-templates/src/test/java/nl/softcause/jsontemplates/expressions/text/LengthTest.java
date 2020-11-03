package nl.softcause.jsontemplates.expressions.text;

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

public class LengthTest {

    @Test
    public void should_get_length_of_string_values() {
        var length = new Length(Arrays.asList(new Constant("ABC")));

        var r = length.evaluate(new TestModel());

        assertThat(r, is(3));
    }

    @Test
    public void should_get_length_of_stringbuffer_values() {
        var length = new Length(Arrays.asList(new Constant(new StringBuffer().append("ABC"))));

        var r = length.evaluate(new TestModel());

        assertThat(r, is(3));
    }

    @Test
    public void should_be_null_safe() {
        var length = new Length(Arrays.asList(new Constant(null)));

        var r = length.evaluate(new TestModel());

        assertThat(r, is(0));
    }


    @Test
    public void should_consume_variables() {
        var length = new Length(Arrays.asList(new Variable("V")));
        var model = new TestModel().put("V", "ABC");

        var r = length.evaluate(model);

        assertThat(r, is(3));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var length = new Length(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(length);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(length, is(obj));
    }
}
