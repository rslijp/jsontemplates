package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringList implements List<String> {

    @Delegate
    private List<String> base = new ArrayList<>();

    public StringList(String...values){
        base.addAll(Arrays.asList(values));
    }
}
