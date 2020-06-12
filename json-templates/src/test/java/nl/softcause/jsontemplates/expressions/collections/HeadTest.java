package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.ModelDefinition;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.syntax.ExpressionTypeChecker;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class HeadTest {

    @Test
    public void should_return_first_element() {
        var head = new Head(Arrays.asList(new Constant(new int[] {1, 2, 3})));

        var r = head.evaluate(null);

        assertThat(r, is(1L));
    }

    @Test
    public void should_return_first_element_from_list() {
        var head = new Head(Arrays.asList(new Constant(new IntegerList(1, 2, 3))));

        var r = head.evaluate(null);

        assertThat(r, is(1L));
    }

    @Test
    public void resolve_generic_type_on_ints() {
        var head = new Head(Arrays.asList(new Constant(new IntegerList(1, 2, 3))));

        var checker = new ExpressionTypeChecker(new ModelDefinition<>(TestDefinition.class));

        checker.checkTypes(head);
        var type = checker.getExpressionType(head);

        assertThat(type, is(Types.OPTIONAL_INTEGER));
    }

    @Test
    public void resolve_generic_type_on_object_list() {
        var head = new Head(Arrays.asList(new Constant(new TestDefinition.TestNestedDefinitionList())));

        var checker = new ExpressionTypeChecker(new ModelDefinition<>(TestDefinition.class));

        checker.checkTypes(head);
        var type = checker.getExpressionType(head);

        assertThat(type, is(Types.OPTIONAL_OBJECT));
    }

    @Test
    public void should_return_null_on_empty_list() {
        var head = new Head(Arrays.asList(new Constant(new int[] {})));

        var r = head.evaluate(null);

        assertThat(r, nullValue());
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var head = new Head(Arrays.asList(new Variable("V")));

        var json = new ObjectMapper().writeValueAsString(head);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(head, is(obj));
    }
}
