package nl.softcause.jsontemplates.types;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TypeSerializer.class)
@JsonDeserialize(using = TypeDeserializer.class)
public interface IExpressionType<T> {
    String getType();

    boolean isA(Object src);

    boolean isClassOfA(Class<?> src);

    T convert(Object src);

    IExpressionType baseType();

    IExpressionType<T> infuse(Class<?> src);
}
