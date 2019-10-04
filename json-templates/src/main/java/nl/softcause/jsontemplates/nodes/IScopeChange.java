package nl.softcause.jsontemplates.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.softcause.jsontemplates.model.NodeScopeChange;

import java.util.List;

public interface IScopeChange {

    @JsonIgnore
    List<NodeScopeChange> getScopeChange();

}
