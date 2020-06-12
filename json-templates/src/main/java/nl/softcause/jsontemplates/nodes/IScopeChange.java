package nl.softcause.jsontemplates.nodes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.softcause.jsontemplates.model.NodeScopeChange;

public interface IScopeChange {

    @JsonIgnore
    List<NodeScopeChange> getScopeChange();

}
