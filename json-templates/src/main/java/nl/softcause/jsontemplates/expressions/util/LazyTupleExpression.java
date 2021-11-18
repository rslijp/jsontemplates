package nl.softcause.jsontemplates.expressions.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode
public abstract class LazyTupleExpression<R, F, S> implements ITupleExpression, IExpressionWithArguments {

    //    @JsonIgnore
    private final IExpressionType<S> rhs;
    //    @JsonIgnore
    private final IExpressionType<F> lhs;

    @Getter
    private final IExpressionType[] argumentsTypes;

    protected LazyTupleExpression(@NonNull IExpressionType<F> lhs, @NonNull IExpressionType<S> rhs,
                                  @NonNull List<IExpression> arguments) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.argumentsTypes = new IExpressionType[] {lhs, rhs};
        this.arguments = arguments;
    }


    @Getter
    @Setter
    @JsonInclude
    private List<IExpression> arguments;

    @Override
    public Object evaluate(IModel model) {
        if (getArguments().size() != 2) {
            throw TupleExpressionException.notATuple(getArguments().size());
        }
        return innerEvaluate(model);
    }


    @JsonIgnore
    public IExpression getLhsArgument() {
        return getArguments().get(0);
    }

    @JsonIgnore
    public IExpression getRhsArgument() {
        return getArguments().get(1);
    }

    protected F getLhs(IModel model) {
        return lhs.convert(getLhsArgument().evaluate(model));
    }

    protected S getRhs(IModel model) {
        return rhs.convert(getRhsArgument().evaluate(model));
    }

    protected abstract R innerEvaluate(IModel model);

    protected abstract IExpressionType getReturnType();

    public IExpressionType getReturnType(IModelDefinition model) {
        return getReturnType();
    }

    public ExpressionParseType parseType() {
        return ExpressionParseType.INFIX;
    }


}
