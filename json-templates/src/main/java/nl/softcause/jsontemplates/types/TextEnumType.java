package nl.softcause.jsontemplates.types;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class TextEnumType implements IExpressionType<Enum> {

    private final Class<?> enumClass;

    TextEnumType() {
        enumClass=null;
    }

    TextEnumType(Class<?> enumClass) {
        if(!enumClass.isEnum()) throw new IllegalArgumentException("Provided class should be an enum");
        this.enumClass=enumClass;
    }

    @Override
    public String getType() {
        return "enum";
    }

    @Override
    public boolean isA(Object src) {
        if (src == null) {
            return false;
        }
        if(src instanceof String && enumClass!=null){
            var values=getEnumValues(enumClass);
            return values.contains(src);

        }
        return isClassOfA(src.getClass());
    }

    @SneakyThrows
    public static List<String> getEnumValues(Class<?> enumClass) {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        var o = (Object[]) f.get(null);
        return Arrays.stream(o).map(Object::toString).collect(Collectors.toList());
    }

    @SneakyThrows
    private static Enum getEnumValue(Class<?> enumClass, String src) {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        var o = (Object[]) f.get(null);
        return (Enum) Arrays.stream(o).filter(i->i.toString().equals(src)).findFirst().orElse(null);
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        if(enumClass!=null) return src.equals(String.class) || src.equals(enumClass);
        return src.isEnum();
    }

    @Override
    public Enum convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        if(src instanceof String && enumClass!=null){
            return getEnumValue(enumClass, src.toString());
        }
        return (Enum) src;
    }

    @Override
    public IExpressionType baseType() {
        return this;
    }


    @Override
    public IExpressionType<Enum> infuse(Class<?> src) {
        return new TextEnumType(src);
    }

    @Override
    public String toString() {
        return getType();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TextEnumType;
    }
}
