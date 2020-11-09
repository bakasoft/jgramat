package gramat.parsers;

import java.util.*;

public class ParserSource implements Iterable<ValueParser> {

    private final List<ValueParser> parsers;

    private final ValueParser defaultParser;

    public ParserSource() {
        this(listDefaultParsers());
    }

    public ParserSource(List<ValueParser> parsers) {
        this.parsers = new ArrayList<>(parsers);
        defaultParser = new StringParser("default");
    }

    public ValueParser searchParser(String name) {
        if (name == null) {
            return defaultParser;
        }

        for (var parser : parsers) {
            if (Objects.equals(parser.getName(), name)) {
                return parser;
            }
        }
        return null;
    }

    @Override
    public Iterator<ValueParser> iterator() {
        return parsers.iterator();
    }

    public static List<ValueParser> listDefaultParsers() {
        return List.of(
                new IntegerParser("integer"),
                new StringParser("string"),
                new StringParser("default"));
    }

    public ValueParser findParser(String name) {
        var parser = searchParser(name);

        if (parser == null) {
            throw new RuntimeException("SourceParser not found: " + name);
        }

        return parser;
    }
}
