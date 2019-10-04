package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.Delegate;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AnyTest {

    @Test
    public void should_detect_object_as_any(){
        assertThat(Types.OBJECT.isA(new Object()), is(true));
    }

    @Test
    public void should_accept_all_as_any(){
        assertThat(Types.OBJECT.isA(1.0f), is(true));
        assertThat(Types.OBJECT.isA(1.0), is(true));
        assertThat(Types.OBJECT.isA(1), is(true));
        assertThat(Types.OBJECT.isA(1L), is(true));
        assertThat(Types.OBJECT.isA(null), is(true));
        assertThat(Types.OBJECT.isA(true), is(true));
        assertThat(Types.OBJECT.isA(null), is(true));
        assertThat(Types.OBJECT.isA("text"), is(true));
    }

    @Test
    public void should_accept_list_and_array_when_list(){
        assertThat(Types.LIST_OBJECT.isA(new int[]{1}), is(true));
        assertThat(Types.LIST_OBJECT.isA(new IntegerList(1)), is(true));
        assertThat(Types.LIST_OBJECT.isA(new Object[]{new Object()}), is(true));
        assertThat(Types.LIST_OBJECT.isA(new ObjectList(new Object())), is(true));
        assertThat(Types.LIST_OBJECT.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map_as_map(){
        assertThat(Types.MAP_OBJECT.isA(new IntegerMap()), is(true));
        assertThat(Types.MAP_OBJECT.isA(new ObjectMap()), is(true));

    }


    @Test
    public void should_pass_through_object(){

        var src = new Object();
        assertThat(Types.OBJECT.convert(src), sameInstance(src));
    }

    class ObjectList implements List<Object> {
        @Delegate
        private List<Object> base = new ArrayList<>();

        public ObjectList(Object...values){
            base.addAll(Arrays.asList(values));
        }
    }

    class ObjectMap implements Map<String,Object> {
        @Delegate
        private Map<String,Object> base = new HashMap<>();

    }


    @Test
    public void should_convert_list_and_array_to_list_of_object(){
        var src = new Object();
        assertThat(Types.LIST_OBJECT.convert(null), nullValue());
        assertThat(Types.LIST_OBJECT.convert(new Object[]{src}), is(Collections.singletonList(src)));
        assertThat(Types.LIST_OBJECT.convert(new ObjectList(src)), is(Collections.singletonList(src)));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OBJECT);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OBJECT));

    }

    @Test
    public void should_serialize_to_same_instance_as_list() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.LIST_OBJECT);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.LIST_OBJECT));
    }

    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_BOOLEAN);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_BOOLEAN));
    }
}
