package gramat.parsers;

public interface ValueParser {

    Object parse(String value);

    String getName();

}