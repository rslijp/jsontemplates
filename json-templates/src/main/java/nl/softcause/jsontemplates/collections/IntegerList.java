package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerList implements List<Integer> {

    @Delegate
    private List<Integer> base = new ArrayList<>();

    public IntegerList(Integer...values){
        base.addAll(Arrays.asList(values));
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs instanceof IntegerList) {
            return base.equals(rhs);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return base.hashCode();
    }

    @Override
    public String toString(){
        return base.toString();
    }
}
