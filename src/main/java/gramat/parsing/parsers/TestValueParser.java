package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.parsing.Reader;
import gramat.parsing.test.TestList;
import gramat.parsing.test.TestMap;
import gramat.parsing.test.TestString;
import gramat.parsing.test.TestValue;

public class TestValueParser {

    public static TestValue parse(Reader reader) {
        if (reader.pull('{')) {
            var map = new TestMap();

            do {
                reader.skipBlanks();

                if (reader.test('}')) {
                    break;
                }

                var key = parseString(reader);

                if (key == null) {
                    throw new TextException("expected key", reader.getLocation());
                }

                reader.skipBlanks();

                if (!reader.pull(':')) {
                    throw new TextException("expected :", reader.getLocation());
                }

                reader.skipBlanks();

                var value = parse(reader);

                if (value == null) {
                    throw new TextException("expected value", reader.getLocation());
                }

                map.set(key.getValue(), value);

                reader.skipBlanks();
            }
            while (reader.pull(','));

            if (!reader.pull('}')) {
                throw new TextException("expected ]", reader.getLocation());
            }

            return map;
        }
        else if (reader.pull('[')) {
            var list = new TestList();

            do {
                reader.skipBlanks();

                if (reader.test(']')) {
                    break;
                }

                var item = parse(reader);

                if (item == null) {
                    throw new TextException("expected value", reader.getLocation());
                }

                list.add(item);

                reader.skipBlanks();
            }
            while (reader.pull(','));

            if (!reader.pull(']')) {
                throw new TextException("expected ]", reader.getLocation());
            }

            return list;
        }

        return parseString(reader);
    }

    private static TestString parseString(Reader reader) {
        var value = reader.readString('`');

        if (value == null) {
            value = reader.readKeyword();
        }

        if (value != null) {
            return new TestString(value);
        }

        return null;
    }

}
