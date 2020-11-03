package nl.softcause.jsontemplates.syntax;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.StringList;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import nl.softcause.jsontemplates.expressions.arithmetic.Minus;
import nl.softcause.jsontemplates.expressions.arithmetic.Multiply;
import nl.softcause.jsontemplates.expressions.collections.Append;
import nl.softcause.jsontemplates.expressions.conversion.FormatInteger;
import nl.softcause.jsontemplates.expressions.conversion.NullOrDefault;
import nl.softcause.jsontemplates.expressions.logic.And;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.expressions.text.Concat;
import nl.softcause.jsontemplates.model.*;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class ExpressionTypeCheckerTest {

    @Test
    public void should_accept_constant_as_valid_type_expression() {
        try {
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(new Constant(null));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_accept_a_variable_as_valid_type_expression() {
        try {
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(new Variable("name"));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_reject_a_variable_without_a_model() {
        try {
            new ExpressionTypeChecker((IModelDefinition) null).checkTypes(new Variable("wrong"));
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(), is(TypeException.noModelDefinition("wrong").getMessage()));
        }
    }

    @Test
    public void should_reject_a_variable_of_unknown_property() {
        try {
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(new Variable("wrong"));
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(), is(ModelException.notFound("wrong", TestDefinition.class).getMessage()));
        }
    }

    @Test
    public void should_accept_a_arithmetic_expression_with_doubles_as_valid_type_expression() {
        try {
            var lhs = new Constant(42.0);
            var rhs = new Constant(37.0);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_accept_a_arithmetic_expression_with_integers_as_valid_type_expression() {
        try {
            var lhs = new Constant(42);
            var rhs = new Constant(37);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }


    @Test
    public void should_reject_a_arithmetic_expression_with_with_on_string_argument_lhs() {
        try {
            var lhs = new Constant("42.0");
            var rhs = new Constant(37.0);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.TEXT, String.valueOf(1)).getMessage()));
        }
    }

    @Test
    public void should_reject_a_arithmetic_expression_with_with_on_string_argument_both() {
        try {
            var lhs = new Constant("42.0");
            var rhs = new Constant("37.0");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.TEXT, String.valueOf(1)).getMessage()));
        }
    }


    @Test
    public void should_reject_a_arithmetic_expression_with_with_on_string_argument_rhs() {
        try {
            var lhs = new Constant(42.0);
            var rhs = new Constant("37.0");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.TEXT, String.valueOf(2)).getMessage()));
        }
    }


    @Test
    public void should_accept_and_recurse_into_complex_expression() {
        try {
            var lhs = new Add();
            lhs.setArguments(Arrays.asList(new Constant(42.0), new Constant(37.0)));
            var rhs = new Minus();
            rhs.setArguments(Arrays.asList(new Constant(8.0), new Constant(2.0)));

            var mult = new Multiply();
            mult.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(mult);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_reject_nested_error() {
        try {
            var lhs = new Add();
            lhs.setArguments(Arrays.asList(new Constant(42.0), new Constant(37.0)));
            var rhs = new Minus();
            rhs.setArguments(Arrays.asList(new Constant(8.0), new Constant("2.0")));

            var mult = new Multiply();
            mult.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(mult);
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.TEXT, String.valueOf(2)).getMessage()));
        }
    }

    @Test
    public void should_reject_top_level_error() {
        try {
            var lhs = new Add();
            lhs.setArguments(Arrays.asList(new Constant(42.0), new Constant(37.0)));
            var rhs = new And();
            rhs.setArguments(Arrays.asList(new Constant(true), new Constant(true)));

            var mult = new Multiply();
            mult.setArguments(Arrays.asList(lhs, rhs));
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(mult);
            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.BOOLEAN, String.valueOf(2)).getMessage()));
        }
    }

    @Test
    public void should_accept_a_non_optional_also_as_optional() {
        try {
            //Given
            var lhs = new Constant("Hello");
            var rhs = new Constant("World");
            var add = new Concat();
            add.setArguments(Arrays.asList(lhs, rhs));

            //Validate
            assertThat(lhs.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.TEXT));
            assertThat(add.getArgumentsTypes()[0], is(Types.OPTIONAL_TEXT));
            assertThat(add.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.TEXT));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_accept_a_listobject_also_as_optional() {
        try {
            //Given
            var lhs = new Constant(new TestDefinition.TestNestedDefinitionList());
            var rhs = new Constant(new TestDefinition.TestNestedDefinitionList());
            var add = new Append();
            add.setArguments(Arrays.asList(lhs, rhs));

            //Validate
            assertThat(lhs.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_OBJECT));
            assertThat(add.getArgumentsTypes()[0], is(Types.LIST_GENERIC));
            assertThat(add.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_GENERIC));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_accept_a_list_of_integers_as_object_list() {
        try {
            //Given
            var lhs = new Constant(new IntegerList());
            var rhs = new Constant(new IntegerList());
            var add = new Append();
            add.setArguments(Arrays.asList(lhs, rhs));

            //Validate
            assertThat(lhs.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_INTEGER));
            assertThat(add.getArgumentsTypes()[0], is(Types.LIST_GENERIC));
            assertThat(add.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_GENERIC));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type." + TCe.getMessage());
        }
    }

    @Test
    public void should_reject_a_list_of_integers_and_a_list_of_objects_as_object_list() {
        try {
            //Given
            var lhs = new Constant(new TestDefinition.TestNestedDefinitionList());
            var rhs = new Constant(new IntegerList());
            var add = new Append();
            add.setArguments(Arrays.asList(lhs, rhs));

            //Validate
            assertThat(lhs.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_OBJECT));
            assertThat(add.getArgumentsTypes()[0], is(Types.LIST_GENERIC));
            assertThat(add.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_GENERIC));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be allowed");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(), is(TypeCheckException
                    .wrongModel(TestDefinition.TestNestedDefinitionList.class.getSimpleName(),
                            Types.LIST_INTEGER.getType(), 1).getMessage()));
        }
    }

    @Test
    public void should_reject_a_list_of_integers_and_a_list_of_strings() {
        try {
            //Given
            var lhs = new Constant(new StringList());
            var rhs = new Constant(new IntegerList());
            var add = new Append();
            add.setArguments(Arrays.asList(lhs, rhs));

            //Validate
            assertThat(lhs.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_TEXT));
            assertThat(add.getArgumentsTypes()[0], is(Types.LIST_GENERIC));
            assertThat(add.getReturnType(new ModelDefinition<>(TestDefinition.class)), is(Types.LIST_GENERIC));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be allowed");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.wrongModel(Types.LIST_TEXT.getType(), Types.LIST_INTEGER.getType(), 1)
                            .getMessage()));
        }
    }

    @Test
    public void should_accept_proper_object_on_null_conversion() {
        //Given
        var lhs = new Constant(new TestDefinition());
        var rhs = new Constant(new TestDefinition());
        var add = new NullOrDefault(Arrays.asList(lhs, rhs));

        //Case
        new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
    }

    @Test
    public void should_reject_wrong_object_on_null_conversion() {
        try {
            //Given
            var lhs = new Constant(new TestDefinition());
            var rhs = new Constant(new TestNestedDefinition());
            var add = new NullOrDefault(Arrays.asList(lhs, rhs));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be allowed");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(), is(TypeCheckException
                    .wrongModel(TestDefinition.class.getSimpleName(), TestNestedDefinition.class.getSimpleName(), 1)
                    .getMessage()));
        }
    }

    @Test
    public void should_reject_on_wrong_type_null_conversion() {
        try {
            //Given
            var lhs = new Constant(new TestDefinition());
            var rhs = new Constant(42);
            var add = new NullOrDefault(Arrays.asList(lhs, rhs));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail("Should not be allowed");
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.wrongModel(TestDefinition.class.getSimpleName(), Types.INTEGER.getType(), 1)
                            .getMessage()));
        }
    }

    @Test
    public void should_accept_on_optional_to_base_type_conversion() {
        //Given
        var lhs = new Variable("mentalAge");
        var rhs = new Variable("age");
        var add = new NullOrDefault(Arrays.asList(lhs, rhs));

        //Case
        new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
    }

    @Test
    public void should_reject_on_second_optional() {
        try {
            //Given
            var lhs = new Variable("mentalAge");
            var rhs = new Variable("mentalAge");
            var add = new NullOrDefault(Arrays.asList(lhs, rhs));

            //Case
            new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
            fail();
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.GENERIC, Types.OPTIONAL_GENERIC, String.valueOf(2))
                            .getMessage()));
        }
    }

    @Test
    public void should_hold_nested_value_of_return_type() {
        //Given
        var lhs = new NullOrDefault(Arrays.asList(new Variable("mentalAge"), new Variable("age")));
        var rhs = new Constant(42L);
        var add = new Add();
        add.setArguments(Arrays.asList(lhs, rhs));

        //Case
        new ExpressionTypeChecker(TestDefinition.class).checkTypes(add);
    }

    @Test
    public void should_hold_nested_optional_resolve() {
        //Given
        var lhs = new Variable("mentalAge");
        var def = new Variable("age");
        var nod = new NullOrDefault(Arrays.asList(lhs, def));


        var fmt = new FormatInteger(Arrays.asList(nod));


        //Case
//        new ExpressionTypeChecker(TestDefinition.class).checkTypes(fmt);

        var returnType = new ExpressionTypeChecker(TestDefinition.class).cachedDetermineReturnType(fmt);
        assertThat(returnType, is(Types.TEXT));
    }


    @Test
    public void should_hold_non_optional_when_resolve_is_active() {
        //Given
        var lhs = new Variable("mentalAge");
        var def = new Variable("age");
        var nod = new NullOrDefault(Arrays.asList(lhs, def));


        var fmt = new FormatInteger(Arrays.asList(nod));
        new ExpressionTypeChecker(TestDefinition.class).checkTypes(fmt);


        //Case
        var returnType = new ExpressionTypeChecker(TestDefinition.class).cachedDetermineReturnType(fmt);

        //Then
        assertThat(returnType, is(Types.TEXT));
    }

    @Test
    public void should_drop_optional_when_resolve_is_active() {
        //Given
        var lhs = new Variable("mentalAge");


        var fmt = new FormatInteger(Arrays.asList(lhs));
        new ExpressionTypeChecker(TestDefinition.class).checkTypes(fmt);


        //Case
        var returnType = new ExpressionTypeChecker(TestDefinition.class).cachedDetermineReturnType(fmt);

        //Then
        assertThat(returnType, is(Types.OPTIONAL_TEXT));
    }

    @Test
    public void should_accept_a_valid_scope_expression() {
        try {
            var lhs = new Variable("scope.lhs");
            var rhs = new Variable("scope.rhs");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));

            var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
            modelDefintion.addDefinition("lhs", Types.INTEGER, null, true, true, 0, null);
            modelDefintion.addDefinition("rhs", Types.INTEGER, null, true, true, 0, null);

            new ExpressionTypeChecker(modelDefintion).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_accept_a_valid_scope_expression_with_auto_resolve() {
        try {
            var lhs = new Variable("scope.lhs");
            var rhs = new Variable("scope.rhs");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));

            var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
            modelDefintion.addDefinition("lhs", Types.INTEGER, null, true, true, 0, null);
            modelDefintion.addDefinition("rhs", Types.INTEGER, null, true, true, 0, null);
            modelDefintion.pushScope(null);

            new ExpressionTypeChecker(modelDefintion).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }


    @Test
    public void should_reject_a_invalid_scope_expression() {
        try {
            var lhs = new Variable("scope.lhs");
            var rhs = new Variable("scope.rhs");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));

            var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
            modelDefintion.addDefinition("lhs", Types.DECIMAL, null, true, true, 0, null);
            modelDefintion.addDefinition("rhs", Types.TEXT, null, true, true, "a", null);

            new ExpressionTypeChecker(modelDefintion).checkTypes(add);
            fail();
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.DECIMAL, Types.TEXT, String.valueOf(2)).getMessage()));
        }
    }

    @Test
    public void should_accept_mixed_scope_and_model_expression_with_auto_resolve() {
        try {
            var lhs = new Variable("scope.lhs");
            var rhs = new Variable("age");
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));

            var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
            modelDefintion.addDefinition("lhs", Types.INTEGER, null, true, true, 0, null);
            modelDefintion.pushScope(null);

            new ExpressionTypeChecker(modelDefintion).checkTypes(add);
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_reslove_type_of_generic_expression() {
        var condition = new Constant(true);
        var lhs = new Constant("TRUE");
        var rhs = new Constant("FALSE");
        var ternary = new Ternary();
        ternary.getArguments().addAll(Arrays.asList(condition, lhs, rhs));

        var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        modelDefintion.addDefinition("lhs", Types.INTEGER, null, true, true, 0, null);
        modelDefintion.pushScope(null);

        assertThat(new ExpressionTypeChecker(modelDefintion).cachedDetermineReturnType(ternary), is(Types.TEXT));
    }

    @Test
    public void should_reject_wrong_2nd_generic_argument() {
        try {
            var condition = new Constant(true);
            var lhs = new Constant("TRUE");
            var rhs = new Constant(false);
            var ternary = new Ternary();
            ternary.getArguments().addAll(Arrays.asList(condition, lhs, rhs));

            var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
            modelDefintion.addDefinition("lhs", Types.INTEGER, null, true, true, 0, null);
            modelDefintion.pushScope(null);

            new ExpressionTypeChecker(modelDefintion).checkTypes(ternary);

        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.wrongModel(Types.TEXT.getType(), Types.BOOLEAN.getType(), 2).getMessage()));
        }
    }

    @Test
    public void should_determine_return_type_of_formatInteger() {
        var returnType = new ExpressionTypeChecker(TestDefinition.class)
                .getExpressionType(new FormatInteger(Collections.singletonList(new Variable("mentalAge"))));

        assertThat(returnType, is(Types.OPTIONAL_TEXT));
    }

    @Test
    public void should_determine_return_type_of_formatInteger_of_constant() {
        var returnType = new ExpressionTypeChecker(TestDefinition.class)
                .getExpressionType(new FormatInteger(Collections.singletonList(new Constant(1L))));

        assertThat(returnType, is(Types.TEXT));
    }

    @Test
    public void should_downcast_if_both_arguments_are_integer() {
        try {
            var lhs = new Constant(42);
            var rhs = new Constant(37);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(add);

            var returnType = checker.getExpressionType(add);

            assertThat(returnType, is(Types.INTEGER));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }


    @Test
    public void should_not_downcast_if_ony_lhs_is_integer() {
        try {
            var lhs = new Constant(42);
            var rhs = new Constant(37.2);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(add);

            var returnType = checker.getExpressionType(add);

            assertThat(returnType, is(Types.DECIMAL));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void should_not_downcast_if_ony_4hs_is_integer() {
        try {
            var lhs = new Constant(42.0);
            var rhs = new Constant(37);
            var add = new Add();
            add.setArguments(Arrays.asList(lhs, rhs));
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(add);

            var returnType = checker.getExpressionType(add);

            assertThat(returnType, is(Types.DECIMAL));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void case1_bug1() {
        try {
            var expression = new ExpressionParser().parse("nullOrDefault(parseInteger($name),2)+1+2");
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(expression);

            var returnType = checker.getExpressionType(expression);

            assertThat(returnType, is(Types.INTEGER));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }

    @Test
    public void case1_bug2() {
        try {
            var expression = new ExpressionParser().parse("1+2+nullOrDefault(parseInteger($name),2)");
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(expression);

            var returnType = checker.getExpressionType(expression);

            assertThat(returnType, is(Types.INTEGER));
        } catch (TypeCheckException TCe) {
            fail("Should be a valid type");
        }
    }


    @Test
    public void should_not_downcast_optional_if_possible() {
        try {
            var expression = new ExpressionParser().parse("1+parseInteger($name)");
            var checker = new ExpressionTypeChecker(TestDefinition.class);
            checker.checkTypes(expression);

            var returnType = checker.getExpressionType(expression);

            fail("Should not be a valid type");
        } catch (TypeCheckException TCe) {
            System.out.println(TCe.getMessage());
            assertThat(TCe.getMessage(), is(
                    TypeCheckException.typeError(Types.INTEGER, Types.OPTIONAL_INTEGER, "2").getMessage()
            ));
        }
    }

    @Test
    public void case2_bug1() {
//        try {
        var expression = new ExpressionParser().parse("$age==42");
        var checker = new ExpressionTypeChecker(TestDefinition.class);
        checker.checkTypes(expression);

        var returnType = checker.getExpressionType(expression);

        assertThat(returnType, is(Types.BOOLEAN));
//        } catch (TypeCheckException TCe){
//            System.out.println(TCe.getMessage());
//            fail("Should be a valid type");
//        }
    }


}
