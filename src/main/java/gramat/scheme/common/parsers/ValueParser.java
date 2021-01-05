package gramat.scheme.common.parsers;

public interface ValueParser {

    Object parse(String value);

    String getName();

}