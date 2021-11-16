package nl.softcause.jsontemplates.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import lombok.Getter;
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
    public void should_parse_true_constant() {
        //Given
        var expression = new ExpressionParser().parse("$name == null");
        var model = new TestDefinition();
        model.setName(null);

        //When
        var result = expression.evaluate(new TemplateModel<>(model));

        //Then
        assertThat(result, is(true));
    }


}
