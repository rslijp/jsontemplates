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

public class DivideTest {

    @Test
    public void should_divide_to_values(){
        var divide = new Divide();
        divide.setArguments(Arrays.asList(new Constant(70), new Constant(7)));

        var r = divide.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_auto_cast_values(){
        var divide = new Divide();
        divide.setArguments(Arrays.asList(new Constant(70), new Constant(7.0)));

        var r = divide.evaluate(null);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_consume_variables(){
        var divide = new Divide();
        divide.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 70).put("R",7);

        var r = divide.evaluate(model);

        assertThat(r, is(10.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var divide = new Divide();
        divide.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(divide);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(divide, is(obj));
    }
}
