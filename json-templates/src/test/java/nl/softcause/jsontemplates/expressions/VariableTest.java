package nl.softcause.jsontemplates.expressions;

import static nl.softcause.jsontemplates.types.Types.INTEGER;
import static nl.softcause.jsontemplates.types.Types.OBJECT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.ModelException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Assert;
import org.junit.Test;

public class VariableTest {

    @Test
    public void should_return_value_of_model() {
        var c = new Variable("name");

        var r = c.evaluate(new TestModel().put("name", "42"));

        assertThat(r, is("42"));
    }

    @Test
    public void should_reject_of_unknown_value() {
        var c = new Variable("wrong");

        try {
            var r = c.evaluate(new TestModel().put("name", "42"));
            fail();
        } catch (ModelException Me) {
            assertThat(Me.getMessage(), is(ModelException.notFound("wrong", TestModel.class).getMessage()));
        }


    }

    @Test
    public void should_return_type_of_any() {
        var model = new DefinedModel<>(VariableTestModel.class);

        assertThat(new Variable("name").getReturnType(model), is(Types.OPTIONAL_TEXT));
        assertThat(new Variable("age").getReturnType(model), is(INTEGER));
        assertThat(new Variable("nested").getReturnType(model), is(OBJECT));

    }

    @Test
    public void should_serialize_to_equal_object() throws IOException {
        var c = new Variable("wrong");

        var json = new ObjectMapper()
                .writeValueAsString(c);
        var obj = (Variable) new ObjectMapper().readValue(json, IExpression.class);

        assertThat(obj, is(c));
        assertThat(obj, isA(Variable.class));
        assertThat(obj.getName(), is("wrong"));
    }

    public class VariableTestModel {
        @Getter
        String name;

        @Getter
        VariableTestModel nested;

        @Setter
        int age;

    }
}
