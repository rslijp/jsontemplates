package nl.softcause.jsontemplates.nodes;

import java.util.Arrays;
import java.util.List;

import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.base.NodeUtil;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.syntax.ExpressionTypeChecker;
import nl.softcause.jsontemplates.syntax.TypeCheckException;

public class NodeTypeChecker {
    private final ITemplateModelDefinition modelDefinition;

    public NodeTypeChecker(ITemplateModelDefinition definition) {
        this.modelDefinition = definition;
    }

    public <T> NodeTypeChecker(Class<T> type) {
        this.modelDefinition = new TemplateModel<>(new DefinedModel<>(type));
    }

    public void checkArguments(INode node) {
        var expressionChecker = new ExpressionTypeChecker(modelDefinition);
        node.getArgumentsTypes().forEach((name, definition) -> {
            var expression = node.getArguments().get(name);
            if (expression == null) {
                expression = new Constant(definition.getDefaultValue());
            }
            expressionChecker.checkTypes(expression);
            var expectedType = definition.getType();
            expressionChecker.matchSingleArgumentExpressionType(expression, expectedType, name);

        });
    }

    public void check(List<INode> nodes, ISlotPattern pattern) {
        nodes.stream().forEach(node -> {
            guardPattern("toplevel", pattern, node);
            this.check(node);
        });
    }

    public void check(INode node) {
        if (node == null) {
            return;
        }
        node.registerDefinitions(modelDefinition);
        checkArguments(node);
        node.getSlotTypes().forEach((name, pattern) -> {
            var slotArray = node.getSlots().get(NodeUtil.slotName(name));
            if (slotArray == null || slotArray.length == 0) {
                guardPattern(name, pattern, null);
            } else {
                Arrays.stream(slotArray).forEach(slot -> {
                    guardPattern(name, pattern, slot);
                    check(slot);
                });
            }
        });
        node.revokeDefinitions(modelDefinition);
    }

    private void guardPattern(String name, ISlotPattern pattern, INode singleNode) {
        if (!pattern.match(singleNode)) {
            throw TypeCheckException.slotMismatch(NodeUtil.slotName(name), pattern, singleNode);
        }
    }

}
