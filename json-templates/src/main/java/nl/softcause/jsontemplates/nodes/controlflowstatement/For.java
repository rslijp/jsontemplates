package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TemplateModelException;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

@EqualsAndHashCode(callSuper = true)
public class For extends ReflectionBasedNodeWithScopeImpl<For.ForScope> {

    public static For create(Map<String, IExpression> arguments,
                             Map<String, INode[]> slots) {
        var node = new For();
        node.setArguments(arguments);
        node.setSlots(slots);
        return node;
    }

    public For() {
        super(ForScope.class);
    }

    @DefaultValue(value = "0")
    private Long start;
    @DefaultValue(value = "1")
    private Long step;
    @RequiredArgument
    private Long until = null;

    @RequiredSlot
    private INode bodyNode = null;


    @Override
    public void internalEvaluate(TemplateModel model) {
        guardParameters(start, step, until);
        for (var i = start; i < until; i += step) {
            pushScopeModel(model, new ForScope(i, i == start, i >= until - step));
            bodyNode.evaluate(model);
        }
    }

    private void guardParameters(Long start, Long step, Long end) {
        if (step == 0) {
            throw stepZeroException();
        } else if (step > 0 && start > end) {
            throw countUpBoundaryException(start, end);
        } else if (step < 0 && end > start) {
            throw countDownBoundaryException(start, end);
        }
    }

    private static TemplateModelException stepZeroException() {
        return new TemplateModelException("Step can't be 0");
    }

    private static TemplateModelException countUpBoundaryException(Long start, Long end) {
        return new TemplateModelException(
                String.format("Case counting up(step>0) start value(%d) must be less then the end value(%d)", start,
                        end));
    }

    private static TemplateModelException countDownBoundaryException(Long start, Long end) {
        return new TemplateModelException(
                String.format("Case counting down(step<0) start value(%d) must be greater then the end value(%d)",
                        start, end));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ForScope {
        protected long current = 0;
        protected boolean first = true;
        protected boolean last = false;
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase()
                .add("For").
                expression(new Variable("i")).
                add("is").
                expression(getArguments().get("start")).
                add("to").
                expression(getArguments().get("until")).
                add("in steps of").
                expression(getArguments().get("step")).
                end();
        builder.phrase("do");
        builder.describe(getSlots().get("body"));
    }
}
