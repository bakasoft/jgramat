package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.SingleChar;
import org.bakasoft.gramat.elements.Symbol;
import org.bakasoft.gramat.Gramat;

import java.util.*;

public class GString extends GElement {

    public final String content;

    public GString(String content) {
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public List<GElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        if (content.isEmpty()) {
            return null;
        }
        else if (content.length() == 1) {
            char c = content.charAt(0);

            return new SingleChar(c);
        }

        return new Symbol(content);
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return true;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return content.isEmpty();
    }
}
