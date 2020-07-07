package nl.softcause.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class NodeDTOTest {

    @Test
    public void should_collect_correct_arguments() {
        var searchScreenNode = new SearchScreen();

        var argumentTypes = searchScreenNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Map.of(
                "reference", new ArgumentDefinition(Types.TEXT, null),
                "name", new ArgumentDefinition(Types.TEXT, null),
                "pageSize", new ArgumentDefinition(Types.OPTIONAL_INTEGER, 10L)
        )));
    }

    @Test
    public void should_collect_correct_arguments_actionNode() {
        var actionNode = new SearchScreen.RowAction();

        var argumentTypes = actionNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Map.of(
                "dialogName", new ArgumentDefinition(Types.TEXT, null),
                "label", new ArgumentDefinition(Types.TEXT, null),
                "icon", new ArgumentDefinition(Types.TEXT,  null)
        )));
    }

    @Test
    public void should_collect_correct_arguments_filterNode() {
        var filterNode = new SearchScreen.Filter();

        var argumentTypes = filterNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Map.of(
                "field", new ArgumentDefinition(Types.TEXT, null),
                "label", new ArgumentDefinition(Types.OPTIONAL_TEXT, null),
                "inputType", new ArgumentDefinition(Types.ENUM, null)
        )));
    }

    @Test
    public void should_collect_correct_arguments_columnNode() {
        var columnNode = new SearchScreen.Column();

        var argumentTypes = columnNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Map.of(
                "field", new ArgumentDefinition(Types.TEXT, null),
                "label", new ArgumentDefinition(Types.OPTIONAL_TEXT, null),
                "dataType", new ArgumentDefinition(Types.ENUM, null),
                "sort", new ArgumentDefinition(Types.ENUM, ClientSearchScreenColumn.DefaultSortDataType.NO)
        )));
    }

    @Test
    public void should_map_searchScreen_configuration_correct() {

        var configuration = new ClientTemplateConfiguration();
        var model  =new TemplateModel<>(configuration);

        var definition = model.getDefinition("searchScreens");

        assertThat(definition.getType(), is(Types.MAP_OBJECT));
    }

    @Test
    public void should_set_arguments() {
        var dialogNode = SearchScreen.create(
                "test-1",
                "Test",
                25
        );

        var configuration = new ClientTemplateConfiguration();
        var model  =new TemplateModel<>(configuration);
        dialogNode.evaluate(model);

        assertThat(configuration.getSearchScreens().containsKey("test-1"), is(true));
        var dialog = configuration.getSearchScreens().get("test-1");
        assertThat(dialog.getName(), is("Test"));
        assertThat(dialog.getReference(), is("test-1"));
        assertThat(dialog.getPageSize(), is(25));
    }

    @Test
    public void should_set_child_column_nodes() {
        var columnUserNameNode = SearchScreen.Column.create(
                "userName",
                null,
                ClientSearchScreenColumn.ClientSearchScreenColumnDataType.TEXT,
                ClientSearchScreenColumn.DefaultSortDataType.ASC
        );
        var columnPasswordNode = SearchScreen.Column.create(
                "password",
                null,
                ClientSearchScreenColumn.ClientSearchScreenColumnDataType.TEXT,
                ClientSearchScreenColumn.DefaultSortDataType.NO
        );

        var searchScreenNode = SearchScreen.create(
                "test-1",
                "Test",
                25,
                columnUserNameNode,
                columnPasswordNode
        );

        var configuration = new ClientTemplateConfiguration();
        var model  =new TemplateModel<>(configuration);
        searchScreenNode.evaluate(model);

        assertThat(configuration.getSearchScreens().containsKey("test-1"), is(true));
        var searchScreen = configuration.getSearchScreens().get("test-1");

        var columns = searchScreen.getClientSearchScreenColumns();
        assertThat(columns, notNullValue());
        assertThat(columns.size(), is(2));

        var columnUser = columns.get(0);
        assertThat(columnUser.getField(), is("userName"));
        assertThat(columnUser.getLabel(), is("test-1.userName"));
        assertThat(columnUser.getDataType(), is(ClientSearchScreenColumn.ClientSearchScreenColumnDataType.TEXT));
        assertThat(columnUser.getLocation(), is(1));
        assertThat(columnUser.getSort(), is(ClientSearchScreenColumn.DefaultSortDataType.ASC));

        var columnPassword = columns.get(1);
        assertThat(columnPassword.getField(), is("password"));
        assertThat(columnPassword.getLabel(), is("test-1.password"));
        assertThat(columnPassword.getDataType(), is(ClientSearchScreenColumn.ClientSearchScreenColumnDataType.TEXT));
        assertThat(columnPassword.getLocation(), is(2));
        assertThat(columnPassword.getSort(), is(ClientSearchScreenColumn.DefaultSortDataType.NO));
    }

    @Test
    public void should_set_child_filter_nodes() {
        var filterUserNameNode = SearchScreen.Filter.create(
                "userName",
                null,
                ClientSearchScreenFilter.ClientSearchScreenFilterType.TEXT
        );
        var filterPasswordNode = SearchScreen.Filter.create(
                "password",
                null,
                ClientSearchScreenFilter.ClientSearchScreenFilterType.TEXT
        );

        var searchScreenNode = SearchScreen.create(
                "test-1",
                "Test",
                25,
                filterUserNameNode,
                filterPasswordNode
        );

        var configuration = new ClientTemplateConfiguration();
        var model  =new TemplateModel<>(configuration);
        searchScreenNode.evaluate(model);

        assertThat(configuration.getSearchScreens().containsKey("test-1"), is(true));
        var searchScreen = configuration.getSearchScreens().get("test-1");

        var filters = searchScreen.getClientSearchScreenFilters();
        assertThat(filters, notNullValue());
        assertThat(filters.size(), is(2));

        var filterUser = filters.get(0);
        assertThat(filterUser.getField(), is("userName"));
        assertThat(filterUser.getLabel(), is("test-1.userName"));
        assertThat(filterUser.getInputType(), is(ClientSearchScreenFilter.ClientSearchScreenFilterType.TEXT));
        assertThat(filterUser.getLocation(), is(1));

        var filterPassword = filters.get(1);
        assertThat(filterPassword.getField(), is("password"));
        assertThat(filterPassword.getLabel(), is("test-1.password"));
        assertThat(filterPassword.getInputType(), is(ClientSearchScreenFilter.ClientSearchScreenFilterType.TEXT));
        assertThat(filterPassword.getLocation(), is(2));
    }


    @Test
    public void should_set_child_action_nodes() {
        var actionEditNode = SearchScreen.RowAction.create(
                "test-edit",
                null,
                "edit"
        );
        var actionAddNode = SearchScreen.ScreenAction.create(
                "test-add",
                null,
                "add"
        );

        var searchScreenNode = SearchScreen.create(
                "test-1",
                "Test",
                25,
                actionEditNode,
                actionAddNode
        );

        var configuration = new ClientTemplateConfiguration();
        var model  =new TemplateModel<>(configuration);
        searchScreenNode.evaluate(model);

        assertThat(configuration.getSearchScreens().containsKey("test-1"), is(true));
        var searchScreen = configuration.getSearchScreens().get("test-1");
        var actions = searchScreen.getClientSearchScreenActions();
        assertThat(actions, notNullValue());
        assertThat(actions.size(), is(2));

        var editAction = actions.get(0);
        assertThat(editAction.getDialogName(), is("test-edit"));
        assertThat(editAction.getLabel(), is("test-1.test-edit"));
        assertThat(editAction.getActionType(), is(ClientSearchScreenAction.ClientSearchScreenColumnActionType.ROW));
        assertThat(editAction.getIcon(), is("edit"));
        assertThat(editAction.getLocation(), is(1));

        var addAction = actions.get(1);
        assertThat(addAction.getDialogName(), is("test-add"));
        assertThat(addAction.getLabel(), is("test-1.test-add"));
        assertThat(addAction.getActionType(), is(ClientSearchScreenAction.ClientSearchScreenColumnActionType.SCREEN));
        assertThat(addAction.getIcon(), is("add"));
        assertThat(addAction.getLocation(), is(2));
    }

    private DescribeTemplateLibrary buildLibrary() {
        return new DescribeTemplateLibrary().addMainNodes( SearchScreen.class);
    }

    @Test
    @SneakyThrows
    public void full_example_dto() {
        SearchScreen searchScreenNode = searchLogScreen();
        var template = TemplateDTO.asDTO(new INode[]{searchScreenNode});
        var raw = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(template);
        System.out.println(raw);
        var parsed = new ObjectMapper().readValue(raw, TemplateDTO.class);
        assertThat(parsed, notNullValue());
        assertThat(parsed.asTemplate(buildLibrary()), notNullValue());
    }

    @Test
    @SneakyThrows
    public void full_example_json() {
        SearchScreen searchScreenNode = searchLogScreen();
        var template = new INode[]{searchScreenNode};
        var raw = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(template);
        var parsed = new ObjectMapper().readValue(raw, INode[].class);
        assertThat(parsed, notNullValue());
    }

    private SearchScreen searchLogScreen() {
        return SearchScreen.create(
                "log-entry",
                "log-entry",
                null,

                filter("keyword","TEXT"),
                filter("level","SELECT"),
                filter("at","DATETIME"),

                column("at","TIMESTAMP","DESC"),
                column("level","ICONENUM","NO"),
                column("clientName","TEXT","NO"),
                column("userName","TEXT","NO"),

                rowAction("show-log-entry","ROW","detail"),
                post("edit-client-template","client.template","template",
                        param("commitUrl", "/dialog/confirm-client-template/{uuid}"),
                        param("cancelUrl", "/search/client")),
                screenAction("create-client","create-client.Title", "create")
        );
    }


    private static SearchScreen.Filter filter(String field,
                                              String inputType) {
        return SearchScreen.Filter.create(field, null, ClientSearchScreenFilter.ClientSearchScreenFilterType.valueOf(inputType));
    }

    private static SearchScreen.Column column(String field, String dataType, String sort) {
        return SearchScreen.Column.create(
                field,
                null,
                ClientSearchScreenColumn.ClientSearchScreenColumnDataType.valueOf(dataType),
                ClientSearchScreenColumn.DefaultSortDataType.valueOf(sort)
        );
    }


    private static SearchScreen.RowAction  rowAction(String dialogName, String icon) {
        return rowAction(dialogName, null, icon);
    }

    private static SearchScreen.RowAction  rowAction(String dialogName, String label, String icon){
        return SearchScreen.RowAction.create(
                dialogName,
                label,
                icon
        );
    }

    private static SearchScreen.ScreenAction  screenAction(String dialogName, String icon) {
        return screenAction(dialogName, null, icon);
    }

    private static SearchScreen.ScreenAction  screenAction(String dialogName, String label, String icon){
        return SearchScreen.ScreenAction.create(
                dialogName,
                label,
                icon
        );
    }

    private static SearchScreen.Post post(String dialogName, String label,  String icon,  SearchScreen.Post.PostParam... params) {
        return SearchScreen.Post.create(
                dialogName,
                label,
                icon,
                params
        );
    }

    private static SearchScreen.Post.PostParam param(String key, String value) {
        return SearchScreen.Post.PostParam.create(key, value);
    }


}
