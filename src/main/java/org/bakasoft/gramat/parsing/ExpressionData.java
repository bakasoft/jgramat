package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Location;
import org.bakasoft.gramat.elements.Element;

import java.util.List;

abstract public class ExpressionData  {

    private Location beginLocation;
    private Location endLocation;

    abstract public Element _settle(Grammar grammar);

    public static Element[] settleAll(Grammar grammar, List<? extends ExpressionData> templates) {
        Element[] elements = new Element[templates.size()];

        for (int i = 0; i < templates.size(); i++) {
            elements[i] = grammar.settle(templates.get(i));
        }

        return elements;
    }

//    @Override
    public void setBeginLocation(Location location) {
        beginLocation = location;
    }

//    @Override
    public Location getBeginLocation() {
        return beginLocation;
    }

//    @Override
    public void setEndLocation(Location location) {
        endLocation = location;
    }

//    @Override
    public Location getEndLocation() {
        return endLocation;
    }
}
