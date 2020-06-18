package nl.softcause.jsontemplates.expressions.arithmetic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class ModuloTest {

    @Test
    public void should_modulo_to_values() {
        var modulo = new Modulo();
        modulo.setArguments(Arrays.asList(new Constant(13), new Constant(7)));

        var r = modulo.evaluate(null);

        assertThat(r, is(6.0));
    }

    @Test
    public void should_auto_cast_values() {
        var modulo = new Modulo();
        modulo.setArguments(Arrays.asList(new Constant(13.5), new Constant(7.0)));

        var r = modulo.evaluate(null);

        assertThat(r, is(6.5));
    }

    @Test
    public void should_consume_variables() {
        var modulo = new Modulo();
        modulo.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", 3).put("R", 7);

        var r = modulo.evaluate(model);

        assertThat(r, is(3.0));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var modulo = new Modulo();
        modulo.setArguments(Arrays.asList(new Variable("V"), new Constant(42)));

        var json = new ObjectMapper().writeValueAsString(modulo);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(modulo, is(obj));
    }
}
