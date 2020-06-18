package nl.softcause.jsontemplates.expressionparser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import nl.softcause.jsontemplates.expresionparser.ExpressionFormatter;
import nl.softcause.jsontemplates.expressions.Brackets;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.*;
import nl.softcause.jsontemplates.expressions.conversion.FormatInteger;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.expressions.text.Concat;
import org.junit.Test;

public class ExpressionFormatterTest {

    @Test
    public void should_format_constant_string() {
        var e = new Constant("42");

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("'42'"));

    }

    @Test
    public void should_format_constant_int() {
        var e = new Constant(42);

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("42"));

    }

    @Test
    public void should_format_constant_decimal() {
        var e = new Constant(4.2);

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("4.2"));

    }

    @Test
    public void should_format_brackets() {
        var e = new Brackets();
        e.setArguments(Collections.singletonList(new Constant(42)));

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("(42)"));

    }

    @Test
    public void should_format_variable() {
        var e = new Variable("some.value");

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("$some.value"));

    }

    @Test
    public void should_format_simple_expression() {
        var e = new Add();
        e.setArguments(Arrays.asList(new Constant(3), new Constant(7)));

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("3 + 7"));
    }

    @Test
    public void should_format_function_expression() {
        var e = new Modulo();
        e.setArguments(Arrays.asList(new Constant(3), new Constant(7)));

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("3 mod 7"));

    }

    @Test
    public void should_format_function_expression_but_exclude_non_set_optionals() {
        var e = new Round(Arrays.asList(new Constant(3.6533)));

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("round(3.6533)"));
    }

    @Test
    public void should_format_function_expression_but_include_set_optionals() {
        var e = new Round(Arrays.asList(new Constant(3.6533), new Constant(2)));

        var s = new ExpressionFormatter().format(e);

        assertThat(s, is("round(3.6533,2)"));

    }

    @Test
    public void should_format_nested_arithmetic_expression() {
        var add1 = new Add();
        add1.setArguments(Arrays.asList(new Constant(3.2), new Constant(7.1)));

        var add2 = new Add();
        add2.setArguments(Arrays.asList(new Constant(8), new Constant(9)));

        var multiply = new Multiply();
        multiply.setArguments(Arrays.asList(add1, add2));


        var s = new ExpressionFormatter().format(multiply);

        assertThat(s, is("(3.2 + 7.1) * (8 + 9)"));

    }

    @Test
    public void should_format_complex_expression() {
        var add = new Add();
        add.setArguments(Arrays.asList(new Constant(3.2), new Constant(7.1)));

        var mod = new Random(Arrays.asList(add));

        var fi = new FormatInteger(Arrays.asList(mod));

        var concat = new Concat();
        concat.setArguments(Arrays.asList(fi, new Constant("%")));


        var s = new ExpressionFormatter().format(concat);

        assertThat(s, is("concat(formatInteger(random(3.2 + 7.1)),'%')"));

    }

    @Test
    public void should_add_brackets_to_prevent_ambiguity() {
        var nested = new Add();
        nested.setArguments(Arrays.asList(new Constant(7L), new Constant(2L)));
        var m = new Multiply();
        m.setArguments(Arrays.asList(new Constant(3L), nested));

        var s = new ExpressionFormatter().format(m);

        assertThat(s, is("3 * (7 + 2)"));

    }

    @Test
    public void should_not_add_brackets_when_not_ambigues_prevent_ambiguity() {
        var nested = new Add();
        nested.setArguments(Arrays.asList(new Constant(7L), new Constant(2L)));
        var m = new Multiply();
        m.setArguments(Arrays.asList(nested, new Constant(3L)));

        var s = new ExpressionFormatter().format(m);

        assertThat(s, is("(7 + 2) * 3"));

    }

    @Test
    public void should_not_add_brackets_when_not_ambigues_prevent_ambiguityX() {
        var nested = new Add();
        nested.setArguments(Arrays.asList(new Constant(7L), new Constant(2L)));
        var brackets = new Brackets();
        brackets.setArguments(Collections.singletonList(nested));
        var m = new Multiply();
        m.setArguments(Arrays.asList(brackets, new Constant(3L)));

        var s = new ExpressionFormatter().format(m);

        assertThat(s, is("(7 + 2) * 3"));

    }

    @Test
    public void should_format_ternary_expression() {
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(new Constant(false), new Constant(1L), new Constant(2L)));

        var s = new ExpressionFormatter().format(ternary);

        assertThat(s, is("false ? 1 : 2"));

    }
}
