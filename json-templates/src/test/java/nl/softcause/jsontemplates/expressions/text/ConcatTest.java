package nl.softcause.jsontemplates.expressions.text;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.logic.And;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConcatTest {

    @Test
    public void should_apply_concat_string_values(){
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Constant("a"), new Constant("b")));

        var r = concat.evaluate(null);

        assertThat(r, is("ab"));
    }

    @Test
    public void should_apply_concat_stringbuffers_values(){
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Constant(new StringBuffer().append("a")),new Constant(new StringBuffer().append("b"))));

        var r = concat.evaluate(null);

        assertThat(r, is("ab"));
    }

    @Test
    public void should_be_null_safe_1st(){
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Constant(null), new Constant("a")));

        var r = concat.evaluate(null);

        assertThat(r, is("a"));
    }

    @Test
    public void should_be_null_safe_2nd(){
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Constant("a"), new Constant(null)));

        var r = concat.evaluate(null);

        assertThat(r, is("a"));
    }

    @Test
    public void should_consume_variables(){
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", "a").put("R","b");

        var r = concat.evaluate(model);

        assertThat(r, is("ab"));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var concat = new Concat();
        concat.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(concat);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(concat, is(obj));
    }
}
