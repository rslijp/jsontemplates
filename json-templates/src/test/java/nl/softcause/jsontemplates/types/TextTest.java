package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.collections.StringList;
import nl.softcause.jsontemplates.collections.StringMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TextTest {

    @Test
    public void should_detect_string_as_text(){
        assertThat(Types.TEXT.isA("a"), is(true));
    }

    @Test
    public void should_detect_stringbuffer_as_text(){
        assertThat(Types.TEXT.isA(new StringBuffer()), is(true));
    }

    @Test
    public void should_reject_other(){
        assertThat(Types.TEXT.isA(1.0), is(false));
        assertThat(Types.TEXT.isA(null), is(false));
    }

    @Test
    public void should_accept_null_when_nullable(){
        assertThat(Types.OPTIONAL_TEXT.isA("a"), is(true));
        assertThat(Types.OPTIONAL_TEXT.isA(null), is(true));
    }

    @Test
    public void should_accept_list_and_array_when_list(){
        assertThat(Types.LIST_TEXT.isA(new String[]{"a"}), is(true));
        assertThat(Types.LIST_TEXT.isA(new StringList("a")), is(true));
        assertThat(Types.LIST_TEXT.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map(){
        assertThat(Types.MAP_TEXT.isA(new StringMap()), is(true));
    }


    @Test
    public void should_convert_string_to_string(){
        assertThat(Types.TEXT.convert("hello"), is("hello"));
    }

    @Test
    public void should_convert_buffer_to_string(){
        assertThat(Types.TEXT.convert(new StringBuffer().append("hello")), is("hello"));
    }

    @Test
    public void should_convert_nullables_to_string(){
        assertThat(Types.OPTIONAL_TEXT.convert(null), nullValue());
        assertThat(Types.OPTIONAL_TEXT.convert("world"), is("world"));
    }


    @Test
    public void should_convert_list_and_array_to_list_of_string(){
        assertThat(Types.LIST_TEXT.convert(null), nullValue());
        assertThat(Types.LIST_TEXT.convert(new String[]{"world"}), is(Collections.singletonList("world")));
        assertThat(Types.LIST_TEXT.convert(new StringList("world")), is(Collections.singletonList("world")));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.TEXT);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.TEXT));

    }

    @Test
    public void should_serialize_to_same_instance_as_optional() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OPTIONAL_TEXT);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OPTIONAL_TEXT));
    }

    @Test
    public void should_serialize_to_same_instance_as_list() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.LIST_TEXT);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.LIST_TEXT));

    }

    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_TEXT);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_TEXT));

    }
}
