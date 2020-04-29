package gramat.util;

@FunctionalInterface
public interface BooleanFunction<T> {

    boolean apply(T input);

}
