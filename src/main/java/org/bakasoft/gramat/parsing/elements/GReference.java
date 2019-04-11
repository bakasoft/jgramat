package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Reference;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GReference extends GExpression0C {

    public final String ruleName;

    public GReference(LocationRange location, Gramat gramat, String ruleName) {
        super(location, gramat);
        this.ruleName = Objects.requireNonNull(ruleName);
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Reference(ruleName);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        return control.add(expression) && expression.isOptional_r(control);
    }

    @Override
    public void validate_r(GControl control) {
        // TODO validate infinite loops
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        if (control.add(expression)) {
            expression.countWildProducers_r(count, control);
        }
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        if (control.add(expression)) {
            expression.countWildMutations_r(count, control);
        }
    }

}
