package nl.softcause.jsontemplates.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.*;
import nl.softcause.jsontemplates.nodes.base.MultiNode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.jsontemplates.nodes.types.LimitedSlot;
import nl.softcause.jsontemplates.syntax.TypeCheckException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class NodeTypeCheckerTest {

    @Test
    public void should_pass_type_checker_with_minimal_fields() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(forNode);
    }

    @Test
    public void should_pass_type_checker_with_object_fields() {
        var setNode = Set.create(
                Map.of("path", new Constant("name"),
                        "value", new Constant("Hello world"))
        );

        var definition = new DefinedModel(TestDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(setNode);
    }

    @Test
    public void should_pass_type_checker_with_variable_from_model() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Variable("age")),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(forNode);
    }

    @Test
    public void should_pass_type_checker_with_all_fields() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Constant(0), "step", new Constant(1), "until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(forNode);
    }

    @Test
    public void should_reject_type_checker_with_wrong() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Constant("3")),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);
        try {
            new NodeTypeChecker(model).check(forNode);
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.INTEGER, Types.TEXT, "until").getMessage()));
        }
    }

    @Test
    public void should_reject_type_checker_with_wrong_optional() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Constant("0"), "step", new Constant(1), "until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);
        try {
            new NodeTypeChecker(model).check(forNode);
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(),
                    is(TypeCheckException.typeError(Types.OPTIONAL_INTEGER, Types.TEXT, "start").getMessage()));
        }
    }

    @Test
    public void should_accept_type_checker_with_null_values_for_optional_nullabel_fields() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Constant(null), "step", new Constant(1), "until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(forNode);
    }

    @Test
    public void should_pass_type_checker_with_nested_fields() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(switchNode);
    }

    @Test
    public void should_reject_wrong_nested_fields() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, assertionNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        try {
            new NodeTypeChecker(model).check(switchNode);
        } catch (TypeCheckException TCe) {
            assertThat(TCe.getMessage(), is(TypeCheckException
                    .slotMismatch("case", new LimitedSlot(new Class[] {Switch.Case.class}), assertionNodeSecond)
                    .getMessage()));
        }
    }

    @Test
    public void should_pass_type_checker_with_deeply_nested_fields() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Constant(0), "step", new Constant(1), "until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {forNode})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var definition = new DefinedModel(TestNestedDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(switchNode);
    }

    @Test
    public void should_pass_type_include_nested_scope() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Variable("scope.start"), "step", new Variable("scope.step"), "until",
                        new Variable("age")),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {forNode})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var setStartNode = Set.create(
                Map.of("path", new Constant("scope.start"),
                        "value", new Constant(0))
        );
        var setStepNode = Set.create(
                Map.of("path", new Constant("scope.step"),
                        "value", new Constant(1))
        );
        var mainNode = new MultiNode(new INode[] {setStartNode, setStepNode, switchNode});
        var definition = new DefinedModel(TestDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(mainNode);
    }

    @Test
    public void should_pass_type_include_deeper_nested_scope() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Map.of("start", new Variable("scope.start"), "step", new Variable("scope.step"), "until",
                        new Variable("age")),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var setStepNode = Set.create(
                Map.of("path", new Constant("scope.step"),
                        "value", new Constant(1))
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {setStepNode, forNode})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var setStartNode = Set.create(
                Map.of("path", new Constant("scope.start"),
                        "value", new Constant(0))
        );

        var mainNode = new MultiNode(new INode[] {setStartNode, switchNode});
        var definition = new DefinedModel(TestDefinition.class);
        var model = new TemplateModel<>(definition);

        new NodeTypeChecker(model).check(mainNode);
    }

    @Test
    public void should_fail_type_check_on_to_deep_include_scope_elements() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNode = new AssertionNode();
        var setStepNode = Set.create(
                Map.of("path", new Constant("scope.step"),
                        "value", new Constant(1))
        );

        var forNode = For.create(
                Map.of("start", new Variable("scope.start"), "step", new Variable("scope.step"), "until",
                        new Variable("age")),
                Collections.singletonMap("body", new INode[] {setStepNode, assertionNode})
        );
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );

        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {forNode})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );
        var setStartNode = Set.create(
                Map.of("path", new Constant("scope.start"),
                        "value", new Constant(0))
        );

        var mainNode = new MultiNode(new INode[] {setStartNode, switchNode});
        var definition = new DefinedModel(TestDefinition.class);
        var model = new TemplateModel<>(definition);

        try {
            new NodeTypeChecker(model).check(mainNode);
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(), is(ScopeException.notFound("step").getMessage()));
        }
    }
}
