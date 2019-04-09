package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GStringifier;
import org.bakasoft.gramat.parsing.elements.captures.GCapture;
import org.bakasoft.gramat.parsing.elements.captures.GObject;

import java.util.*;

abstract public class GElement {

    abstract public GElement simplify();

    abstract public List<GElement> getChildren();

    // TODO now map is not required
    abstract public Element compile(Gramat gramat, Map<String, Element> compiled);

    abstract public boolean isPlain(Gramat gramat);

    // is this method needed
    abstract public boolean isOptional(Gramat gramat);

    public String stringify() {
        return GStringifier.stringify(this);
    }

    public static Element[] compileAll(GElement[] elements, Gramat gramat, Map<String, Element> compiled) {
        return Arrays.stream(elements)
                .map(item -> item.compile(gramat, compiled))
                .toArray(Element[]::new);
    }

    public static boolean areAllPlain(GElement[] elements, Gramat gramat) {
        return Arrays.stream(elements).allMatch(item -> item.isPlain(gramat));
    }

    public static boolean isAnyPlain(GElement[] elements, Gramat gramat) {
        return Arrays.stream(elements).anyMatch(item -> item.isPlain(gramat));
    }

    public static GElement[] simplifyAll(GElement[] elements) {
        ArrayList<GElement> list = new ArrayList<>();

        for (GElement element : elements) {
            if (element != null) {
                GElement simplified = element.simplify();

                if (simplified != null) {
                    list.add(simplified);
                }
            }
        }

        return toArray(list);
    }

    public static GElement[] toArray(List<? extends GElement> elements) {
        return elements.toArray(new GElement[0]);
    }

}
