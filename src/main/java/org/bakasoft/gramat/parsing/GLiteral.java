package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class GLiteral {

    public String resolveString() {
        if (this instanceof GToken) {
            GToken token = (GToken)this;

            return token.content;
        }
        else if (this instanceof GArray) {
            StringBuilder result = new StringBuilder();
            GArray array = (GArray)this;

            for (int i = 0; i < array.size(); i++) {
                GLiteral item = array.get(i);

                if (i > 0) {
                    result.append(", ");
                }

                result.append(item.resolveString());
            }

            return result.toString();
        }
        else if (this instanceof GMap) {
            StringBuilder result = new StringBuilder();
            GMap map = (GMap)this;

            for (String key : map.keySet()) {
                if (result.length() > 0) {
                    result.append(", ");
                }

                GLiteral value = map.get(key);
                String str = value.resolveString();

                result.append(key);
                result.append(": ");
                result.append(str);
            }

            return result.toString();
        }
        else {
            throw new RuntimeException("cannot resolve string");
        }
    }

    public List<String> resolveStringList() {
        ArrayList<String> result = new ArrayList<>();

        if (this instanceof GToken) {
            GToken token = (GToken)this;

            result.add(token.content);
        }
        else if (this instanceof GArray) {
            GArray array = (GArray)this;

            for (int i = 0; i < array.size(); i++) {
                GLiteral item = array.get(i);

                if (item instanceof GArray) {
                    result.addAll(item.resolveStringList());
                }

                result.add(item.resolveString());
            }
        }
        else if (this instanceof GMap) {
            GMap map = (GMap)this;

            for (String key : map.keySet()) {
                GLiteral value = map.get(key);
                String str = value.resolveString();

                result.add(str);
            }
        }
        else {
            throw new RuntimeException("cannot resolve string list");
        }

        return result;
    }

    public static GLiteral expectLiteral(Tape tape) {
        GLiteral literal = tryLiteral(tape);

        if (literal == null) {
            throw new GrammarException("Expected literal", tape.getLocation());
        }

        return literal;
    }

    public static GLiteral tryLiteral(Tape tape) {
        if (GElement.isChar(tape, '"') || GElement.isLetter(tape)) {
            return GToken.expectToken(tape);
        }
        else if (GElement.isChar(tape, '[')) {
            return GArray.expectArray(tape);
        }
        else if (GElement.isChar(tape, '{')) {
            return GMap.expectMap(tape);
        }

        return null;
    }
}
