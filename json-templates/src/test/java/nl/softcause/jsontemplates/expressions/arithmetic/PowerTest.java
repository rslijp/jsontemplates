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

public class PowerTest {

    @Test
    public void should_power_to_values(){
        var power = new Power();
        power.setArguments(Arrays.asList(new Constant(3), new Constant(7)));

        var r = power.evaluate(null);

        assertThat(r, is(2187.0));
    }

    @Test
    public void should_auto_cast_values(){
        var power = new Power();
        power.setArguments(Arrays.asList(new Constant(3), new Constant(7.0)));

        var r = power.evaluate(null);

        assertThat(r, is(2187.0));
    }

    @Test
    public void should_consume_variables(){
        var power = new Power();
        power.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 3).put("R",7);

        var r = power.evaluate(model);

        assertThat(r, is(2187.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var power = new Power();
        power.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(power);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(power, is(obj));
    }
}
