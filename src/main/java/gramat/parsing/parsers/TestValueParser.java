package gramat.parsing.parsers;

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
                    throw reader.error("expected key");
                }

                reader.skipBlanks();

                if (!reader.pull(':')) {
                    throw reader.error("expected :");
                }

                reader.skipBlanks();

                var value = parse(reader);

                if (value == null) {
                    throw reader.error("expected value");
                }

                map.set(key.getValue(), value);

                reader.skipBlanks();
            }
            while (reader.pull(','));

            if (!reader.pull('}')) {
                throw reader.error("expected ]");
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
                    throw reader.error("expected value");
                }

                list.add(item);

                reader.skipBlanks();
            }
            while (reader.pull(','));

            if (!reader.pull(']')) {
                throw reader.error("expected ]");
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
