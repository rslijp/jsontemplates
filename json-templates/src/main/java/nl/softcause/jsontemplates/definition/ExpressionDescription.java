package nl.softcause.jsontemplates.definition;

import lombok.Getter;
import lombok.Setter;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

@Getter
public class ExpressionDescription {

    public static final ExpressionDescription CONSTANT = new ExpressionDescription("constant");
    public static final ExpressionDescription VARIABLE = new ExpressionDescription("variable");

    private ExpressionDescription(String operator){
        this.name=operator;
        this.operator=operator;
        this.special=true;
    }

    public ExpressionDescription(int id, String operator, String name, int priority, IExpressionType returnType, ExpressionParseType parseType){
        this.id=id;
        this.operator=operator;
        this.name=name;
        this.priority=priority;
        this.returnType=returnType.getType();
        this.parseType=parseType.toString();

    }

    private int id;

    private String name;

    private String operator;

    private Integer priority;

    private boolean special;

    private String returnType;

    private List<String> argumentTypes;

    private String parseType;

    void addArgument(IExpressionType argType) {
        if(argumentTypes==null) argumentTypes = new ArrayList<>();
        argumentTypes.add(argType.getType());
    }

    public String toString() {
        if (argumentTypes != null) {
            return String.format("%s as %d: (%d) %s -> %s [%s %s]", name, id, priority, String.join(",", argumentTypes), returnType, parseType, operator);
        } else {
            return String.format("%s as %d: (%d) -> %s [%s %s]", name, id, priority, returnType, parseType, operator);
        }
    }
}
