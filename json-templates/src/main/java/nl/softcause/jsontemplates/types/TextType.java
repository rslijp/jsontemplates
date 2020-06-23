package nl.softcause.jsontemplates.types;

public class TextType implements IExpressionType<String> {

    TextType() {

    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public boolean isA(Object src) {
        if (src == null) {
            return false;
        }
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return src == String.class || src == StringBuffer.class;
    }

    @Override
    public String convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        return src.toString();
    }

    @Override
    public IExpressionType baseType() {
        return this;
    }


    @Override
    public IExpressionType<String> infuse(Class<?> src) {
        return this;
    }

    @Override
    public String toString() {
        return getType();
    }


}
