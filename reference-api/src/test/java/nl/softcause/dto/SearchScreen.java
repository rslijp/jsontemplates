package nl.softcause.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;
import org.springframework.util.StringUtils;

@EqualsAndHashCode(callSuper = true)
public class SearchScreen extends ReflectionBasedNodeWithScopeImpl<SearchScreen.SearchScreenScope> {

    public SearchScreen(){
        super(SearchScreen.SearchScreenScope.class);
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    public static SearchScreen create(String reference, String name, Integer pageSize,
                                      INodeWithParent<SearchScreen>... fields){
        var searchScreen = new SearchScreen();
        Map<String, IExpression>  arguments = new HashMap<>(Map.of(
                "reference", new Constant(reference),
                "name", new Constant(name)
        ));
        if(pageSize!=null){
            arguments.put("pageSize",new Constant(pageSize.longValue()));
        }
        searchScreen.setArguments(arguments);

        var columns = Arrays.stream(fields).filter(c->c instanceof SearchScreen.Column).toArray(INode[]::new);
        var filters = Arrays.stream(fields).filter(c->c instanceof SearchScreen.Filter).toArray(INode[]::new);
        var actions = Arrays.stream(fields).filter(c->c instanceof SearchScreen.Action).toArray(INode[]::new);
        searchScreen.setSlots(Map.of(
                "columns", columns,
                "filters", filters,
                "actions", actions
        ));
        return searchScreen;
    }

    @RequiredArgument
    private String name;

    @RequiredArgument
    private String reference;


    @DefaultValue(value=10L)
    private Long pageSize;

    @RequiredSlot
    @LimitSlots(allowed={nl.softcause.dto.SearchScreen.Column.class})
    private INode columnsNode=null;

    @RequiredSlot
    @LimitSlots(allowed={nl.softcause.dto.SearchScreen.Filter.class})
    private INode filtersNode=null;

    @RequiredSlot
    @LimitSlots(allowed={nl.softcause.dto.SearchScreen.Action.class})
    private INode actionsNode=null;

    @Override
    protected void internalEvaluate(TemplateModel model) {
        var config = (Map<String, ClientSearchScreen>) model.get("searchScreens");
        var searchScreen = config.containsKey(reference) ? config.get(reference) : new ClientSearchScreen();
        searchScreen.setReference(reference);
        searchScreen.setName(name);
        searchScreen.setPageSize(pageSize.intValue());
        pushScopeModel(model, new SearchScreen.SearchScreenScope(searchScreen));
        columnsNode.evaluate(model);
        filtersNode.evaluate(model);
        actionsNode.evaluate(model);
//        model.popScope();
        config.put(reference, searchScreen);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchScreenScope {
        @Getter
        protected ClientSearchScreen searchScreen;
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Column extends ReflectionBasedNodeImpl implements INodeWithParent<SearchScreen> {

        public static Column create(String field,
                                    String label,
                                    ClientSearchScreenColumn.ClientSearchScreenColumnDataType dataType,
                                    ClientSearchScreenColumn.DefaultSortDataType defaultSort) {
            var  column= new Column();
            Map<String, IExpression>  arguments = new HashMap<>(Map.of(
                    "field", new Constant(field),
                    "dataType", new Constant(dataType.name()),
                    "sort", new Constant(defaultSort.name())
            ));
            if(label!=null) {
                arguments.put("label", new Constant(label));
            }
            column.setArguments(arguments);

            return column;
        }

        private SearchScreen parent;

        @Override
        public void registerParent(SearchScreen parent) {
            this.parent=parent;
        }

        @RequiredArgument
        private String field;

        private String label;

        @RequiredArgument
        private ClientSearchScreenColumn.ClientSearchScreenColumnDataType dataType;

        @RequiredArgument
        private ClientSearchScreenColumn.DefaultSortDataType sort;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            var scope = parent.pullScopeModel(model);
            var searchScreen = scope.getSearchScreen();
            var searchScreenColumn = new ClientSearchScreenColumn();

            searchScreenColumn.setField(field);
            searchScreenColumn.setLabel(StringUtils.isEmpty(label)?searchScreen.suggestLabel(field):label);
            searchScreenColumn.setDataType(dataType);
            searchScreenColumn.setSort(sort);
            searchScreen.addColumn(searchScreenColumn);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Filter extends ReflectionBasedNodeImpl implements INodeWithParent<SearchScreen> {


        public static Filter create(String field,
                                    String label,
                                    ClientSearchScreenFilter.ClientSearchScreenFilterType inputType) {
            var  filter= new Filter();
            Map<String, IExpression>  arguments = new HashMap<>(Map.of(
                    "field", new Constant(field),
                    "inputType", new Constant(inputType.name())
            ));
            if(label!=null) {
                arguments.put("label", new Constant(label));
            }
            filter.setArguments(arguments);
            return filter;
        }

        private SearchScreen parent;

        @Override
        public void registerParent(SearchScreen parent) {
            this.parent=parent;
        }

        @RequiredArgument
        private String field;

        private String label;

        @RequiredArgument
        private ClientSearchScreenFilter.ClientSearchScreenFilterType inputType;

        @Override
        protected void internalEvaluate(TemplateModel model) {
            var scope = parent.pullScopeModel(model);
            var searchScreen = scope.getSearchScreen();
            var searchScreenFilter = new ClientSearchScreenFilter();
            searchScreenFilter.setField(field);
            searchScreenFilter.setLabel(StringUtils.isEmpty(label)?searchScreen.suggestLabel(field):label);
            searchScreenFilter.setInputType(inputType);
            searchScreen.addFilter(searchScreenFilter);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Action extends ReflectionBasedNodeImpl implements INodeWithParent<SearchScreen> {

        public static Action create(String dialogName,
                                    String label,
                                    String icon,
                                    ClientSearchScreenAction.ClientSearchScreenColumnActionType actionType) {
            var  action= new Action();
            Map<String, IExpression>  arguments = new HashMap<>(Map.of(
                    "dialogName", new Constant(dialogName),
                    "actionType", new Constant(actionType.name()),
                    "icon", new Constant(icon)
            ));
            if(label!=null) {
                arguments.put("label", new Constant(label));
            }
            action.setArguments(arguments);
            return action;
        }

        private SearchScreen parent;

        @Override
        public void registerParent(SearchScreen parent) {
            this.parent=parent;
        }

        @RequiredArgument
        private String dialogName;

        @RequiredArgument
        private String label;

        @RequiredArgument
        private String actionType;

        @RequiredArgument
        private String icon;

        @Override
        protected void internalEvaluate(TemplateModel model) {
            var scope = parent.pullScopeModel(model);
            var searchScreen = scope.getSearchScreen();
            var searchScreenAction = new ClientSearchScreenAction();
            searchScreenAction.setDialogName(dialogName);
            searchScreenAction.setLabel(StringUtils.isEmpty(label)?searchScreen.suggestLabel(dialogName):label);
            searchScreenAction.setActionType(ClientSearchScreenAction.ClientSearchScreenColumnActionType.valueOf(actionType));
            searchScreenAction.setIcon(icon);
            searchScreen.addAction(searchScreenAction);
        }
    }
}
