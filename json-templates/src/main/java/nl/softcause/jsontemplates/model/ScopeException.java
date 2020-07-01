package nl.softcause.jsontemplates.model;

import nl.softcause.jsontemplates.types.IExpressionType;

public class ScopeException extends RuntimeException {

    private ScopeException(String msg) {
        super(msg);
    }

    public static ScopeException notFound(String path) {
        return new ScopeException(String.format("'%s' wasn't found on scope", path));
    }

    public static ScopeException alreadyDefined(String path) {
        return new ScopeException(String.format("'%s' is already found", path));
    }

    public static ScopeException nestedDefinitionNotAllowed(String path) {
        return new ScopeException(String.format("'%s' is a nested definition. It's not allowed", path));
    }

    public static ScopeException notDefined(String path) {
        return new ScopeException(String.format("'%s' isn't found", path));
    }

    public static ScopeException notFoundForOwner(Object owner) {
        return new ScopeException(String.format("No scope found for owner '%s'", owner));
    }

    public static ScopeException notReadable(String path) {
        return new ScopeException(String.format("'%s' isn't readable", path));
    }

    public static ScopeException notWritable(String path) {
        return new ScopeException(String.format("'%s' isn't writable", path));
    }

    public static ScopeException writingInParentScopesNotAllowed(String path) {
        return new ScopeException(String.format("Writing in parent scopes is not allowed path '%s'.", path));
    }

    public static ScopeException defaultValueTypeError(String path, IExpressionType expectedType,
                                                       IExpressionType actualType) {
        return new ScopeException(
                String.format("Default value for '%s' must be of type %s but was %s.", path, expectedType.getType(),
                        actualType.getType()));
    }

    public static ScopeException defaultValueTypeError(String path, Class<?> expectedType, Class<?> actualType) {
        return new ScopeException(String.format("Default value for '%s' must be of type %s but was %s.", path,
                expectedType.getSimpleName(), actualType.getSimpleName()));
    }
}
