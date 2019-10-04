package nl.softcause.jsontemplates.expressions.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrTest {

    @Test
    public void should_apply_boolean_or_to_values_t_t(){
        var or = new Or();
        or.setArguments(Arrays.asList(new Constant(true), new Constant(true)));

        var r = or.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_apply_boolean_or_to_values_t_f(){
        var or = new Or();
        or.setArguments(Arrays.asList(new Constant(true), new Constant(false)));

        var r = or.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_apply_boolean_or_to_values_f_t(){
        var or = new Or();
        or.setArguments(Arrays.asList(new Constant(false), new Constant(true)));

        var r = or.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_apply_boolean_or_to_values_f_f(){
        var or = new Or();
        or.setArguments(Arrays.asList(new Constant(false), new Constant(false)));

        var r = or.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_consume_variables(){
        var or = new Or();
        or.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", true).put("R",true);

        var r = or.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var or = new Or();
        or.setArguments(Arrays.asList(new Variable("V"), new Constant(true)));

        var json = new ObjectMapper().writeValueAsString(or);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(or, is(obj));
    }
}
