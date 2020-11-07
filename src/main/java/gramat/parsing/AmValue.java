package gramat.parsing;

import gramat.data.ListData;
import gramat.data.MapData;
import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;
import gramat.util.Args;

import java.util.ArrayList;
import java.util.List;

public interface AmValue extends AmString {

    default Object readValue(Parser parser) {
        var value = tryValue(parser);

        if (value == null) { // TODO what about null values?
            throw new RuntimeException();
        }

        return value;
    }

    default Object tryValue(Parser parser) {
        var str = tryString(parser);

        var map = tryMap(parser, str);

        if (map != null) {
            return map;
        }

        var list = tryList(parser, str);

        if (list != null) {
            return list;
        }

        var call = tryCall(parser);

        if (call != null) {
            if (call.expression != null) {
                throw new RuntimeException("unexepcted expression");
            }
            var valueParser = parser.parsers.findParser(str);
            var text = parser.loadValue(call.keyword, call.arguments);
            return valueParser.parse(text);
        }

        if (str == null) {
            throw new RuntimeException();
        }

        // check for parsed text
        if (tryToken(parser, '(')) {
            var valueParser = parser.parsers.findParser(str);
            var text = tryString(parser);
            var result = valueParser.parse(text);

            expectToken(parser, ')');

            return result;
        }

        return str;
    }

    default MapData tryMap(Parser parser, String typeHint) {
        if (!tryToken(parser, '{')) {
            return null;
        }

        if (tryToken(parser, '}')) {
            return new MapData(typeHint);
        }

        var map = new MapData(typeHint);

        while (true) {
            var key = readString(parser);

            expectToken(parser, ':');

            var value = readValue(parser);

            if (map.containsKey(key)) {
                throw new RuntimeException();
            }

            map.put(key, value);

            if (tryToken(parser, '}')) {
                break;
            }
            else if (!tryToken(parser, ',')) {
                throw new UnexpectedCharException(parser.tape);
            }
        }

        return map;
    }

    default ListData tryList(Parser parser, String typeHint) {
        if (!tryToken(parser, '[')) {
            return null;
        }

        if (tryToken(parser, ']')) {
            return new ListData(typeHint);
        }

        var list = new ListData(typeHint);

        while (true) {
            var value = readValue(parser);

            list.add(value);

            if (tryToken(parser, ']')) {
                break;
            }
            else if (!tryToken(parser, ',')) {
                throw new RuntimeException();
            }
        }

        return list;
    }

    default List<Object> readArguments(Parser parser) {
        var values = new ArrayList<>();

        while (true) {
            var value = tryValue(parser);

            if (value == null) {
                break;
            }

            values.add(value);

            if (!tryToken(parser, ',')) {
                break;
            }
        }

        return values;
    }

}
