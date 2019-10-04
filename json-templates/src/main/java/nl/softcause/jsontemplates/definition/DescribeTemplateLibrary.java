package nl.softcause.jsontemplates.definition;

import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.nodes.controlflowstatement.*;

import static nl.softcause.jsontemplates.utils.ClassUtil.listAllExpressions;

public class DescribeTemplateLibrary {

    private static  final boolean LOG= true;

    private static final Class[] MAIN_NODES = new Class[]{For.class, If.class, Set.class, Switch.class, Try.class, While.class};
    private static final Class[] MAIN_EXPRESIONS = listAllExpressions();

    public TemplateDescription describe(ITemplateModelDefinition definition){
        var description = new TemplateDescription();
        new DescribeNodeHelper(MAIN_NODES).describe(description);
        new DescribeExpressionHelper(MAIN_EXPRESIONS).describe(definition,description);
        new DescribeModelHelper(definition).describe(description);
        return description;
    }


    private void log(String line) {
        if(LOG) System.out.println(line);
    }

}
