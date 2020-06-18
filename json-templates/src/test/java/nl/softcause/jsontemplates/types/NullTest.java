package nl.softcause.jsontemplates.types;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class NullTest {

    @Test
    public void should_detect_null_as_null() {
        assertThat(Types.NULL.isA(null), is(true));
    }

    @Test
    public void should_reject_object_as_null() {
        assertThat(Types.NULL.isA(new Object()), is(false));
    }

    @Test
    public void should_pass_through_null() {

        assertThat(Types.OBJECT.convert(null), nullValue());
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.NULL);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.NULL));

    }
}
