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
        var actions = Arrays.stream(fields).filter(c->c instanceof SearchScreen.BaseAction).toArray(INode[]::new);
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
    @LimitSlots(allowed = {nl.softcause.dto.SearchScreen.RowAction.class,nl.softcause.dto.SearchScreen.ScreenAction.class,nl.softcause.dto.SearchScreen.Post.class})
    private INode actionsNode = null;

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
    private abstract static class BaseAction extends ReflectionBasedNodeWithScopeImpl<BaseAction.BaseActionScope> implements INodeWithParent<SearchScreen> {

        protected BaseAction() {
            super(BaseAction.BaseActionScope.class);
        }

        @NoArgsConstructor
        @AllArgsConstructor
        public static class BaseActionScope {
            @Getter
            protected ClientSearchScreenAction action;
        }

        void decorate(String dialogName,
                      String label,
                      String icon) {
            Map<String, IExpression> arguments = new HashMap<>(Map.of(
                    "dialogName", new Constant(dialogName),
                    "icon", new Constant(icon)
            ));
            if (label != null) {
                arguments.put("label", new Constant(label));
            }
            setArguments(arguments);
        }

        private SearchScreen parent;

        @Override
        public void registerParent(SearchScreen parent) {
            this.parent = parent;
        }

        @RequiredArgument
        private String dialogName;

        @RequiredArgument
        private String label;

        @RequiredArgument
        private String icon;

        void baseEvaluate(ClientSearchScreenAction.ClientSearchScreenColumnActionType actionType, TemplateModel model) {
            var scope = parent.pullScopeModel(model);
            var searchScreen = scope.getSearchScreen();
            var searchScreenAction = new ClientSearchScreenAction();
            pushScopeModel(model, new BaseAction.BaseActionScope(searchScreenAction));
            searchScreenAction.setDialogName(dialogName);
            searchScreenAction.setLabel(StringUtils.isEmpty(label) ? searchScreen.suggestLabel(dialogName) : label);
            searchScreenAction
                    .setActionType(actionType);
            searchScreenAction.setIcon(icon);
            searchScreen.addAction(searchScreenAction);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    public static class ScreenAction extends BaseAction {

        public static ScreenAction create(String dialogName,
                                          String label,
                                          String icon) {
            var action = new ScreenAction();
            action.decorate(dialogName, label, icon);
            return action;
        }

        @Override
        protected void internalEvaluate(TemplateModel model) {
            baseEvaluate(ClientSearchScreenAction.ClientSearchScreenColumnActionType.SCREEN, model);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    public static class RowAction extends BaseAction {

        public static RowAction create(String dialogName,
                                       String label,
                                       String icon) {
            var action = new RowAction();
            action.decorate(dialogName, label, icon);
            return action;
        }

        @Override
        protected void internalEvaluate(TemplateModel model) {
            baseEvaluate(ClientSearchScreenAction.ClientSearchScreenColumnActionType.ROW, model);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Post extends BaseAction {

        public static Post create(String dialogName,
                                  String label,
                                  String icon, SearchScreen.Post.PostParam... params
        ) {
            var action = new Post();
            action.decorate(dialogName, label, icon);
            action.setSlots(Map.of(
                    "param", params
            ));
            return action;
        }

        @LimitSlots(allowed = {nl.softcause.dto.SearchScreen.Post.PostParam.class})
        private INode paramNode = null;

        @Override
        protected void internalEvaluate(TemplateModel model) {
            baseEvaluate(ClientSearchScreenAction.ClientSearchScreenColumnActionType.ROWPOST, model);
            if(paramNode!=null) paramNode.evaluate(model);
        }

        @EqualsAndHashCode(callSuper = true)
        public static class PostParam extends ReflectionBasedNodeImpl implements INodeWithParent<SearchScreen.Post> {
            public static PostParam create(
                    String key,
                    String value) {
                var param = new PostParam();
                Map<String, IExpression> arguments = new HashMap<>(Map.of(
                        "key", new Constant(key),
                        "value", new Constant(value)
                ));
                param.setArguments(arguments);

                return param;
            }

            private SearchScreen.Post parent;

            @Override
            public void registerParent(SearchScreen.Post parent) {
                this.parent = parent;
            }

            @RequiredArgument
            private String key;

            @RequiredArgument
            private String value;

            @Override
            protected void internalEvaluate(TemplateModel model) {
                var scope = parent.pullScopeModel(model);
                scope.getAction().addParam(key, value);
            }
        }
    }
}
