package nl.softcause.jsontemplates.nodes.base;

public abstract class ReflectionBasedNodeWithDefaultScopeImpl extends ReflectionBasedNodeWithScopeImpl<ReflectionBasedNodeWithDefaultScopeImpl.DefaultScope> {

    protected ReflectionBasedNodeWithDefaultScopeImpl(){
        super(DefaultScope.class);
    }

    static class DefaultScope{}



}
