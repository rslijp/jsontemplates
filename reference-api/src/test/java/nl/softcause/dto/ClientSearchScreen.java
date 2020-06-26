package nl.softcause.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(value = {"client", "clientSearchScreenFilters", "clientSearchScreenColumns",
        "clientSearchScreenActions"})
public class ClientSearchScreen {

    public ClientSearchScreen(){
        clientSearchScreenFilters=new ClientSearchScreen.ClientSearchScreenFilterSet();
        clientSearchScreenColumns=new ClientSearchScreen.ClientSearchScreenColumnSet();
        clientSearchScreenActions=new ClientSearchScreen.ClientSearchScreenActionSet();
    }

    @NotNull
    @NotBlank
    @EqualsAndHashCode.Include
    private String name;

    @NotNull
    @NotBlank
    @EqualsAndHashCode.Include
    private String reference;

    @Max(500)
    private Integer pageSize;

    private ClientSearchScreenFilterSet clientSearchScreenFilters;

    private ClientSearchScreenColumnSet clientSearchScreenColumns;

    private ClientSearchScreenActionSet  clientSearchScreenActions;

    public String suggestLabel(String field) {
        return getReference()+"."+field;
    }

    public void addColumn(ClientSearchScreenColumn searchScreenColumn) {
        getClientSearchScreenColumns().add(searchScreenColumn);
        searchScreenColumn.setLocation(getClientSearchScreenColumns().size());
    }

    public void addFilter(ClientSearchScreenFilter searchScreenFilter) {
        getClientSearchScreenFilters().add(searchScreenFilter);
        searchScreenFilter.setLocation(getClientSearchScreenFilters().size());
    }

    public void addAction(ClientSearchScreenAction searchScreenAction) {
        getClientSearchScreenActions().add(searchScreenAction);
        searchScreenAction.setLocation(getClientSearchScreenActions().size());
    }

    public static class ClientSearchScreenFilterSet implements List<ClientSearchScreenFilter> {
        @Delegate
        private List<ClientSearchScreenFilter> base = new ArrayList<>();
    }

    public static class ClientSearchScreenColumnSet implements Set<ClientSearchScreenColumn> {
        @Delegate
        private List<ClientSearchScreenColumn> base = new ArrayList<>();
    }


    public static class ClientSearchScreenActionSet implements Set<ClientSearchScreenAction> {
        @Delegate
        private List<ClientSearchScreenAction> base = new ArrayList<>();
    }
}
