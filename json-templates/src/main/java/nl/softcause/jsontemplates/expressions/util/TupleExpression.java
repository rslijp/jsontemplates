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
public abstract class TupleExpression<R, F, S> implements ITupleExpression, IExpressionWithArguments {

    //    @JsonIgnore
    protected final IExpressionType<S> rhs;
    //    @JsonIgnore
    protected final IExpressionType<F> lhs;

    @Getter
    private final IExpressionType[] argumentsTypes;

    protected TupleExpression(@NonNull IExpressionType<F> lhs, @NonNull IExpressionType<S> rhs,
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
        return innerEvaluate(
                lhs.convert(getLhsArgument().evaluate(model)),
                rhs.convert(getRhsArgument().evaluate(model))
        );
    }

    @JsonIgnore
    public IExpression getLhsArgument() {
        return getArguments().get(0);
    }

    @JsonIgnore
    public IExpression getRhsArgument() {
        return getArguments().get(1);
    }

    protected abstract R innerEvaluate(F lhs, S rhs);

    protected abstract IExpressionType getReturnType();

    public IExpressionType getReturnType(IModelDefinition model) {
        return getReturnType();
    }

    public ExpressionParseType parseType() {
        return ExpressionParseType.INFIX;
    }


}
