package nl.softcause.jsontemplates.expressionparser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import lombok.Getter;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expresionparser.ParseException;
import nl.softcause.jsontemplates.expressions.Constant;
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
import nl.softcause.jsontemplates.syntax.ExpressionTypeChecker;
import org.junit.Test;

public class ExpressionParserTest {

    @Test
    public void should_parse_true_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("true");
        //Then
        assertThat(constant.getValue(), is(true));
    }

    @Test
    public void should_parse_false_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("false");
        //Then
        assertThat(constant.getValue(), is(false));
    }

    @Test
    public void should_parse_simple_int_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("3");
        //Then
        assertThat(constant.getValue(), is(3L));
    }

    @Test
    public void should_parse_simple_negative_int_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("-3");
        //Then
        assertThat(constant.getValue(), is(-3L));
    }

    @Test
    public void should_parse_simple_double_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("3.42");
        //Then
        assertThat(constant.getValue(), is(3.42));
    }

    @Test
    public void should_parse_simple_negativ_double_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("-3.42");
        //Then
        assertThat(constant.getValue(), is(-3.42));
    }

    @Test
    public void should_parse_simple_string_constant() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("'Quote hasn\\'t got a value'");
        //Then
        assertThat(constant.getValue(), is("Quote hasn't got a value"));
    }

    @Test
    public void should_parse_simple_string_constant_with_quote() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("'Hello world'");
        //Then
        assertThat(constant.getValue(), is("Hello world"));
    }


    @Test
    public void should_parse_simple_string_constant_with_dash() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("'hello-world'");
        //Then
        assertThat(constant.getValue(), is("hello-world"));
    }

    @Test
    public void should_parse_simple_string_constant_with_odd_chars() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("'Hello world!?.\";:{}/\\#'");
        //Then
        assertThat(constant.getValue(), is("Hello world!?.\";:{}/\\#"));
    }


    @Test
    public void should_trim_start() {
        //Given
        var constant = (Constant) new ExpressionParser().parse(" 'Hello world'");
        //Then
        assertThat(constant.getValue(), is("Hello world"));
    }

    @Test
    public void should_trim_end() {
        //Given
        var constant = (Constant) new ExpressionParser().parse("'Hello world' ");
        //Then
        assertThat(constant.getValue(), is("Hello world"));
    }


    @Test
    public void should_both_start_and_end() {
        //Given
        var constant = (Constant) new ExpressionParser().parse(" 'Hello world' ");
        //Then
        assertThat(constant.getValue(), is("Hello world"));
    }

    @Test
    public void should_parse_simple_string_variable() {
        //Given
        var constant = (Variable) new ExpressionParser().parse("$bla");
        //Then
        assertThat(constant.getName(), is("bla"));
    }

    @Test
    public void should_parse_simple_string_variable_with_dots() {
        //Given
        var constant = (Variable) new ExpressionParser().parse("$bla.bla");
        //Then
        assertThat(constant.getName(), is("bla.bla"));
    }

    @Test
    public void should_parse_add_expression() {
        var expected = new Add();
        expected.setArguments(Arrays.asList(new Constant(3L), new Constant(7L)));


        //When
        var actual = new ExpressionParser().parse("3+7");
        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_nested_add_expression() {
        var nested = new Add();
        nested.setArguments(Arrays.asList(new Constant(3L), new Constant(7L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(nested, new Constant(1L)));


        //When
        var actual = new ExpressionParser().parse("3+7+1");

        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_expression_with_spaces() {
        var nested = new Add();
        nested.setArguments(Arrays.asList(new Constant(3L), new Constant(7L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(nested, new Constant(1L)));


        //When
        var actual = new ExpressionParser().parse("3 + 7 + 1");

        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_function_expression() {
        var expected = new Round(Arrays.asList(new Constant(3.2), new Constant(1L)));

        //When
        var actual = new ExpressionParser().parse("round(3.2,1)");

        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_function_with_optional_argument_expression() {
        var expected = new Round(Collections.singletonList(new Constant(3.2)));

        //When
        var actual = new ExpressionParser().parse("round(3.2)");

        assertThat(actual, is(expected));
    }


    @Test
    public void should_parse_function_nested_expression() {
        var nested = new Minus();
        nested.setArguments(Arrays.asList(new Constant(3.3), new Constant(0.1)));
        var expected = new Round(Arrays.asList(nested, new Constant(1L)));

        //When
        var actual = new ExpressionParser().parse("round(3.3-0.1,1)");

        assertThat(actual, is(expected));
    }


    @Test
    public void should_parse_nested_priority_expression() {
        var nested = new Multiply();
        nested.setArguments(Arrays.asList(new Constant(3L), new Constant(7L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(nested, new Constant(1L)));

        //When
        var actual = new ExpressionParser().parse("3*7+1");

        assertThat(actual, is(expected));
        assertThat(actual.evaluate(null), is(expected.evaluate(null)));
    }

    @Test
    public void should_parse_nested_honor_priority_expression() {
        var nested = new Multiply();
        nested.setArguments(Arrays.asList(new Constant(2L), new Constant(3L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(new Constant(7L), nested));

        //When
        var actual = new ExpressionParser().parse("7+2*3");

        assertThat(expected.evaluate(null), is(13.0));
        assertThat(actual.evaluate(null), is(13.0));
        assertThat(expected.evaluate(null), is(actual.evaluate(null)));
        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_nested_honor_priority_expression_2() {
        var nestedLhs = new Multiply();
        nestedLhs.setArguments(Arrays.asList(new Constant(4L), new Constant(7L)));
        var nestedRhs = new Multiply();
        nestedRhs.setArguments(Arrays.asList(new Constant(2L), new Constant(3L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(nestedLhs, nestedRhs));

        //When
        var actual = new ExpressionParser().parse("4*7+2*3");

        assertThat(expected.evaluate(null), is(34.0));
        assertThat(actual.evaluate(null), is(34.0));
        assertThat(expected.evaluate(null), is(actual.evaluate(null)));
        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_nested_honor_priority_expression_reversed_argument_order() {
        var nested = new Multiply();
        nested.setArguments(Arrays.asList(new Constant(3L), new Constant(7L)));
        var expected = new Add();
        expected.setArguments(Arrays.asList(nested, new Constant(2L)));


        //When
        var actual = new ExpressionParser().parse("3*7+2");


        assertThat(expected.evaluate(null), is(23.0));
        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_unary_expression() {
        var nested = new Constant(false);

        var expected = new Not(Collections.singletonList(nested));

        //When
        var actual = new ExpressionParser().parse("!false");

        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_nested_unary_expression() {
        var nested = new Constant(false);

        var not = new Not(Collections.singletonList(nested));
        var expected = new Or();
        expected.setArguments(Arrays.asList(not, new Constant(false)));

        //When
        var actual = new ExpressionParser().parse("!false||false");

//        System.out.println(new ExpressionFormatter().format(actual));
        assertThat(actual.evaluate(null), is(true));
        assertThat(actual, is(expected));
    }


    @Test
    public void should_parse_ternary_expression() {
        var expected = new Ternary();
        expected.getArguments().addAll(Arrays.asList(new Constant(false), new Constant(1L), new Constant(2L)));


        var actual = new ExpressionParser().parse("false?1:2");

        assertThat(actual.evaluate(null), is(2L));
        assertThat(actual, is(expected));

    }

    @Test
    public void should_not_parse_partial_ternary_expression() {
        var expected = new Ternary();
        expected.getArguments().addAll(Arrays.asList(new Constant(false), new Constant(1L), new Constant(2L)));

        try {
            new ExpressionParser().parse("false?1:");
            fail();
        } catch (ParseException Pe) {
            assertThat(Pe.getBaseMessage(), is(ParseException.expectedMoreArguments().getMessage()));
        }
    }

    @Test
    public void should_parse_nesting_in_ternary_expression() {
        var nested = new Ternary();
        nested.getArguments().addAll(Arrays.asList(new Variable("other"), new Constant(true), new Constant(false)));
        var expected = new Ternary();
        expected.getArguments().addAll(Arrays.asList(new Variable("test"), new Constant(true), nested));
        var actual = new ExpressionParser().parse("$test?true:$other?true:false");

        assertThat(actual.evaluate(new TemplateModel<>(new TernaryTestModel())), is(false));
        assertThat(actual, is(expected));
    }

    @Test
    public void should_parse_nesting_in_ternary_expression2() {
        var condition = new Not();
        condition.getArguments().add(new Constant(false));
        var lhs = new Multiply();
        lhs.getArguments().add(new Constant(6L));
        lhs.getArguments().add(new Constant(7L));
        var rhs = new Minus();
        rhs.getArguments().add(new Constant(9L));
        rhs.getArguments().add(new Constant(5L));
        var nested = new Ternary();
        nested.getArguments().addAll(Arrays.asList(condition, lhs, rhs));
        var expected = new Ternary();
        expected.getArguments().addAll(Arrays.asList(new Constant(false), new Constant(1.0), nested));


        var actual = new ExpressionParser().parse("false?1.0:!false?6*7:9-5");

        assertThat(actual.evaluate(null), is(42.0));
        new ExpressionTypeChecker(DefinedModelTest.class).checkTypes(condition);
        new ExpressionTypeChecker(DefinedModelTest.class).checkTypes(lhs);
        new ExpressionTypeChecker(DefinedModelTest.class).checkTypes(rhs);
        new ExpressionTypeChecker(DefinedModelTest.class).checkTypes(nested);
        new ExpressionTypeChecker(DefinedModelTest.class).checkTypes(expected);
        assertThat(actual, is(expected));

    }

    @Test
    public void operator_precedence_1() {
        var actual = new ExpressionParser().parse("2+3 pow 2*2+2");

        assertThat(actual.evaluate(null), is(22.0));
    }

    @Test
    public void operator_precedence_2() {
        var actual = new ExpressionParser().parse("2+3*2+2pow2");

        assertThat(actual.evaluate(null), is(12.0));
    }

    @Test
    public void operator_precedence_3() {
        var actual = new ExpressionParser().parse("(6+$firstInt*3 pow (2 mod 1))<(9+$secondInt pow (4/2))");

        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
    }

    @Test
    public void operator_precedence_4() {
        var actual = new ExpressionParser()
                .parse("13 + $firstInt / 2 >= $secondInt - $secondInt mod 8 && 7.0 == $firstInt / 6");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
    }

    @Test
    public void operator_precedence_4b() {
        var actual = new ExpressionParser().parse("7 == $firstInt / 6");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
    }

    @Test
    public void operator_precedence_4c() {
        var actual = new ExpressionParser().parse("$firstInt / 6 == 7");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
    }

    @Test
    public void operator_precedence_5() {
        var actual = new ExpressionParser().parse("false||!true");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(false));
    }

    @Test
    public void operator_precedence_6() {
        var actual = new ExpressionParser().parse("!false && !false");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
    }

    @Test
    public void operator_precedence_7() {
        var actual = new ExpressionParser().parse("!!false && !false");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(false));
    }

    @Test
    public void operator_precedence_8() {
        var actual = new ExpressionParser().parse("!(!false && !false)");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(false));
    }

    @Test
    public void case3_bug1() {

        var actual = new ExpressionParser().parse("1+parseInteger('1')");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(2.0));
    }

    //parseInteger('1')+1+parseInteger('1')
    @Test
    public void case3_bug2() {

        var actual = new ExpressionParser().parse("parseInteger('1')+1+parseInteger('1')");
        assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(3.0));
    }

    public class TernaryTestModel {
        public boolean isTest() {
            return false;
        }

        public boolean isOther() {
            return false;
        }
    }

    public class TestModel {
        @Getter
        public int firstInt = 42;
        @Getter
        public int secondInt = 37;
    }
//    @Test
//    public void benchmark()
//    {
//        IExpression actual= new ExpressionParser().parse("(6+$firstInt*3 pow (2 mod 1))<(9+$secondInt pow (4/2))");
//        for(var i=0; i<10000; i++) {
//            actual = new ExpressionParser().parse("(6+$firstInt*3 pow (2 mod 1))<(9+$secondInt pow (4/2))");
//            assertThat(actual.evaluate(new TemplateModel<>(new TestModel())), is(true));
//        }
//    }
}
