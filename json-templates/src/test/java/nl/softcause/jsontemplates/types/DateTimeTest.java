package nl.softcause.jsontemplates.types;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import nl.softcause.jsontemplates.collections.CalendarList;
import nl.softcause.jsontemplates.collections.DateList;
import nl.softcause.jsontemplates.collections.InstantList;
import org.junit.Test;

public class DateTimeTest {

    @Test
    public void should_detect_Date_as_DateTime() {
        assertThat(Types.DATETIME.isA(new Date()), is(true));
    }

    @Test
    public void should_detect_Calendar_as_DateTime() {
        assertThat(Types.DATETIME.isA(Instant.now()), is(true));
    }

    @Test
    public void should_detect_Instant_as_DateTime() {
        assertThat(Types.DATETIME.isA(new GregorianCalendar()), is(true));
    }

    @Test
    public void should_reject_other() {
        assertThat(Types.DATETIME.isA("true"), is(false));
        assertThat(Types.DATETIME.isA(null), is(false));

    }

    @Test
    public void should_accept_null_when_nullable() {
        assertThat(Types.OPTIONAL_DATETIME.isA(new Date()), is(true));
        assertThat(Types.OPTIONAL_DATETIME.isA(null), is(true));
    }


    @Test
    public void should_accept_list_and_array_when_list() {
        assertThat(Types.LIST_DATETIME.isA(new Date[] {new Date()}), is(true));
        assertThat(Types.LIST_DATETIME.isA(new DateList(new Date())), is(true));
        assertThat(Types.LIST_DATETIME.isA(new Instant[] {Instant.now()}), is(true));
        assertThat(Types.LIST_DATETIME.isA(new InstantList(Instant.now())), is(true));
        assertThat(Types.LIST_DATETIME.isA(new Calendar[] {new GregorianCalendar()}), is(true));
        assertThat(Types.LIST_DATETIME.isA(new CalendarList(new GregorianCalendar())), is(true));
        assertThat(Types.LIST_DATETIME.isA(null), is(true));
    }

    @Test
    public void should_convert_DateTime_to_Instant() {
        assertThat(Types.DATETIME.convert(new Date(190000)), is(new Date(190000).toInstant()));
    }

    @Test
    public void should_convert_Instant_to_Instant() {
        assertThat(Types.DATETIME.convert(new Date(190000).toInstant()), is(new Date(190000).toInstant()));
    }

    @Test
    public void should_convert_Calendar_to_Instant() {
        var cal = new GregorianCalendar();
        assertThat(Types.DATETIME.convert(cal), is(cal.toInstant()));
    }

    @Test
    public void should_convert_nullables_to_Instant() {
        assertThat(Types.OPTIONAL_DATETIME.convert(null), nullValue());
        assertThat(Types.OPTIONAL_DATETIME.convert(new Date(190000)), is(new Date(190000).toInstant()));
    }

    @Test
    public void should_convert_list_and_array_to_list_of_long() {
        var cal = new GregorianCalendar();
        assertThat(Types.LIST_DATETIME.convert(null), nullValue());
        assertThat(Types.LIST_DATETIME.convert(new Date[] {new Date(190000)}),
                is(Collections.singletonList(new Date(190000).toInstant())));
        assertThat(Types.LIST_DATETIME.convert(new DateList(new Date(190000))),
                is(Collections.singletonList(new Date(190000).toInstant())));
        assertThat(Types.LIST_DATETIME.convert(new Instant[] {new Date(190000).toInstant()}),
                is(Collections.singletonList(new Date(190000).toInstant())));
        assertThat(Types.LIST_DATETIME.convert(new InstantList(new Date(190000).toInstant())),
                is(Collections.singletonList(new Date(190000).toInstant())));
        assertThat(Types.LIST_DATETIME.convert(new Calendar[] {cal}), is(Collections.singletonList(cal.toInstant())));
        assertThat(Types.LIST_DATETIME.convert(new CalendarList(cal)), is(Collections.singletonList(cal.toInstant())));
    }

    @Test
    public void should_serialize_to_same_instance() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.DATETIME);
        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.DATETIME));

    }

    @Test
    public void should_serialize_to_same_instance_as_optional() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.OPTIONAL_DATETIME);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.OPTIONAL_DATETIME));
    }

    @Test
    public void should_serialize_to_same_instance_as_map() throws IOException {

        var json = new ObjectMapper()
                .writeValueAsString(Types.MAP_DATETIME);

        var obj = new ObjectMapper().readValue(json, IExpressionType.class);

        assertThat(obj, sameInstance(Types.MAP_DATETIME));
    }
}
