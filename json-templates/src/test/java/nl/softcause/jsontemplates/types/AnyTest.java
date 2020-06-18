package nl.softcause.jsontemplates.types;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import lombok.experimental.Delegate;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import org.junit.Test;

public class AnyTest {


    @Test
    public void should_detect_object_as_any() {
        assertThat(Types.OBJECT.isA(new Object()), is(true));
    }


    @Test
    public void xxx() {


        assertThat(solution(4, 25), is(3));
        assertThat(solution(6, 20), is(3));
        assertThat(solution(21, 29), is(0));
        assertThat(solution(21, 29), is(0));

        Random r = new Random();
        for (var i = 0; i < 100000; i++) {
            int a = r.nextInt(10000) + 1;
            int b = a + r.nextInt(10000) + 1;
            solution(a, b);
        }
    }

    public int solution(int A, int B) {
        int m = mathSolution(A, B);
        int s = simpleSolution(A, B);
//        System.out.println(m+"vs"+s);
        if (m != s) {
            System.out.println(m + " vs " + s);
            throw new RuntimeException("BUG " + A + " vs " + B);
        }
        return s;

    }

    public int mathSolution(int A, int B) {
        if (A < 1 || B <= A) {
            throw new IllegalArgumentException(A + ", " + B);
        }
        int aSqrt = (int) Math.sqrt(A);
        int bSqrt = (int) Math.sqrt(B);
        var count = bSqrt - aSqrt - 1;

        if (aSqrt * aSqrt <= A) {
            int P = aSqrt * (aSqrt + 1);
            if (A <= P && P <= B) {
                count++;
            }
        }
        if (bSqrt * bSqrt <= B) {
            int P = bSqrt * (bSqrt + 1);
            if (A <= P && P <= B) {
                count++;
            }
        }
        return Math.max(0, count);
    }

    public int simpleSolution(int A, int B) {
        if (A < 1 || B <= A) {
            throw new IllegalArgumentException();
        }
        int count = 0;
        for (int X = (int) Math.sqrt(A); X * X <= B; X++) {
            int P = X * (X + 1);
            if (A <= P && P <= B) {
                count++;
            }
        }
        return count;
    }


    @Test
    public void should_accept_all_as_any() {
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
    public void should_accept_list_and_array_when_list() {
        assertThat(Types.LIST_OBJECT.isA(new int[] {1}), is(true));
        assertThat(Types.LIST_OBJECT.isA(new IntegerList(1)), is(true));
        assertThat(Types.LIST_OBJECT.isA(new Object[] {new Object()}), is(true));
        assertThat(Types.LIST_OBJECT.isA(new ObjectList(new Object())), is(true));
        assertThat(Types.LIST_OBJECT.isA(null), is(true));
    }

    @Test
    public void should_accept_map_as_map_as_map() {
        assertThat(Types.MAP_OBJECT.isA(new IntegerMap()), is(true));
        assertThat(Types.MAP_OBJECT.isA(new ObjectMap()), is(true));

    }


    @Test
    public void should_pass_through_object() {

        var src = new Object();
        assertThat(Types.OBJECT.convert(src), sameInstance(src));
    }

    @Test
    public void should_convert_list_and_array_to_list_of_object() {
        var src = new Object();
        assertThat(Types.LIST_OBJECT.convert(null), nullValue());
        assertThat(Types.LIST_OBJECT.convert(new Object[] {src}), is(Collections.singletonList(src)));
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

    class ObjectList implements List<Object> {
        @Delegate
        private List<Object> base = new ArrayList<>();

        public ObjectList(Object... values) {
            base.addAll(Arrays.asList(values));
        }
    }

    class ObjectMap implements Map<String, Object> {
        @Delegate
        private Map<String, Object> base = new HashMap<>();

    }
}
