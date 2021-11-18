package nl.softcause.jsontemplates.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import lombok.Data;
import lombok.Getter;
import nl.softcause.jsontemplates.collections.ObjectList;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expresionparser.ParseException;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import nl.softcause.jsontemplates.expressions.arithmetic.Minus;
import nl.softcause.jsontemplates.expressions.arithmetic.Multiply;
import nl.softcause.jsontemplates.expressions.arithmetic.Round;
import nl.softcause.jsontemplates.expressions.logic.Not;
import nl.softcause.jsontemplates.expressions.logic.Or;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.model.DefinedModelTest;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.syntax.ExpressionTypeChecker;
import org.junit.Test;

public class FullTest {

    @Test
    public void should_parse_null_constant() {
        //Given
        var expression = new ExpressionParser().parse("$name == null");
        var model = new TestDefinition();
        model.setName(null);

        //When
        var result = expression.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(result, is(true));
    }

    @Test
    public void should_parse_negation_expression() {
        //Given
        var expression = new ExpressionParser().parse("$name != null");
        var model = new TestDefinition();
        model.setName(null);

        //When
        var result = expression.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(result, is(false));
    }


    @Test
    public void should_type_check_lazy() {
        //Given
        var expression = new ExpressionParser().parse("$name == null || $nested.name == null");
        var model = new TestDefinition();
        model.setName(null);

        //When
        var result = expression.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(result, is(true));
    }

    @Data
    public static class CallModelDefinition {
        public String joined;

        public String[] parts;

        public Helper helper = new Helper();

        public static class Helper {
            public String join(Object part1, Object part2, Object part3) {
                return part1+":"+part2+":"+part3;
            }
        }
    }

    @Test
    public void should_handle_array_from_variable() {
        //Given
        var model = new CallModelDefinition();
        model.setParts(new String[]{"a","b","c"});
        var node = RetrieveDataFromModelNode.create(
                new ExpressionParser().parse("$helper"),
                "join",
                new ExpressionParser().parse("$parts"),
                "joined"
        );

        //When
        node.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(model.getJoined(), is("a:b:c"));
    }

    @Test
    public void should_handle_array_build_from_expression() {
        //Given
        var model = new CallModelDefinition();
        model.setParts(new String[]{"a","b","c"});
        var node = RetrieveDataFromModelNode.create(
                new ExpressionParser().parse("$helper"),
                "join",
                new ExpressionParser().parse("asList('a','b','c')"),
                "joined"
        );

        //When
        node.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(model.getJoined(), is("a:b:c"));
    }

    @Test
    public void should_handle_array_build_from_constant() {
        //Given
        var model = new CallModelDefinition();
        model.setParts(new String[]{"a","b","c"});
        var node = RetrieveDataFromModelNode.create(
                new ExpressionParser().parse("$helper"),
                "join",
                new Constant(new ObjectList("a", "b", "c")),
                "joined"
        );

        //When
        node.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(model.getJoined(), is("a:b:c"));
    }

    public static enum SomeEnumLevel {
        NONE, FUNDAMENTAL, NOVICE, INTERMEDIATE, ADVANCED, EXPERT;
    }

    @Data
    public static class EnumModelDefinition {
        public SomeEnumLevel level;

    }

    @Test
    public void should_compare_enum_by_string() {
        //Given
        var model = new EnumModelDefinition();
        model.setLevel(SomeEnumLevel.EXPERT);
        var expr = new ExpressionParser().parse("$level == 'EXPERT'");
        //When
        var result = expr.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(result, is(true));
    }
}
