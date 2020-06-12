package nl.softcause.jsontemplates.nodes;

public interface INodeWithParent<T> {

    void registerParent(T parent);

}
