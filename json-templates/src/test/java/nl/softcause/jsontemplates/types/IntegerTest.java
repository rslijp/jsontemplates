package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.collections.DoubleMap;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import nl.softcause.jsontemplates.collections.LongList;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IntegerTest {

    @Test
    public void should_detect_long_as_number(){
        assertThat(Types.INTEGER.isA(1L), is(true));
    }

    @Test
    public void should_detect_int_as_number(){
        assertThat(Types.INTEGER.isA(1), is(true));
    }

    @Test
    public void should_reject_other(){
        assertThat(Types.INTEGER.isA(1.0), is(false));
        assertThat(Types.INTEGER.isA(BigInteger.valueOf(1L)), is(false));
        assertThat(Types.INTEGER.isA(1.0f), is(false));
        assertThat(Types.INTEGER.isA("1"), is(false));
        assertThat(Types.INTEGER.isA(null), is(false));

    }

    @Test
    public void should_accept_null_when_nullable(){
        assertThat(Types.OPTIONAL_INTEGER.isA(1), is(true));
        assertThat(Types.OPTIONAL_INTEGER.isA(null), is(true));
    }

    @Test
    public void should_accept_list_and_array_as_list(){
        assertThat(Types.LIST_INTEGER.isA(new int[]{1}), is(true));
        assertThat(Types.LIST_INTEGER.isA(new IntegerList(1)), is(true));
        assertThat(Types.LIST_INTEGER.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map(){
        assertThat(Types.MAP_INTEGER.isA(new DoubleMap()), is(false));
        assertThat(Types.MAP_INTEGER.isA(new IntegerMap()), is(true));
    }

    @Test
    public void should_convert_long_to_long(){
        assertThat(Types.INTEGER.convert(1L), is(1L));
    }

    @Test
    public void should_convert_int_to_long(){
        assertThat(Types.INTEGER.convert(1), is(1L));
    }

    @Test
    public void should_convert_nullables_to_long(){
        assertThat(Types.OPTIONAL_INTEGER.convert(null), nullValue());
        assertThat(Types.OPTIONAL_INTEGER.convert(1), is(1L));
    }

    @Test
    public void should_convert_list_and_array_to_list_of_long(){
        assertThat(Types.LIST_INTEGER.convert(null), nullValue());
        assertThat(Types.LIST_INTEGER.convert(new int[]{1}), is(new LongList(1L)));
        assertThat(Types.LIST_INTEGER.convert(new IntegerList(1)), is(Collections.singletonList(1L)));
        assertThat(Types.LIST_INTEGER.convert(new LongList(1L)), is(Collections.singletonList(1L)));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.INTEGER);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.INTEGER));

    }

    @Test
    public void should_serialize_to_same_instance_as_optional() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OPTIONAL_INTEGER);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OPTIONAL_INTEGER));

    }

    @Test
    public void should_serialize_to_same_instance_as_list() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.LIST_INTEGER);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.LIST_INTEGER));
    }

    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_INTEGER);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_INTEGER));
    }
}
