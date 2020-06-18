package nl.softcause.jsontemplates.syntax;

import nl.softcause.jsontemplates.model.ModelException;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.types.GenericType;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;

public class TypeCheckException extends RuntimeException {
    private TypeCheckException(String msg) {
        super(msg);
    }

    public static TypeCheckException wrongNumberOfArguments(int expected, int actual) {
        return new TypeCheckException(String.format("Expected %d arguments but found %d", expected, actual));
    }

    public static TypeCheckException typeError(IExpressionType expectedType, IExpressionType actualType,
                                               String argumentName) {
        var uxType = expectedType.baseType() == Types.GENERIC ? GenericType.inferT(actualType) : actualType;
        return new TypeCheckException(
                String.format("Expected argument %s to be %s but found %s", argumentName, expectedType.getType(),
                        uxType.getType()));
    }

    public static TypeCheckException from(ModelException me) {
        return new TypeCheckException(me.getMessage());
    }

    public static TypeCheckException from(TypeException te) {
        return new TypeCheckException(te.getMessage());
    }

    public static TypeCheckException wrongModel(String expectedModelType, String actualModelType, int argumentIndex) {
        return new TypeCheckException(
                String.format("Expected argument %d to be model %s but found %s", argumentIndex, expectedModelType,
                        actualModelType));
    }

    public static TypeCheckException slotMismatch(String name, ISlotPattern pattern, INode actual) {
        return new TypeCheckException(
                String.format("Slot '%s' only accepts '%s' but found '%s'", name, pattern.getDescription(),
                        actual.getClass().getSimpleName()));
    }

}
