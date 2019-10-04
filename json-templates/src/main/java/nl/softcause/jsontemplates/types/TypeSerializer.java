package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TypeSerializer extends StdSerializer<IExpressionType> {

    public TypeSerializer() {
        this(null);
    }

    public TypeSerializer(Class<IExpressionType> t) {
        super(t);
    }

    @Override
    public void serialize(IExpressionType expressionType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(expressionType.getType());
    }

}