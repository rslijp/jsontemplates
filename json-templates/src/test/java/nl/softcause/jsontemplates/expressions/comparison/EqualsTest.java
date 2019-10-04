package nl.softcause.jsontemplates.expressions.comparison;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EqualsTest {

    @Test
    public void should_return_true_for_same_string(){
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant("a"), new Constant("a")));

        var r = eq.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_false_for_different_string(){
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant("a"), new Constant("b")));

        var r = eq.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_true_for_same_number(){
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant(42), new Constant(42)));

        var r = eq.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_false_for_different_numbers(){
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant(42), new Constant(37)));

        var r = eq.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_true_for_same_object_reference(){
        var v = new Object();
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant(v), new Constant(v)));

        var r = eq.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_false_for_different_object_references(){
        var v = new Object();
        var v2 = new Object();
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Constant(v), new Constant(v2)));

        var r = eq.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_return_true_for_same_variables(){
        var model = new TestModel().put("V1",   "a").put("V2",   "a");
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Variable("V1"), new Variable("V2")));

        var r = eq.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_false_for_different_variables(){
        var model = new TestModel().put("V1",   "a").put("V2",   "b");
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Variable("V1"), new Variable("V2")));

        var r = eq.evaluate(model);

        assertThat(r, is(false));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var eq = new Equals();
        eq.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(eq);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(eq, is(obj));
    }
}
