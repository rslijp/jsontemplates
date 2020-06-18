package nl.softcause.jsontemplates.types;

public class TypeException extends RuntimeException {
    protected TypeException(String msg) {
        super(msg);
    }

    public static TypeException invalidCast(Object src, IExpressionType type) {
        return new TypeException(String.format("%s is not an %s", src, type.getType()));
    }

    public static TypeException notFound(String typeName) {
        return new TypeException(String.format("%s is not an known type", typeName));
    }

    public static TypeException notComparable(Class<?> subject) {
        return new TypeException(String.format("%s is not comparable", subject.getSimpleName()));
    }

    public static TypeException propertyAccess(String property, Class<?> subject) {
        return new TypeException(String.format("Error accessing %s on %s", property, subject.getSimpleName()));
    }

    public static TypeException onProperty(TypeException ex, Class subject, String property) {
        return new TypeException(String.format("%s on property %s.%s", ex, subject.getSimpleName(), property));
    }

    public static TypeException firstClassCollectionOnly(Class<?> clazz) {
        return new TypeException(String.format(
                "Type erasure prevents type safety for the json template. Please use the following construct MyElement%s implements %s<MyElement>",
                clazz.getSimpleName(), clazz.getSimpleName()));
    }


    public static TypeException expected(IExpressionType type) {
        return new TypeException(String.format("Expeted something that could be interpreted as a %s", type.getType()));
    }

    public static TypeException firstClassMapOnly(Class<?> clazz) {
        return new TypeException(String.format(
                "Type erasure prevents type safety for the json template. Please use the following construct MyElement%s implements %s<String,MyElement>",
                clazz.getSimpleName(), clazz.getSimpleName()));
    }

    public static TypeException onlyMapWithStringKeysSupported(Class<?> clazz) {
        return new TypeException(
                String.format("Please use a map with a key type of type String for class %s", clazz.getSimpleName()));
    }

    public static TypeException noModelDefinition(String name) {
        return new TypeException(String.format("No model present to determine type of variable '%s'", name));
    }

    public static TypeException conversionError(Object value, IExpressionType target) {
        return new TypeException(String.format("Can't convert '%s' to %s", value, target.getType()));
    }
}
