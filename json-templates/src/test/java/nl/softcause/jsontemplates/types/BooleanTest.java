package nl.softcause.jsontemplates.types;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nl.softcause.jsontemplates.collections.BooleanList;
import nl.softcause.jsontemplates.collections.BooleanMap;
import org.junit.Test;

public class BooleanTest {

    @Test
    public void should_detect_boolean_as_boolean() {
        assertThat(Types.BOOLEAN.isA(true), is(true));
    }

    @Test
    public void should_detect_Big_Boolean_as_boolean() {
        assertThat(Types.BOOLEAN.isA(Boolean.TRUE), is(true));
    }


    @Test
    public void should_reject_other() {
        assertThat(Types.BOOLEAN.isA("true"), is(false));
        assertThat(Types.BOOLEAN.isA(null), is(false));

    }

    @Test
    public void should_accept_null_when_nullable() {
        assertThat(Types.OPTIONAL_BOOLEAN.isA(true), is(true));
        assertThat(Types.OPTIONAL_BOOLEAN.isA(null), is(true));
    }

    @Test
    public void should_convert_boolean_to_long() {
        assertThat(Types.BOOLEAN.convert(true), is(true));
    }

    @Test
    public void should_convert_nullables_to_long() {
        assertThat(Types.OPTIONAL_BOOLEAN.convert(null), nullValue());
        assertThat(Types.OPTIONAL_BOOLEAN.convert(true), is(true));
    }

    @Test
    public void should_accept_list_and_array_when_list() {
        assertThat(Types.LIST_BOOLEAN.isA(new boolean[] {true}), is(true));
        assertThat(Types.LIST_BOOLEAN.isA(new BooleanList(true)), is(true));
        assertThat(Types.LIST_BOOLEAN.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map() {
        assertThat(Types.MAP_BOOLEAN.isA(new BooleanMap()), is(true));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.BOOLEAN);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.BOOLEAN));

    }

    @Test
    public void should_serialize_to_same_instance_as_optional() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OPTIONAL_BOOLEAN);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OPTIONAL_BOOLEAN));

    }


    @Test
    public void should_convert_list_and_array_to_list_of_boolean() {
        assertThat(Types.LIST_BOOLEAN.convert(null), nullValue());
        assertThat(Types.LIST_BOOLEAN.convert(new boolean[] {true}), is(new BooleanList(true)));
        assertThat(Types.LIST_BOOLEAN.convert(new BooleanList(true)), is(new BooleanList(true)));
    }

    @Test
    public void should_serialize_to_same_instance_as_list() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.LIST_BOOLEAN);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.LIST_BOOLEAN));
    }

    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_BOOLEAN);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_BOOLEAN));
    }
}
