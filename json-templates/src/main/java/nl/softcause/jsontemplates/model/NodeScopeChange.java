package nl.softcause.jsontemplates.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import nl.softcause.jsontemplates.types.IExpressionType;

@Value
public class NodeScopeChange {
    private String name;
    private IExpressionType type;
    private boolean writable;
    @JsonIgnore
    private Object defaultValue;
    @JsonIgnore
    private Object[] allowedValues;


}
