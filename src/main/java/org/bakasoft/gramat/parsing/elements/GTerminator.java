package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Termination;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;

import java.util.*;

public class GTerminator extends GExpression0C {

    public GTerminator(LocationRange location, Gramat gramat) {
        super(location, gramat);
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Termination();
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return false;
    }

    @Override
    public void validate_r(GControl control) {
        // everything is ok!
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
