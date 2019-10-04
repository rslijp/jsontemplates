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

public class TernaryTest {

    @Test
    public void should_apply_and_return_true_argument_when_condition_is_true(){
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(new Constant(true), new Constant(1L),new Constant(2L)));

        var r = ternary.evaluate(null);

        assertThat(r, is(1L));
    }

    @Test
    public void should_apply_and_return_false_argument_when_condition_is_false(){
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(new Constant(false), new Constant(1L),new Constant(2L)));

        var r = ternary.evaluate(null);

        assertThat(r, is(2L));
    }



    @Test
    public void should_consume_variables(){
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(new Variable("C"), new Variable("T"),new Variable("F")));
        var model = new TestModel().put("C", true).put("T",true).put("F",false);

        var r = ternary.evaluate(model);

        assertThat(r, is(true));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(new Constant(true), new Constant(1),new Constant(2)));


        var json = new ObjectMapper().writeValueAsString(ternary);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(ternary, is(obj));
    }
}
