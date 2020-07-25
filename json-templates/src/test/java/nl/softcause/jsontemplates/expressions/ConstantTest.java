package nl.softcause.jsontemplates.expressions;

import static nl.softcause.jsontemplates.types.Types.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class ConstantTest {

    @Test
    public void should_return_value() {
        var c = new Constant("42");

        var r = c.evaluate(null);

        assertThat(r, is("42"));
    }

    @Test
    public void should_handle_quotes() {
        var c = new Constant("Quote hasn\'t got a value");

        var r = c.evaluate(null);

        assertThat(r, is("Quote hasn\'t got a value"));
    }

    @Test
    public void should_return_value_of_all_types() {
        assertThat(new Constant(true).evaluate(null), is(true));
        assertThat(new Constant(this).evaluate(null), is(this));
        assertThat(new Constant("42").evaluate(null), is("42"));
        assertThat(new Constant(42).evaluate(null), is(42L));
        assertThat(new Constant(42.0).evaluate(null), is(42.0));
        assertThat(new Constant(42L).evaluate(null), is(42L));
        assertThat(new Constant(42.0f).evaluate(null), is(42.0));
    }

    @Test
    public void should_guess_return_type() {
        assertThat(new Constant(true).getReturnType(null), is(BOOLEAN));
        assertThat(new Constant(this).getReturnType(null), is(OBJECT));
        assertThat(new Constant("42").getReturnType(null), is(TEXT));
        assertThat(new Constant(42).getReturnType(null), is(INTEGER));
        assertThat(new Constant(42.0).getReturnType(null), is(DECIMAL));
        assertThat(new Constant(42L).getReturnType(null), is(INTEGER));
        assertThat(new Constant(42.0f).getReturnType(null), is(DECIMAL));
    }

    @Test
    public void should_serialize_to_equal_object() throws IOException {
        var c = new Constant(42);

        var json = new ObjectMapper()
                .writeValueAsString(c);
        var obj = (Constant) new ObjectMapper().readValue(json, IExpression.class);

        Assert.assertThat(obj, is(c));
        Assert.assertThat(obj, isA(Constant.class));
        Assert.assertThat(obj.getValue(), is(42));
    }
}
