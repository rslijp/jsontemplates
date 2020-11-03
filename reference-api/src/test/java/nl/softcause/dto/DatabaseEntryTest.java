package nl.softcause.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.SneakyThrows;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.definition.TemplateDescription;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.referenceapi.DatabaseLoader;
import nl.softcause.referenceapi.TestDefinition;
import nl.softcause.workbench.DatabaseEntry;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

public class DatabaseEntryTest {

    @SneakyThrows
    @Test
    public void should_serialize_and_deserialize(){
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
        var slots = new INode[]{switchNode};
        var library = DatabaseLoader.buildLibrary();

        var entry = new DatabaseEntry(
                "TEST-NESTED", "http://localhost:8080/commit.html", "http://localhost:8080/cancel.html",
                slots, slots, library, TestDefinition.class);

        var json = new ObjectMapper().writeValueAsString(entry);

        assertThat(json.isEmpty(), is(false));

        var reconstructed = new ObjectMapper().readValue(json, DatabaseEntry.class);

        assertThat(reconstructed, notNullValue());
        assertThat(json, is(new ObjectMapper().writeValueAsString(reconstructed)));

    }

    @Test
    public void should_serialize_simple_description() throws IOException {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);
        var json = new ObjectMapper().writeValueAsString(description);

        assertThat(json, notNullValue()); //one way serialize is enough
    }

    @Test
    public void should_serialize_and_deserialize_simple_library() throws IOException {
        var library = DatabaseLoader.buildLibrary();
        var json = new ObjectMapper().writeValueAsString(library);
        System.out.println(json);
        assertThat(json.isEmpty(), is(false));

        var reconstructed = new ObjectMapper().readValue(json, DescribeTemplateLibrary.class);

        assertThat(reconstructed, notNullValue());
    }

}
