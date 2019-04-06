package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Sequence;
import org.bakasoft.gramat.Gramat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class GSequence extends GElement {

    public final GElement[] expressions;

    public GSequence(GElement[] expressions) {
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public GElement simplify() {
        GElement[] simplification = simplifyAll(expressions);

        if (simplification.length == 0) {
            return null;
        }
        else if (simplification.length == 1) {
            return simplification[0];
        }

        // flatten sequences
        ArrayList<GElement> flattened = new ArrayList<>();
        for (GElement e : simplification) {
            if (e instanceof GSequence) {
                GSequence seq = (GSequence)e;

                Collections.addAll(flattened, seq.expressions);
            }
            else {
                flattened.add(e);
            }
        }

        return new GSequence(toArray(flattened));
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Sequence(compileAll(expressions, gramat, compiled));
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return areAllPlain(expressions, gramat);
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        for (GElement expression : expressions) {
            if (!expression.isOptional(gramat)) {
                return false;
            }
        }

        return true;
    }
}
