package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.parsing.GLiteral;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GMap extends GLiteral {

    private final Map<String, GLiteral> map;

    public GMap(Map<String, GLiteral> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public GLiteral get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    // parsing

    public static GMap expectMap(Tape tape) {
        HashMap<String, GLiteral> map = new HashMap<>();

        GElement.expectSymbol(tape, '{');

        GElement.skipVoid(tape);

        while (!GElement.trySymbol(tape, '}')) {
            String keyName = GElement.expectName(tape, "key name");

            GElement.skipVoid(tape);

            GElement.expectSymbol(tape, ':');

            GElement.skipVoid(tape);

            GLiteral value = GLiteral.expectLiteral(tape);

            GElement.skipVoid(tape);

            if (map.put(keyName, value) != null) {
                throw new GrammarException("Key already exists: " + GElement.inspect(keyName), tape.getLocation());
            }
        }

        return new GMap(map);
    }
}
