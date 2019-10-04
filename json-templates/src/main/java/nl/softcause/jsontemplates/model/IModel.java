package nl.softcause.jsontemplates.model;

import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.Locale;

public interface IModel extends IModelDefinition{

    Object get(String name);

    void set(String name, Object value);

    Locale getLocale();

}
