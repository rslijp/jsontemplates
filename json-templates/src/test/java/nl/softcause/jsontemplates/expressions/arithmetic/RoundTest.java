package nl.softcause.jsontemplates.expressions.arithmetic;

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

public class RoundTest {

    @Test
    public void should_make_value_whole(){
        var round = new Round(Arrays.asList(new Constant(3.3)));

        var r = round.evaluate(null);

        assertThat(r, is(3.0));
    }

    @Test
    public void should_make_value_rounded_to_requested_digits(){
        var round = new Round(Arrays.asList(new Constant(4.323),new Constant(2)));

        var r = round.evaluate(null);

        assertThat(r, is(4.32));
    }

    @Test
    public void should_consume_variables(){
        var round = new Round(Arrays.asList(new Variable("V")));

        var model = new TestModel().put("V", -3.2);

        var r = round.evaluate(model);

        assertThat(r, is(-3.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var round = new Round(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(round);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(round, is(obj));
    }
}
