package nl.softcause.jsontemplates.nodes;

import nl.softcause.jsontemplates.expressions.IExpression;

public interface IDescriptionBuilder {

    IDescriptionBuilder describeIfPresent(String line, INode... nodes);

    IDescriptionBuilder describe(INode... nodes);

    IDescriptionPhraseBuilder phrase();

    IDescriptionBuilder phrase(String line);

    IDescriptionBuilder emptyLine();

    public interface IDescriptionPhraseBuilder {
        IDescriptionPhraseBuilder add(String text);

        IDescriptionPhraseBuilder remark(String text);

        IDescriptionPhraseBuilder expression(IExpression text);

        IDescriptionBuilder end();
    }

}
