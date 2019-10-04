package nl.softcause.jsontemplates.expressions.comparison;

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

public class LessThanOrEqualTest {

    @Test
    public void should_return_true_for_2_being_less_than_3(){
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Constant(2), new Constant(3)));

        var r = lte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_3_being_equal_to_3(){
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Constant(3), new Constant(3)));

        var r = lte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_4_being_greater_than_3(){
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Constant(4), new Constant(3)));

        var r = lte.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_auto_cast_values(){
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Constant(6), new Constant(7.0)));

        var r = lte.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_consume_variables(){
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 6).put("R",7);

        var r = lte.evaluate(model);

        assertThat(r, is(true));
    }



    @Test
    public void should_serialize_to_json() throws IOException {
        var lte = new LessThanOrEqual();
        lte.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(lte);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(lte, is(obj));
    }
}
