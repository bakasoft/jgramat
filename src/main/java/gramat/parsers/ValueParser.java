package gramat.parsers;

@FunctionalInterface
public interface ValueParser {

    Object parse(String value);

}
