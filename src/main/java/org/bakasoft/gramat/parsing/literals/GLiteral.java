package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class GLiteral {

    public String resolveString() {
        if (this instanceof GToken) {
            GToken token = (GToken)this;

            return token.content;
        }
        else if (this instanceof GArray) {
            GArray array = (GArray)this;

            if (array.size() == 1) {
                return array.get(0).resolveString();
            }
        }
        else if (this instanceof GMap) {
            GMap map = (GMap)this;

            if (map.size() == 1) {
                for (String key : map.keySet()) {
                    if (map.get(key) == null) {
                        return key;
                    }
                }
            }
        }

        throw new RuntimeException("cannot resolve string");
    }

    public String[] resolveStringArray() {
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
                    Collections.addAll(result, item.resolveStringArray());
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

        return result.toArray(new String[0]);
    }
}
