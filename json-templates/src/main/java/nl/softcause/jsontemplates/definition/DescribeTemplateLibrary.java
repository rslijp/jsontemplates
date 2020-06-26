package nl.softcause.jsontemplates.definition;

import static nl.softcause.jsontemplates.utils.ClassUtil.listAllExpressions;

import java.util.*;

import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.nodes.controlflowstatement.*;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;

public class DescribeTemplateLibrary implements ILibrary {

    private static final boolean LOG = true;

    private static final Class[] MAIN_NODES =
            new Class[] {For.class, If.class, Set.class, Switch.class, Try.class, While.class};
    private static final Class[] MAIN_EXPRESIONS = listAllExpressions();

    private List<Class> mainNodes = new ArrayList<>(Arrays.asList(MAIN_NODES));
    private Stack<List<Class>> additionalNodes = new Stack<>();
    private List<Class> mainExpressions = new ArrayList<>(Arrays.asList(MAIN_EXPRESIONS));

    public TemplateDescription describe(ITemplateModelDefinition definition) {
        var description = new TemplateDescription();
        new DescribeNodeHelper(mainNodes.toArray(new Class[mainNodes.size()])).describe(description);
        new DescribeExpressionHelper(mainExpressions.toArray(new Class[mainExpressions.size()]))
                .describe(definition, description);
        new DescribeModelHelper(definition).describe(description);
        return description;
    }

    public DescribeTemplateLibrary addMainNodes(Class... nodes) {
        mainNodes.addAll(Arrays.asList(nodes));
        return this;
    }

    public Optional<Class> getNodeClass(String name) {
        var result = findNodeClass(name, mainNodes);
        for (var stack : additionalNodes) {
            if(result.isEmpty()) {
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
