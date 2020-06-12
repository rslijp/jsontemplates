package nl.softcause.jsontemplates.expressions.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import org.junit.Test;

public class NullOrDefaultTest {

    @Test
    public void should_return_primary_element_when_not_null() {
        var td = new TestDefinition();
        td.setMentalAge(42);
        var nullOrDefault = new NullOrDefault(Arrays.asList(new Variable("mentalAge"), new Constant(37)));

        var r = nullOrDefault.evaluate(new DefinedModel<TestDefinition>(td));

        assertThat(r, is(42L));
    }

    @Test
    public void should_return_fallback_element_when_primary_is_null() {
        var td = new TestDefinition();
        td.setMentalAge(null);
        var nullOrDefault = new NullOrDefault(Arrays.asList(new Variable("mentalAge"), new Constant(37)));

        var r = nullOrDefault.evaluate(new DefinedModel<TestDefinition>(td));

        assertThat(r, is(37L));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var nullOrDefault = new NullOrDefault(Arrays.asList(new Variable("V"), new Constant(37)));

        var json = new ObjectMapper().writeValueAsString(nullOrDefault);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(nullOrDefault, is(obj));
    }
}
