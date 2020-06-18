package nl.softcause.jsontemplates.nodes.base;

class ReflectionBasedNodeException extends RuntimeException {

    private ReflectionBasedNodeException(String msg) {
        super(msg);
    }

    static ReflectionBasedNodeException noSuchField(Class node, String fieldName) {
        return new ReflectionBasedNodeException(
                String.format("Field %s not found on node %s", node.getSimpleName(), fieldName));
    }

    static ReflectionBasedNodeException illegalAccessOfArgumentField(Class node, String fieldName) {
        return new ReflectionBasedNodeException(
                String.format("Argument field %s was not accessible on %s", node.getSimpleName(), fieldName));
    }

    static ReflectionBasedNodeException illegalGetAccessOfScopeField(Class node, String fieldName) {
        return new ReflectionBasedNodeException(
                String.format("Scope field %s was not read accessible on %s", node.getSimpleName(), fieldName));
    }

    static ReflectionBasedNodeException illegalSetAccessOfScopeField(Class node, String fieldName) {
        return new ReflectionBasedNodeException(
                String.format("Scope field %s was not write accessible on %s", node.getSimpleName(), fieldName));
    }

    static ReflectionBasedNodeException illegalAccessOfNodeField(Class node, String fieldName) {
        return new ReflectionBasedNodeException(
                String.format("Node field %s was not accessible on %s", node.getSimpleName(), fieldName));
    }

    static ReflectionBasedNodeException scopeMissedDefaultConstructor(Class node) {
        return new ReflectionBasedNodeException(
                String.format("Default constructor of %s is missing", node.getSimpleName()));
    }

    static ReflectionBasedNodeException illegalAccessOfScopeConstructor(Class node) {
        return new ReflectionBasedNodeException(
                String.format("Default constructor was not accessible on %s", node.getSimpleName()));
    }

    static ReflectionBasedNodeException errorInvokingScopeConstructor(Class node, ReflectiveOperationException iAe) {
        return new ReflectionBasedNodeException(
                String.format("Invoking constructor of %s threw an error %s", node.getSimpleName(), iAe.getMessage()));
    }
}
