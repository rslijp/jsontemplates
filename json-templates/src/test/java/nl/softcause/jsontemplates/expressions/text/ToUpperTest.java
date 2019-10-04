package nl.softcause.jsontemplates.expressions.text;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ToUpperTest {

    @Test
    public void should_apply_to_upper_case_on_string_values(){
        var upper = new ToUpper();
        upper.setArguments(Collections.singletonList(new Constant("abc")));

        var r = upper.evaluate(new TestModel());

        assertThat(r, is("ABC"));
    }

    @Test
    public void should_apply_to_upper_case_stringbuffers_values(){
        var upper = new ToUpper();
        upper.setArguments(Collections.singletonList(new Constant(new StringBuffer().append("abc"))));

        var r = upper.evaluate(new TestModel());

        assertThat(r, is("ABC"));
    }

    @Test
    public void should_be_null_safe(){
        var upper = new ToUpper();
        upper.setArguments(Collections.singletonList(new Constant(null)));

        var r = upper.evaluate(new TestModel());

        assertThat(r, nullValue());
    }


    @Test
    public void should_consume_variables(){
        var upper = new ToUpper();
        upper.setArguments(Collections.singletonList(new Variable("V")));
        var model = new TestModel().put("V", "abc");

        var r = upper.evaluate(model);

        assertThat(r, is("ABC"));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var upper = new ToUpper();
        upper.setArguments(Collections.singletonList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(upper);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(upper, is(obj));
    }
}
