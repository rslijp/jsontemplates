package nl.softcause.referenceapi;

import java.util.*;
import lombok.Getter;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.AllowedValues;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.jsontemplates.types.AllowedValueSets;
import nl.softcause.jsontemplates.types.IAllowedValuesProvider;
import nl.softcause.jsontemplates.types.StaticValuesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final WorkBenchDatabase database;

    public DatabaseLoader(ActivatedWorkBenchDatabase database) {
        this.database = database;
    }

    private DescribeTemplateLibrary buildLibrary() {
        return new DescribeTemplateLibrary().
                addMainNodes(Log.class, LogWithEnum.class, LogWithContext.class, LogParentNode.class, LogWithContextFromParent.class);
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
                Map.of("path", new Constant("myAge"),
                        "value", new Variable("age"))
        );
        var setNode2 = Set.create(
                Map.of("path", new Constant("myMentalAge"),
                        "value", new Variable("mentalAge"))
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

    @SuppressWarnings("unused")
    public static class LogLevelProvider extends StaticValuesProvider {

        @SuppressWarnings("unused")
        public LogLevelProvider() {
            super("info", "warn", "error");
        }
    }

    public static class LogLevelProviderWithContext implements IAllowedValuesProvider {

        @Override
        public List<Object> valuesFor(String discriminator) {
            var set = allValues().
                      stream().
                      filter(v->v.match(discriminator)).
                      findFirst().
                      orElse( new AllowedValueSets( null, List.of("debug", "info", "warn", "error")));
            return set.getValues();
        }


        @Override
        public List<AllowedValueSets> allValues() {
            return List.of(
                    new AllowedValueSets( null, List.of("debug", "info", "warn", "error")),
                    new AllowedValueSets( "true", List.of( "warn", "error")),
                    new AllowedValueSets( "false", List.of("debug", "info")));
        }

    }

    public static class Log extends ReflectionBasedNodeImpl {

        @IgnoreArgument
        private static final Logger logger = LoggerFactory.getLogger(Log.class);

        @SuppressWarnings("unused")
        @RequiredArgument
        @AllowedValues(factory = LogLevelProvider.class)
        private String level;

        @RequiredArgument
        private String message;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            if(level.equals("debug")) logger.debug(message);
            else if(level.equals("info")) logger.info(message);
            else if (level.equals("warn")) logger.warn(message);
            else logger.error(message);
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().expression(getArguments().get("level")).
                    expression(getArguments().get("message")).end();
        }
    }

    public static class LogWithEnum extends ReflectionBasedNodeImpl {

        @IgnoreArgument
        private static final Logger logger = LoggerFactory.getLogger(Log.class);

        @SuppressWarnings("unused")
        @RequiredArgument
        private LogLevel level;

        @RequiredArgument
        private String message;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            if(level==LogLevel.INFO) logger.info(message);
            else if (level==LogLevel.WARN) logger.warn(message);
            else logger.error(message);
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().expression(getArguments().get("level")).
                    expression(getArguments().get("message")).end();
        }
    }

    public static class LogWithContext extends ReflectionBasedNodeImpl {

        @IgnoreArgument
        private static final Logger logger = LoggerFactory.getLogger(Log.class);

        @SuppressWarnings("unused")
        private boolean error;

        @SuppressWarnings("unused")
        @RequiredArgument
        @AllowedValues(factory = LogLevelProviderWithContext.class, discriminatorField = "error")
        private String level;

        @RequiredArgument
        private String message;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            if(level.equals("debug")) logger.debug(message);
            else if(level.equals("info")) logger.info(message);
            else if (level.equals("warn")) logger.warn(message);
            else logger.error(message);
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().expression(getArguments().get("level")).
                    expression(getArguments().get("message")).end();
        }
    }

    public static class LogParentNode extends
            ReflectionBasedNodeImpl {

        public static LogParentNode create(boolean error, INode body) {
            var node = new LogParentNode();
            node.setArguments(Collections.singletonMap("error", new Constant(error)));
            node.setSlots(Collections.singletonMap("body", new INode[]{body}));
            return node;
        }

        @RequiredArgument
        private boolean error;

        @RequiredSlot
        private INode bodyNode = null;


        @Override
        public void internalEvaluate(TemplateModel model) {
            bodyNode.evaluate(model);
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase("Test");
        }
    }

    public static class LogWithContextFromParent extends ReflectionBasedNodeImpl implements INodeWithParent<LogParentNode> {

        private LogParentNode parent;

        public static LogWithContextFromParent create(String level) {
            var node = new LogWithContextFromParent();
            node.setArguments(Collections.singletonMap("level",new Constant(level)));
            node.setSlots(new HashMap<>());
            return node;
        }

        @SuppressWarnings("unused")
        @AllowedValues(factory = LogLevelProviderWithContext.class, discriminatorField = "parent.error")
        private String level;
        @Getter
        private String output;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            output=level;
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().add("Value is").expression(getArguments().get("value")).end();
        }

        @Override
        public void registerParent(LogParentNode parent) {
            this.parent=parent;
        }

        @Override
        public LogParentNode getRegisteredParent() {
            return this.parent;
        }
    }
}