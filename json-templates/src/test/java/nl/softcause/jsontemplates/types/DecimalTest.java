package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.collections.DoubleList;
import nl.softcause.jsontemplates.collections.DoubleMap;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DecimalTest {

    @Test
    public void should_detect_long_as_decimal(){
        assertThat(Types.DECIMAL.isA(1L), is(true));
    }

    @Test
    public void should_detect_int_as_decimal(){
        assertThat(Types.DECIMAL.isA(1), is(true));
    }

    @Test
    public void should_detect_float_as_decimal(){
        assertThat(Types.DECIMAL.isA(1.0f), is(true));
    }

    @Test
    public void should_detect_double_as_decimal(){
        assertThat(Types.DECIMAL.isA(1.0), is(true));
    }

    @Test
    public void should_reject_other(){
        assertThat(Types.DECIMAL.isA(BigInteger.valueOf(1L)), is(false));
        assertThat(Types.DECIMAL.isA(BigDecimal.valueOf(1L)), is(false));
        assertThat(Types.DECIMAL.isA("1"), is(false));
        assertThat(Types.DECIMAL.isA(null), is(false));
    }


    @Test
    public void should_accept_null_when_nullable(){
        assertThat(Types.OPTIONAL_DECIMAL.isA(1), is(true));
        assertThat(Types.OPTIONAL_DECIMAL.isA(null), is(true));
    }

    @Test
    public void should_accept_list_and_array_when_list(){
        assertThat(Types.LIST_DECIMAL.isA(new double[]{1.0}), is(true));
        assertThat(Types.LIST_DECIMAL.isA(new DoubleList(1.0)), is(true));
        assertThat(Types.LIST_DECIMAL.isA(new int[]{1}), is(true));
        assertThat(Types.LIST_DECIMAL.isA(new IntegerList(1)), is(true));
        assertThat(Types.LIST_DECIMAL.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map(){
        assertThat(Types.MAP_DECIMAL.isA(new DoubleMap()), is(true));
        assertThat(Types.MAP_DECIMAL.isA(new IntegerMap()), is(true));
    }

    @Test
    public void should_convert_long_to_double(){
        assertThat(Types.DECIMAL.convert(1L), is(1.0));
    }

    @Test
    public void should_convert_int_to_double(){
        assertThat(Types.DECIMAL.convert(1), is(1.0));
    }

    @Test
    public void should_convert_float_to_double(){
        assertThat(Types.DECIMAL.convert(1.0f), is(1.0));
    }

    @Test
    public void should_convert_double_to_double(){
        assertThat(Types.DECIMAL.convert(1.0), is(1.0));
    }


    @Test
    public void should_convert_nullables_to_double(){
        assertThat(Types.OPTIONAL_DECIMAL.convert(null), nullValue());
        assertThat(Types.OPTIONAL_DECIMAL.convert(1), is(1.0));
    }

    @Test
    public void should_convert_list_and_array_to_list_of_double(){
        assertThat(Types.LIST_DECIMAL.convert(null), nullValue());
        assertThat(Types.LIST_DECIMAL.convert(new double[]{1}), is(Collections.singletonList(1.0)));
        assertThat(Types.LIST_DECIMAL.convert(new IntegerList(1)), is(Collections.singletonList(1.0)));
        assertThat(Types.LIST_DECIMAL.convert(new double[]{1.0}), is(Collections.singletonList(1.0)));
        assertThat(Types.LIST_DECIMAL.convert(new DoubleList(1.0)), is(Collections.singletonList(1.0)));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.DECIMAL);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.DECIMAL));

    }

    @Test
    public void should_serialize_to_same_instance_as_optional() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OPTIONAL_DECIMAL);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OPTIONAL_DECIMAL));

    }

    @Test
    public void should_serialize_to_same_instance_as_list() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.LIST_DECIMAL);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.LIST_DECIMAL));
    }


    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_DECIMAL);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_DECIMAL));
    }
}
