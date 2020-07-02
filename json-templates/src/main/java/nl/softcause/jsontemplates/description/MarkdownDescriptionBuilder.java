package nl.softcause.jsontemplates.description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import lombok.RequiredArgsConstructor;
import nl.softcause.jsontemplates.expresionparser.ExpressionFormatter;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;

public class MarkdownDescriptionBuilder implements IDescriptionBuilder {
    private boolean blockStyle = false;
    private List<String> builder = new ArrayList<>();
    private Stack<Boolean> indentation = new Stack<>();
    private Stack<Boolean> autoClear = new Stack<>();

    public MarkdownDescriptionBuilder() {
        autoClear.push(false);
    }

    public MarkdownDescriptionBuilder switchToBlockStyle() {
        if (indentation.size() > 0) {
            throw new IllegalStateException("Indention has size greater than 0");
        }
        blockStyle = true;
        return this;
    }

    @Override
    public IDescriptionBuilder describeIfPresent(String line, INode... nodes) {
        if (nodes == null) {
            return this;
        }
        if (nodes.length == 0) {
            return this;
        }
        phrase(line);
        describe(nodes);
        return this;
    }

    @Override
    public IDescriptionBuilder describe(INode... nodes) {
        if (nodes == null || nodes.length == 0) {
            return this;
        }
        autoClear.push(false);
        var last = nodes[nodes.length - 1];
        for (var node : nodes) {
            indentation.push(false);
            node.describe(this);
            indentation.pop();
            var isLast = node == last;
            if (blockStyle) {
                if (indentation.size() == 0 && !isLast) {
                    builder.add("---");
                } else {
                    write("");
                }
            } else if (indentation.size() == 0) {
                builder.add(node != last ? "---" : "");
            }
            if (!isLast) {
                setAutoClear(autoClear, false);
            }
        }
        autoClear.pop();
        setAutoClear(autoClear, true);
        return this;
    }

    private void setAutoClear(Stack<Boolean> autoClear, boolean b) {
        autoClear.pop();
        autoClear.push(b);
    }

    private void write(String... fragments) {
        if (fragments == null) {
            return;
        }
        var sb = new ArrayList<String>();
        if (blockStyle) {
            indentBlockStyle(sb);
        } else {
            indentListStyle(sb);
        }
        sb.addAll(Arrays.asList(fragments));
        builder.add(String.join(" ", sb));
    }

    private void indentBlockStyle(ArrayList<String> sb) {
        for (var i = 0; i < indentation.size(); i++) {
            sb.add(">");
        }
    }

    private void indentListStyle(ArrayList<String> sb) {
        for (var i = 2; i <= indentation.size(); i++) {
            if (i == indentation.size() && !indentation.peek()) {
                setAutoClear(indentation, true);
                sb.add("-");
            } else {
                sb.add(" ");
            }
        }
    }

    @Override
    public IDescriptionPhraseBuilder phrase() {
        if (autoClear.peek()) {
            setAutoClear(autoClear, true);
            if (!blockStyle) {
                write("");
            }
        }
        return new MarkdownPhraseBuilder(this);
    }

    @Override
    public IDescriptionBuilder phrase(String line) {
        return phrase().add(line).end();
    }

    @Override
    public IDescriptionBuilder emptyLine() {
        write("");
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public String asMarkdown() {
        return String.join(System.getProperty("line.separator"), builder);
    }

    @Override
    public String toString() {
        return asMarkdown();
    }

    @RequiredArgsConstructor
    class MarkdownPhraseBuilder implements IDescriptionPhraseBuilder {
        private final MarkdownDescriptionBuilder builder;
        private final List<String> fragments = new ArrayList<>();

        @Override
        public IDescriptionPhraseBuilder add(String text) {
            fragments.add(String.format("**%s**", text));
            return this;
        }

        @Override
        public IDescriptionPhraseBuilder remark(String text) {
            fragments.add(String.format("*%s*", text));
            return this;
        }


        @Override
        public IDescriptionPhraseBuilder expression(IExpression expression) {
            fragments.add(String.format("`%s`", new ExpressionFormatter().format(expression)));
            return this;
        }

        @Override
        public IDescriptionBuilder end() {
            builder.write(fragments.toArray(String[]::new));
            return builder;
        }
    }
}
