package nl.softcause.jsontemplates.nodes.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.softcause.jsontemplates.types.IAllowedValuesProvider;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValues {
    Class<? extends IAllowedValuesProvider> factory();

    String discriminatorField() default "";
}
