package nl.softcause.jsontemplates.collections;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

public class BeanConverters {

    public static void register() {
        ConvertUtils.register(build(StringList::new, String.class), StringList.class);
        ConvertUtils.register(build(IntegerList::new, Integer.class), IntegerList.class);
        ConvertUtils.register(build(LongList::new, Long.class), LongList.class);
        ConvertUtils.register(build(FloatList::new, Float.class), FloatList.class);
        ConvertUtils.register(build(DoubleList::new, Double.class), DoubleList.class);
        ConvertUtils.register(build(InstantList::new, Instant.class), InstantList.class);
        ConvertUtils.register(build(DateList::new, Date.class), DateList.class);
        ConvertUtils.register(build(BooleanList::new, Boolean.class), BooleanList.class);

    }

    private static Converter build(Supplier<Object> create, Class elementType) {
        return new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object src) {
                if(src == null) {
                    return null;
                }
                var converted = (List) create.get();
                var srcClass = src.getClass();
                if(srcClass.isArray()) {
                    Arrays.stream((Object[]) src).forEach(e-> converted.add(ConvertUtils.convert(e, elementType)));
                }
                if(Iterable.class.isAssignableFrom(srcClass)) {
                    ((List) src).stream().forEach(e-> converted.add(ConvertUtils.convert(e, elementType)));

                }
                return  (T) converted;
            }
        };
    }
}
