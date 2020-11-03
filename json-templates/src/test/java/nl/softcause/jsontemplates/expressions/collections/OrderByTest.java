package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import lombok.Value;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class OrderByTest {

    public TestSubject<Integer> i(int value) {
        return new TestSubject<>(value);
    }

    public TestSubject<String> s(String value) {
        return new TestSubject<>(value);
    }

    @Test
    public void should_apply_order_array_value() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {i(1), i(4), i(5), i(6), i(2), i(3)}),
                new Constant("value")
        ));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(i(1), i(2), i(3), i(4), i(5), i(6))));
    }

    @Test
    public void should_apply_order_array_objects() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {s("a"), s("c"), s("b")}),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(s("a"), s("b"), s("c"))));
    }

    @Test
    public void should_apply_order_array_with_null_object() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {s("a"), s("c"), null, s("b")}),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(s("a"), s("b"), s("c"), null)));
    }

    @Test
    public void should_apply_order_array_with_multiple_null_object() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {s("a"), null, s("c"), null, s("b")}),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(s("a"), s("b"), s("c"), null, null)));
    }

    @Test
    public void should_apply_order_array_with_null_value() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {s("a"), s("c"), s(null), s("b")}),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(s("a"), s("b"), s("c"), s(null))));
    }

    @Test
    public void should_apply_order_array_with_multiple_null_value() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(new Object[] {s("a"), s(null), s("c"), s(null), s("b")}),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(s("a"), s("b"), s("c"), s(null), s(null))));
    }

    @Test
    public void should_be_null_safe_1st() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(
                new Constant(null),
                new Constant("value")));

        var r = order.evaluate(null);

        assertThat(r, nullValue());
    }

    @Test
    public void should_consume_variables() {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(new Variable("V"), new Constant("value")));
        var model = new TestModel().put("V", new Object[] {i(1), i(4), i(5), i(6), i(2), i(3)});

        var r = order.evaluate(model);

        assertThat(r, is(Arrays.asList(i(1), i(2), i(3), i(4), i(5), i(6))));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var order = new OrderBy();
        order.setArguments(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(order);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(order, is(obj));
    }

    @Value
    public class TestSubject<T> {
        T value;
    }
}
