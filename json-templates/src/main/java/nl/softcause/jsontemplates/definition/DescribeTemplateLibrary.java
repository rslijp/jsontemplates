package nl.softcause.jsontemplates.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Try;
import nl.softcause.jsontemplates.nodes.controlflowstatement.While;

@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class DescribeTemplateLibrary implements ILibrary, Serializable {

    private static final boolean LOG = true;

    private static final Class[] MAIN_NODES =
            new Class[] {For.class, If.class, Set.class, Switch.class, Try.class, While.class};

    @Getter
    private List<Class> mainNodes = new ArrayList<>(Arrays.asList(MAIN_NODES));

    @Getter
    private Stack<List<Class>> additionalNodes = new Stack<>();

    @Getter
    private List<Class> mainExpressions = new ArrayList<>(Arrays.asList(ExpressionParser.DEFAULT_EXPRESIONS));

    public TemplateDescription describe(ITemplateModelDefinition definition) {
        var description = new TemplateDescription();
        new DescribeNodeHelper(mainNodes.toArray(new Class[mainNodes.size()])).describe(description);
        new DescribeExpressionHelper(mainExpressions.toArray(new Class[mainExpressions.size()]))
                .describe(definition, description);
        new DescribeModelHelper(definition).describe(description);
        return description;
    }

    public DescribeTemplateLibrary addMainNodes(Class... nodes) {
        mainNodes.addAll(List.of(nodes));
        return this;
    }

    @SuppressWarnings("unused")
    public DescribeTemplateLibrary addExpressions(Class... expressions) {
        mainExpressions.addAll(List.of(expressions));
        return this;
    }

    public Optional<Class> getNodeClass(String name) {
        var result = findNodeClass(name, mainNodes);
        for (var stack : additionalNodes) {
            if (result.isEmpty()) {
                result = findNodeClass(name, stack);
            }
        }
        return result;
    }

    private Optional<Class> findNodeClass(String name, List<Class> mainNodes) {
        return mainNodes.stream().filter(c -> c.getSimpleName().equals(name)).findFirst();
    }

    @Override
    public void push(Class[] limit) {
        additionalNodes.push(Arrays.asList(limit));
    }

    @Override
    public void pop() {
        additionalNodes.pop();
    }

    private void log(String line) {
        if (LOG) {
            System.out.println(line);
        }
    }
}
