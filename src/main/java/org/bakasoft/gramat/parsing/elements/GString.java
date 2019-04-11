package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.SingleChar;
import org.bakasoft.gramat.elements.Symbol;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;

import java.util.*;

public class GString extends GExpression0C {

    public final String content;

    public GString(LocationRange location, Gramat gramat, String content) {
        super(location, gramat);
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
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
    public boolean isOptional_r(GControl control) {
        return content.isEmpty();
    }

    @Override
    public void validate_r(GControl control) {
        // nothing to validate yet
    }

    @Override
    public boolean hasWildProducers_r(GControl control) {
        return false;
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        return false;
    }
}
