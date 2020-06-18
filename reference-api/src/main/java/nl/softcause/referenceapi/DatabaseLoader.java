package nl.softcause.referenceapi;

//import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import nl.softcause.dialogs.Dialog;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.types.Types;
import nl.softcause.wizard.Wizard;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final WorkBenchDatabase database;

    public DatabaseLoader(WorkBenchDatabase database) {
        this.database = database;
    }

    private DescribeTemplateLibrary buildLibrary() {
        return new DescribeTemplateLibrary().addMainNodes(Wizard.class, Dialog.class);
    }

    //    private final EmployeeRepository repository;
//
//    @Autowired
//    public DatabaseLoader(EmployeeRepository repository) {
//        this.repository = repository;
//    }
//
    @Override
    public void run(String... strings) throws Exception {
        loadEmpty();
        loadPreloadSingle();
        loadPreloadDouble();
        loadPreloadWizard();
    }

    private void loadPreloadSingle() {
        var setNode = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );

        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var ifNode = If.create(
                Collections.singletonMap("test", testExpression),
                Collections.singletonMap("then", new INode[] {setNode})
        );

        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {ifNode})
        );

        database.save("TEST-SINGLE", buildLibrary(), TestDefinition.class, new INode[]{forNode});
    }

    private void loadPreloadDouble() {
        var setNode1 = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );
        var setNode2 = Set.create(
                Map.of("path", new Constant("mentalAge"),
                        "value", new Variable("scope.current"))
        );

        database.save("TEST-DOUBLE", buildLibrary(), TestDefinition.class, new INode[]{setNode1, setNode2});
    }

    private void loadPreloadWizard() {
        var steps = new Wizard.Step[]{
                Wizard.Step.create(new Constant(true), Dialog.create(
                    Dialog.Field.create("Initials", Types.TEXT, false),
                    Dialog.Field.create("FirstName", Types.TEXT, true),
                    Dialog.Field.create("LastName", Types.TEXT, true)
                )),
                Wizard.Step.create(new Constant(true), Dialog.create(
                        Dialog.Field.create("Developer", Types.BOOLEAN, true)
                )),
                Wizard.Step.create(new ExpressionParser().parse("$developer == true"), Dialog.create(
                        Dialog.Field.create("Developer", Types.BOOLEAN, true)
                ))
        };
        var wizard = Wizard.create(
                steps
        );


        database.save("TEST-WIZARD", buildLibrary(), TestDefinition.class, new INode[]{wizard});
    }

    private void loadEmpty() {
        database.save("TEST-EMPTY", buildLibrary(), TestDefinition.class, new INode[]{});
    }
}