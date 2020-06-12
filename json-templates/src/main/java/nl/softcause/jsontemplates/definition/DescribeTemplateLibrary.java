package nl.softcause.jsontemplates.definition;

import static nl.softcause.jsontemplates.utils.ClassUtil.listAllExpressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.nodes.controlflowstatement.*;

public class DescribeTemplateLibrary {

    private static final boolean LOG = true;

    private static final Class[] MAIN_NODES =
            new Class[] {For.class, If.class, Set.class, Switch.class, Try.class, While.class};
    private static final Class[] MAIN_EXPRESIONS = listAllExpressions();

    private List<Class> mainNodes = new ArrayList<>(Arrays.asList(MAIN_NODES));
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

    private void log(String line) {
        if (LOG) {
            System.out.println(line);
        }
    }

}
