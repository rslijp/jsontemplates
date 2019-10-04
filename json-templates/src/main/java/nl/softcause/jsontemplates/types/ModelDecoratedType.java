package nl.softcause.jsontemplates.types;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

public class ModelDecoratedType<T> implements IExpressionType<T> {

    @Delegate
    @Getter
    private final IExpressionType<T> undecoratedExpressionType;
    @Getter
    private final Class<T> modelType;

    public ModelDecoratedType(@NonNull IExpressionType<T> undecoratedExpressionType, @NonNull Class<T> modelType){
        this.undecoratedExpressionType = undecoratedExpressionType;
        this.modelType = modelType;
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs==null) return false;
        if(rhs instanceof ModelDecoratedType){
            return undecoratedExpressionType.equals(((ModelDecoratedType) rhs).getUndecoratedExpressionType());
        }
        return undecoratedExpressionType.equals(rhs);
    }

    @Override
    public String toString() {
        return undecoratedExpressionType.toString();
    }
}
