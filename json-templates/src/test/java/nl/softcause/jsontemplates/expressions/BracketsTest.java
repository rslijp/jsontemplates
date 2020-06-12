package nl.softcause.jsontemplates.expressions;

import static nl.softcause.jsontemplates.types.Types.GENERIC;
import static nl.softcause.jsontemplates.types.Types.INTEGER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.syntax.ExpressionTypeChecker;
import org.junit.Assert;
import org.junit.Test;

public class BracketsTest {

    @Test
    public void should_return_value_of_nested_expression() {
        var c = new Brackets();
        c.setArguments(Collections.singletonList(new Constant(42)));

        var r = c.evaluate(null);

        assertThat(r, is(42L));
    }

    @Test
    public void should_take_return_type_of_nested_expression() {
        var c = new Brackets();
        c.setArguments(Collections.singletonList(new Constant(42)));

        var etc = new ExpressionTypeChecker(new TemplateModel<>(new TestDefinition()));

        var type = etc.getRawExpressionType(c);


        assertThat(c.getReturnType(null), is(GENERIC));
        assertThat(type, is(INTEGER));
    }


    @Test
    public void should_serialize_to_equal_object() throws IOException {
        var c = new Brackets();
        c.setArguments(Collections.singletonList(new Constant(42)));

        var json = new ObjectMapper()
                .writeValueAsString(c);
        var obj = (Brackets) new ObjectMapper().readValue(json, IExpression.class);

        Assert.assertThat(obj, is(c));
    }
}
