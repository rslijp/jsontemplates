package nl.softcause.jsontemplates.model;

public class TemplateModelException extends RuntimeException {

    public TemplateModelException(String msg) {
        super(msg);
    }

    public static TemplateModelException wrongPath(String name) {
        return new TemplateModelException(String.format("%s isn't valid path", name));
    }

    public static TemplateModelException toManyParents(String name) {
        return new TemplateModelException(String.format("%s too manyh parents", name));
    }
}
