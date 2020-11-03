package nl.softcause.jsontemplates.expressions.text;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class ToLowerTest {

    @Test
    public void should_apply_to_lower_case_on_string_values() {
        var lower = new ToLower();
        lower.setArguments(Collections.singletonList(new Constant("ABC")));

        var r = lower.evaluate(new TestModel());

        assertThat(r, is("abc"));
    }

    @Test
    public void should_apply_to_lower_case_on_stringbuffers_values() {
        var lower = new ToLower();
        lower.setArguments(Collections.singletonList(new Constant(new StringBuffer().append("ABC"))));

        var r = lower.evaluate(new TestModel());

        assertThat(r, is("abc"));
    }

    @Test
    public void should_be_null_safe() {
        var lower = new ToLower();
        lower.setArguments(Collections.singletonList(new Constant(null)));

        var r = lower.evaluate(new TestModel());

        assertThat(r, nullValue());
    }


    @Test
    public void should_consume_variables() {
        var lower = new ToLower();
        lower.setArguments(Collections.singletonList(new Variable("V")));
        var model = new TestModel().put("V", "ABC");

        var r = lower.evaluate(model);

        assertThat(r, is("abc"));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var lower = new ToLower();
        lower.setArguments(Collections.singletonList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(lower);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(lower, is(obj));
    }
}
