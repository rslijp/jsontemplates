package nl.softcause.jsontemplates.model;

import nl.softcause.jsontemplates.types.IExpressionType;

public class ModelException extends RuntimeException {

    private ModelException(String msg) {
        super(msg);
    }

    private ModelException(String msg, Exception e) {
        super(msg, e);
    }

    public static ModelException notFound(String path, Class<?> type) {
        return new ModelException(String.format("'%s' wasn't found on %s", path, type.getSimpleName()));
    }

    static ModelException primitiveCantHaveProperty(IExpressionType type, String property, String fullPath) {
        return new ModelException(
                String.format("Primitive object(%s) can't have property '%s'. Part of accessing ('%s')", type.getType(),
                        property, fullPath));
    }

    static ModelException notReadeable(String path, Class<?> type) {
        return new ModelException(String.format("'%s' is not readable on %s", path, type.getSimpleName()));
    }

    static ModelException notWriteable(String path, Class<?> type) {
        return new ModelException(String.format("'%s' is not readable on %s", path, type.getSimpleName()));
    }

    static ModelException noAccess(String path, Class<?> type) {
        return new ModelException(String.format("Can't access '%s' on %s", path, type.getSimpleName()));
    }

    static ModelException invocationErrorDuringGet(String path, Class<?> type, Exception ex) {
        return new ModelException(
                String.format("Error invoking '%s' on %s. Error %s", path, type.getSimpleName(), ex.getMessage()), ex);
    }

    static ModelException invocationErrorDuringSet(String path, Class<?> type, Exception ex) {
        return new ModelException(
                String.format("Error invoking '%s' on %s. Error %s", path, type.getSimpleName(), ex.getMessage()), ex);
    }


    public static ModelException noSuchGetMethod(String path, Class<?> type) {
        return new ModelException(String.format("No getter for '%s' found on %s", path, type.getSimpleName()));
    }

    public static ModelException noModelLoaded() {
        return new ModelException("No model loaded");
    }

    public static ModelException nestedNullModel(String name) {
        return new ModelException(String.format("Can't get %s there is a nested null value.", name));
    }

    public static ModelException wrongType(String path, IExpressionType expectedType, IExpressionType actualType) {
        return new ModelException(String.format("Expected value of type %s but found %s for %s", expectedType.getType(),
                actualType.getType(), path));
    }

    public static ModelException wrongType(String path, Class<?> expectedType, Class<?> actualType) {
        return new ModelException(
                String.format("Expected value of type %s but found %s for %s", expectedType.getSimpleName(),
                        actualType.getSimpleName(), path));
    }

}
