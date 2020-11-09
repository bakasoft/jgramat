package gramat.pipeline.parsing;

import gramat.data.ListData;
import gramat.data.MapData;
import gramat.exceptions.UnexpectedCharException;

import java.util.ArrayList;
import java.util.List;

public interface ValueParser extends StringParser {

    default Object readValue() {
        var value = tryValue();

        if (value == null) { // TODO what about null values?
            throw new RuntimeException();
        }

        return value;
    }

    default Object tryValue() {
        var str = tryString();

        var map = tryMap(str);

        if (map != null) {
            return map;
        }

        var list = tryList(str);

        if (list != null) {
            return list;
        }

        var call = tryCall();

        if (call != null) {
            if (call.expression != null) {
                throw new RuntimeException("unexepcted expression");
            }
            var valueParser = findParser(str);
            var text = loadValue(call.keyword, call.arguments);
            return valueParser.parse(text);
        }

        if (str == null) {
            throw new RuntimeException();
        }

        // check for parsed text
        if (tryToken('(')) {
            var valueParser = findParser(str);
            var text = tryString();
            var result = valueParser.parse(text);

            expectToken(')');

            return result;
        }

        return str;
    }

    default MapData tryMap(String typeHint) {
        if (!tryToken('{')) {
            return null;
        }

        if (tryToken('}')) {
            return new MapData(typeHint);
        }

        var map = new MapData(typeHint);

        while (true) {
            var key = readString();

            expectToken(':');

            var value = readValue();

            if (map.containsKey(key)) {
                throw new RuntimeException();
            }

            map.put(key, value);

            if (tryToken('}')) {
                break;
            }
            else if (!tryToken(',')) {
                throw new UnexpectedCharException(getTape());
            }
        }

        return map;
    }

    default ListData tryList(String typeHint) {
        if (!tryToken('[')) {
            return null;
        }

        if (tryToken(']')) {
            return new ListData(typeHint);
        }

        var list = new ListData(typeHint);

        while (true) {
            var value = readValue();

            list.add(value);

            if (tryToken(']')) {
                break;
            }
            else if (!tryToken(',')) {
                throw new RuntimeException();
            }
        }

        return list;
    }

    default List<Object> readArguments() {
        var values = new ArrayList<>();

        while (true) {
            var value = tryValue();

            if (value == null) {
                break;
            }

            values.add(value);

            if (!tryToken(',')) {
                break;
            }
        }

        return values;
    }

}
