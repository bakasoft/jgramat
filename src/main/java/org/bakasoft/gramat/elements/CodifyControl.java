package org.bakasoft.gramat.elements;

import java.util.HashMap;
import java.util.function.Consumer;

public class CodifyControl {

    private final StringBuilder builder;
    private final HashMap<Element, String> elementNames;

    public CodifyControl() {
        builder = new StringBuilder();
        elementNames = new HashMap<>();
    }

    public void codify(Element element, boolean grouped, Consumer<StringBuilder> codifier) {
        if (elementNames.containsKey(element)) {
            builder.append(elementNames.get(element));
        }
        else {
            String name = "ref" + elementNames.size();

            elementNames.put(element, name);

            if (grouped || element instanceof WrappedElement) {
                codifier.accept(builder);
            }
            else {
                builder.append("(");
                codifier.accept(builder);
                builder.append(")");
            }
        }
    }

    public String getCode() {
        return builder.toString();
    }

}
