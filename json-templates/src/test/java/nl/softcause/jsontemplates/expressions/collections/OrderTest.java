package nl.softcause.jsontemplates.expressions.collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderTest {

    @Test
    public void should_apply_order_array_value(){
        var order = new Order(Arrays.asList(
                new Constant(new int[]{1,4,5,6,2,3})));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList(1L,2L,3L,4L,5L,6L)));
    }

    @Test
    public void should_apply_order_array_objects(){
        var order = new Order(Arrays.asList(
                new Constant(new String[]{"a","c","b"})));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList("a","b","c")));
    }


    @Test
    public void should_apply_order_array_with_null_value(){
        var order = new Order(Arrays.asList(
                new Constant(new String[]{"a","c",null,"b"})));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList("a","b","c",null)));
    }

    @Test
    public void should_apply_order_array_with_multiple_null_value(){
        var order = new Order(Arrays.asList(
                new Constant(new String[]{"a",null,"c",null,"b"})));

        var r = order.evaluate(null);

        assertThat(r, is(Arrays.asList("a","b","c",null,null)));
    }


    @Test
    public void should_be_null_safe_1st(){
        var order = new Order(Arrays.asList(
                new Constant(null)));

        var r = order.evaluate(null);

        assertThat(r, nullValue());
    }


    @Test
    public void should_consume_variables(){
        var order = new Order(Arrays.asList(new Variable("V")));
        var model = new TestModel().put("V", new IntegerList(1, 4, 5, 6, 2, 3));

        var r = order.evaluate(model);

        assertThat(r, is(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L)));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var order = new Order(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(order);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(order, is(obj));
    }
}
