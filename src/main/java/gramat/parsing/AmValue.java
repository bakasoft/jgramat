package gramat.parsing;

import gramat.data.ListData;
import gramat.data.MapData;
import gramat.input.Tape;
import gramat.input.errors.UnexpectedCharException;

import java.util.ArrayList;

public interface AmValue extends AmString {

    default Object readValue(Tape tape) {
        var value = tryValue(tape);

        if (value == null) { // TODO what about null values?
            throw new RuntimeException();
        }

        return value;
    }

    default Object tryValue(Tape tape) {
        var str = tryString(tape);

        var map = tryMap(tape, str);

        if (map != null) {
            return map;
        }

        var list = tryList(tape, str);

        if (list != null) {
            return list;
        }

        if (str == null) {
            throw new RuntimeException();
        }

        // check for parsed text
        if (tryToken(tape, '(')) {
            var parser = getGramat().parsers.findParser(str);
            var text = tryString(tape);
            var result = parser.parse(text);

            expectToken(tape, ')');

            return result;
        }

        return str;
    }

    default MapData tryMap(Tape tape, String typeHint) {
        if (!tryToken(tape, '{')) {
            return null;
        }

        if (tryToken(tape, '}')) {
            return new MapData(typeHint);
        }

        var map = new MapData(typeHint);

        while (true) {
            var key = readString(tape);

            expectToken(tape, ':');

            var value = readValue(tape);

            if (map.containsKey(key)) {
                throw new RuntimeException();
            }

            map.put(key, value);

            if (tryToken(tape, '}')) {
                break;
            }
            else if (!tryToken(tape, ',')) {
                throw new UnexpectedCharException(tape);
            }
        }

        return map;
    }

    default ListData tryList(Tape tape, String typeHint) {
        if (!tryToken(tape, '[')) {
            return null;
        }

        if (tryToken(tape, ']')) {
            return new ListData(typeHint);
        }

        var list = new ListData(typeHint);

        while (true) {
            var value = readValue(tape);

            list.add(value);

            if (tryToken(tape, ']')) {
                break;
            }
            else if (!tryToken(tape, ',')) {
                throw new RuntimeException();
            }
        }

        return list;
    }

    default Object readArguments(Tape tape) {
        var values = new ArrayList<>();

        while (true) {
            var value = tryValue(tape);

            if (value == null) {
                break;
            }

            values.add(value);

            if (!tryToken(tape, ',')) {
                break;
            }
        }

        if (values.isEmpty()) {
            return null;
        }
        else if (values.size() == 1) {
            return values.get(0);
        }
        return values;
    }

}
