package nl.softcause.jsontemplates.model;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import nl.softcause.jsontemplates.types.IExpressionType;

public class TemplateModel<T> implements IModel, ITemplateModelDefinition {


    private static final String SCOPE = "scope";
    private static final String PARENT = "parent";
    public static final String SEPARATOR_CHAR = ".";
    public static final String SEPARATOR = "\\" + SEPARATOR_CHAR;

    private final DefinedModel<T> model;
    private final Stack<ScopeModel> scopes = new Stack<>();

    @Getter
    @Setter
    private Locale locale;

    public TemplateModel(T m) {
        this.model = new DefinedModel<>(m);
        this.locale = Locale.ENGLISH;
        pushScope(this);
    }

    public TemplateModel(DefinedModel<T> model) {
        this.model = model;
        this.locale = Locale.ENGLISH;
        pushScope(this);
    }

    public void load(T subject) {
        this.model.load(subject);
    }

    public T getModel() {
        return this.model.getModel();
    }

    @Override
    public Class getModelType() {
        return model.getModelType();
    }

    public void pushScope(Object owner) {
        scopes.push(new ScopeModel(owner));
    }

    public void popScope() {
        scopes.pop();
    }

    public ScopeModel scope() {
        return scopes.peek();
    }

    public static <T> Collector<T, ?, Stream<T>> reverse() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            Collections.reverse(list);
            return list.stream();
        });
    }


    public Optional<ScopeModel> scope(Object owner) {
        return scopes.stream().collect(reverse()).filter(s -> s.getOwner() == owner).findFirst();
    }

    public int scopeDepth() {
        return scopes.size();
    }

    @Override
    public DefinitionRegistryEntry getDefinition(String name) {
        return resolve(name, true).getDefintion();
    }

    @Override
    public DefinitionRegistryEntry[] getDefinitions() {
        return model.getDefinitions();
    }


    public void addDefinition(String name, IExpressionType type, DefinitionRegistry nested, boolean readeable,
                              boolean writerable, Object defaultValue, Object[] allowedValues) {
        scopes.peek().addDefintion(name, type, nested, readeable, writerable, defaultValue, allowedValues);
    }

    public void dropDefintion(String name) {
        scopes.peek().dropDefintion(name);
    }

    @Override
    public Object get(String name) {
        return resolve(name, true).get();
    }

    @Override
    public void set(String name, Object value) {
        resolve(name, true).set(value);
    }

    private ResolvedModel resolve(@NonNull String path, @SuppressWarnings("SameParameterValue") boolean parentsAllowed) {
        var parts = path.split(SEPARATOR);
        var localName = parts[0];
        if (!(localName.equals(SCOPE) || localName.equals(PARENT))) {
            return new ResolvedModel(model, path);
        }
        var c = new Stack<ScopeModel>();
        c.addAll(scopes);

        var scopeIndex = 0;
        while (parts[scopeIndex].equals(PARENT)) {
            if (!parentsAllowed) {
                throw ScopeException.writingInParentScopesNotAllowed(path);
            }
            c.pop();
            scopeIndex++;
            if (c.isEmpty() || parts.length == scopeIndex) {
                throw TemplateModelException.toManyParents(path);
            }
        }

        if (parts[scopeIndex].equals(SCOPE)) {
            var relativeName = String.join(SEPARATOR_CHAR, Arrays.copyOfRange(parts, scopeIndex + 1, parts.length));
            var base = c.peek();
            ScopeModel resolved = null;
            if (scopeIndex == 0 && parentsAllowed) {
                while (resolved == null && !c.isEmpty()) {
                    if (c.peek().hasDefinition(relativeName)) {
                        resolved = c.peek();
                    }
                    c.pop();
                }
            }
            return new ResolvedModel(resolved != null ? resolved : base, relativeName);
        } else {
            throw TemplateModelException.wrongPath(path);
        }
    }


    @Value
    private class ResolvedModel {
        IModel model;
        String relativeName;

        DefinitionRegistryEntry getDefintion() {
            return model.getDefinition(relativeName);
        }

        Object get() {
            return model.get(relativeName);
        }

        void set(Object value) {
            model.set(relativeName, value);
        }
    }
}
