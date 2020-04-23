package gramat.compiling;

@FunctionalInterface
public interface ValueParser {

    Object parse(String value);

}
