package gramat.engine.parsers;

@FunctionalInterface
public interface ValueParser {

    Object parse(String value);

}
