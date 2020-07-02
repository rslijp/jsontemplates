package nl.softcause.referenceapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final WorkBenchDatabase database;

    public DatabaseLoader(ActivatedWorkBenchDatabase database) {
        this.database = database;
    }

    private DescribeTemplateLibrary buildLibrary() {
        return new DescribeTemplateLibrary();
    }

    @Override
    public void run(String... strings) throws Exception {
        loadEmpty();
        loadPreloadSingle();
        loadPreloadDouble();
        loadPreloadNested();
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

        database.save("TEST-SINGLE", "http://localhost:8080/commit.html", "http://localhost:8080/cancel.html", buildLibrary(), TestDefinition.class, new INode[]{forNode});
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

        database.save("TEST-DOUBLE", "http://localhost:8080/commit.html", "http://localhost:8080/cancel.html", buildLibrary(), TestDefinition.class, new INode[]{setNode1, setNode2});
    }

    private void loadPreloadNested() {
        var setNode1 = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );
        var setNode2 = Set.create(
                Map.of("path", new Constant("mentalAge"),
                        "value", new Variable("scope.current"))
        );
        var setNodeOther = Set.create(
                Map.of("path", new Constant("name"),
                        "value", new Variable("scope.current"))
        );
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {setNode1})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {setNode2})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {setNodeOther}
                )
        );


        database.save("TEST-NESTED", "http://localhost:8080/commit.html", "http://localhost:8080/cancel.html", buildLibrary(), TestDefinition.class, new INode[]{switchNode});
    }

    private void loadEmpty() {
        database.save("TEST-EMPTY", "http://localhost:8080/commit.html", "http://localhost:8080/cancel.html", buildLibrary(), TestDefinition.class, new INode[]{});
    }
}