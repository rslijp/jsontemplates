package nl.softcause.jsontemplates.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TypeDeserializer extends StdDeserializer<IExpressionType> {

    public TypeDeserializer() {
        this(null);
    }


    public TypeDeserializer(Class<IExpressionType> t) {
        super(t);
    }

    @Override
    public IExpressionType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        var typeName = jsonParser.getText();
        var type = Types.byName(typeName);
        if(type==null){
            throw TypeException.notFound(typeName);
        }
        return type;
    }


}