package nl.softcause.jsontemplates.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface INodeWithParent<T extends INode> {

    void registerParent(T parent);

    @JsonIgnore
    T getRegisteredParent();

}
