package nl.softcause.jsontemplates.types;

import nl.softcause.jsontemplates.expressions.IExpression;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.*;

public class Types {

    public static final List<IExpressionType> ORDERED = new ArrayList<>();
    public static final Map<String, IExpressionType> LOOKUP = new HashMap<>();

    private static final IExpressionType<Object> _GENERIC = new GenericType();
    public static final IExpressionType<Object> OPTIONAL_GENERIC = addLookup(new Optional<>(_GENERIC));
    public static final IExpressionType<Object> GENERIC = addLookup(_GENERIC);
    public static final IExpressionType<List<Object>> LIST_GENERIC = addLookup(new ListOf<>(_GENERIC));
    public static final IExpressionType<Map<String,Object>> MAP_GENERIC = addLookup(new MapOf<>(_GENERIC));

    private static final IExpressionType<Boolean> _BOOLEAN = new BooleanType();
    private static final IExpressionType<Long> _INTEGER = new IntegerType();
    private static final IExpressionType<Double> _DECIMAL = new DecimalType();
    private static final IExpressionType<String> _TEXT = new TextType();
    private static final IExpressionType<Instant> _DATETIME = new DateTimeType();

    public static final IExpressionType<Boolean> OPTIONAL_BOOLEAN = add(new Optional<>(_BOOLEAN));
    public static final IExpressionType<Long> OPTIONAL_INTEGER = add(new Optional<>(_INTEGER));
    public static final IExpressionType<Double> OPTIONAL_DECIMAL = add(new Optional<>(_DECIMAL));
    public static final IExpressionType<String> OPTIONAL_TEXT = add(new Optional<>(_TEXT));
    public static final IExpressionType<Instant> OPTIONAL_DATETIME = add(new Optional<>(_DATETIME));

    public static final IExpressionType<Boolean> BOOLEAN = add(_BOOLEAN);
    public static final IExpressionType<Long> INTEGER = add(_INTEGER);
    public static final IExpressionType<Double> DECIMAL = add(_DECIMAL);
    public static final IExpressionType<String> TEXT = add(_TEXT);
    public static final IExpressionType<Instant> DATETIME = add(_DATETIME);

    public static final IExpressionType<List<Boolean>> LIST_BOOLEAN = add(new ListOf<>(BOOLEAN));
    public static final IExpressionType<List<Long>> LIST_INTEGER = add(new ListOf<>(INTEGER));
    public static final IExpressionType<List<Double>> LIST_DECIMAL = add(new ListOf<>(DECIMAL));
    public static final IExpressionType<List<String>> LIST_TEXT = add(new ListOf<>(TEXT));
    public static final IExpressionType<List<Instant>> LIST_DATETIME = add(new ListOf<>(DATETIME));

    public static final IExpressionType<Map<String,Boolean>> MAP_BOOLEAN = add(new MapOf<>(BOOLEAN));
    public static final IExpressionType<Map<String,Long>> MAP_INTEGER = add(new MapOf<>(INTEGER));
    public static final IExpressionType<Map<String,Double>>  MAP_DECIMAL = add(new MapOf<>(DECIMAL));
    public static final IExpressionType<Map<String,String>> MAP_TEXT = add(new MapOf<>(TEXT));
    public static final IExpressionType<Map<String,Instant>> MAP_DATETIME = add(new MapOf<>(DATETIME));


    public static final IExpressionType<Object> NULL = add(new NullType());
    private static final IExpressionType<Object> _OBJECT = new ObjectType();
    public static final IExpressionType<List<Object>> LIST_OBJECT = add(new ListOf<>(_OBJECT));
    public static final IExpressionType<Map<String,Object>> MAP_OBJECT = add(new MapOf<>(_OBJECT));
    public static final IExpressionType<Object> OPTIONAL_OBJECT = addLookup(new Optional<>(_OBJECT));
    public static final IExpressionType<Object> OBJECT = add(_OBJECT);

    private static IExpressionType[] types = new IExpressionType[]{
        BOOLEAN,
        INTEGER,
        DECIMAL,
        DATETIME,
        TEXT,
        NULL,
        LIST_INTEGER,
        LIST_DECIMAL,
        LIST_TEXT,
        LIST_DATETIME,
        LIST_OBJECT,
        LIST_TEXT,
        MAP_OBJECT,
        OBJECT,
    };

    public static <T> IExpressionType<T> add(IExpressionType<T> type){
        LOOKUP.put(type.getType(), type);
        ORDERED.add(type);
        return type;
    }

    public static <T> IExpressionType<T> addLookup(IExpressionType<T> type){
        LOOKUP.put(type.getType(), type);
        return type;
    }


    //Optional types are not auto guessed

    public static IExpressionType determineConstant(Object src){
        return Arrays.stream(types).
                filter(c->c.isA(src)).
                findFirst().
                get();
    }

    public static IExpressionType determine(Object src){
        if(src==null) return Types.NULL;
        return ORDERED.stream().
                filter(c-> {
                    try {
                        return c.isA(src);
                    } catch(TypeException Te){
                        return false;
                    }
                }).
                findFirst().
                get();
    }

    public static IExpressionType determine(Class<?> clazz) {
        return ORDERED.stream().
                filter(c -> c.isClassOfA(clazz)).
                findFirst().
                get();
    }

    public static IExpressionType byName(String typeName) {
        return LOOKUP.get(typeName);
    }

    public static boolean runtimeTypesMatch(IExpressionType target, IExpressionType candidate){
        if(candidate== Types.NULL){
            return  target instanceof Optional ||
                    target instanceof ListOf ||
                    target instanceof MapOf ||
                    target instanceof ObjectType;
        }

        return typesMatch(target, candidate);
    }

    public static boolean primitiveTypesMatch(Class target, Class candidate) {
        if (target.equals(candidate)) return true;
        if (target.equals(DecimalType.class) && candidate.equals(IntegerType.class)) return true;
        return false;
    }

    public static boolean typesMatch(IExpressionType target, IExpressionType candidate){
        if(target.equals(candidate)) return true;
        if(target.equals(Types.DECIMAL) && candidate.equals(Types.INTEGER)) return true;
        if(target.baseType().equals(Types.GENERIC) && !candidate.baseType().equals(Types.GENERIC)) {
            return typesMatch(GenericType.resolveT(target, candidate), candidate);
        }
        if(target instanceof Optional) {
            if(candidate==Types.NULL) return true;
            return typesMatch(target.baseType(), candidate);
        }
//        if(target.equals(Object.class)) return true;
        return false;
    }

    public static IExpressionType decorate(IExpressionType<?> type, Class<?> modelType) {
        return new ModelDecoratedType(type, modelType);
    }

}
