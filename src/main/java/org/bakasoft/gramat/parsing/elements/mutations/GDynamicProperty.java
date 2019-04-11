package org.bakasoft.gramat.parsing.elements.mutations;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.DynamicProperty;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GDynamicProperty extends GExpression {

    public final GExpression nameExpression;
    public final GExpression separatorExpression;
    public final GExpression valueExpression;
    public final boolean appendMode;

    // Normal mode: key-value, inverted mode: value-key
    public final boolean invertedMode;

    public GDynamicProperty(LocationRange location, Gramat gramat, GExpression nameExpression, GExpression separatorExpression, GExpression valueExpression, boolean appendMode, boolean invertedMode) {
        super(location, gramat);
        this.nameExpression = nameExpression;
        this.separatorExpression = separatorExpression;
        this.appendMode = appendMode;
        this.valueExpression = valueExpression;
        this.invertedMode = invertedMode;
    }

    @Override
    public List<GExpression> getChildren() {
        if (separatorExpression == null) {
            return Arrays.asList(nameExpression, valueExpression);
        }

        return Arrays.asList(nameExpression, separatorExpression, valueExpression);
    }

    @Override
    public GExpression simplify() {
        return new GDynamicProperty(
                location, gramat,
                nameExpression.simplify(),
                separatorExpression != null ? separatorExpression.simplify() : null,
                valueExpression.simplify(),
                appendMode,
                invertedMode
        );
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new DynamicProperty(
                nameExpression.compile(compiled),
                separatorExpression != null ? separatorExpression.compile(compiled) : null,
                valueExpression.compile(compiled),
                appendMode,
                invertedMode
        );
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return getChildren().stream().allMatch(e -> e.isOptional_r(control));
    }

    @Override
    public void validate_r(GControl control) {
        if (nameExpression.hasWildMutations() || nameExpression.hasWildProducers()) {
            throw new GrammarException("Property names cannot have mutations or producers inside.", nameExpression.location);
        }
        else if (valueExpression.hasWildMutations()) {
            throw new GrammarException("Property values cannot have other mutations inside.", valueExpression.location);
        }
        else if (separatorExpression != null && (separatorExpression.hasWildMutations() || separatorExpression.hasWildProducers())) {
            throw new GrammarException("Property separators cannot have mutations or producers inside.", separatorExpression.location);
        }
    }

    @Override
    public boolean hasWildProducers_r(GControl control) {
        // properties absorb producers
        return false;
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        // this is a wild mutation!
        return false;
    }
}
