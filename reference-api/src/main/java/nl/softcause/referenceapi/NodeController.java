package nl.softcause.referenceapi;


import lombok.Getter;
import lombok.Setter;
import lombok.var;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import nl.softcause.jsontemplates.collections.StringList;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.definition.TemplateDescription;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.nl.softcause.dialogs.Dialog;
import nl.softcause.wizard.Wizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/node")
public class NodeController {

    public class TestDefinition {

//        public static class TestNestedDefinitionList implements List<TestNestedDefinition> {
//            @Delegate
//            private List<TestNestedDefinition> base = new ArrayList<>();
//        }
//
//        static class TestNestedDefinitionMap implements Map<String,TestNestedDefinition> {
//            @Delegate
//            private Map<String,TestNestedDefinition> base = new HashMap<>();
//        }

        @Setter
        @Getter
        String name;

        @Getter
        String nameGet;

        @Setter
        String nameSet;

        @Setter
        @Getter
        int age;

        @Setter
        @Getter
        Integer mentalAge;

        @Setter
        @Getter
        IntegerList magicNumbers;

        @Setter
        @Getter
        StringList titles;

        @Setter
        @Getter
        String[] certificates;

//        @Setter
//        @Getter
//        TestNestedDefinitionList other;
//
//        @Setter
//        @Getter
//        TestNestedDefinitionMap map;

        @Setter
        @Getter
        IntegerMap moreMagic;
    }

    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    @GetMapping(path = "/main")
    public @ResponseBody TemplateDescription describe(){
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        return new DescribeTemplateLibrary().addMainNodes(Wizard.class, Dialog.class).describe(modelDefinition);
    }

}