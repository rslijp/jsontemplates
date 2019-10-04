package nl.softcause.jsontemplates.expressions.util;

public class TupleExpressionException extends RuntimeException {
    TupleExpressionException(String msg) {
        super(msg);
    }

    public static TupleExpressionException notATuple(int length) {
        return new TupleExpressionException(String.format("A tuple must have 2 arguments instead of %d", length));
    }
}
