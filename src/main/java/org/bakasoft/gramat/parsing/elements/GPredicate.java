package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.*;
import org.bakasoft.gramat.elements.CharRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.SingleChar;
import org.bakasoft.gramat.inspect.Inspector;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;

import java.util.Map;

public class GPredicate extends GExpression0C {

    public final Condition[] conditions;

    public GPredicate(LocationRange location, Gramat gramat, Condition[] conditions) {
        super(location, gramat);
        this.conditions = conditions;
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        // case when there is only one char
        if (conditions.length == 1 && conditions[0] instanceof Option) {
            Option o = (Option)conditions[0];
            return new SingleChar(o.value);
        }

        StringBuilder name = new StringBuilder();
        CharPredicate[] predicates = new CharPredicate[conditions.length];
        for (int i = 0; i < predicates.length; i++) {
            if (i > 0) {
                name.append(' ');
            }

            if (conditions[i] instanceof Range) {
                Range range = (Range)conditions[i];
                predicates[i] = c -> (c >= range.beginChar && c <= range.endChar);
                name.append("from ")
                        .append(Inspector.inspect(range.beginChar))
                        .append(" to ")
                        .append(Inspector.inspect(range.endChar));
            }
            else if (conditions[i] instanceof Option) {
                Option option = (Option)conditions[i];
                predicates[i] = c -> (c == option.value);
                name.append(Inspector.inspect(option.value));
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        return new CharRange(name.toString(), c -> {
            for (CharPredicate predicate : predicates) {
                if (predicate.test(c)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return conditions.length == 0;
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

    public interface Condition {}

    public static class Range implements Condition {

        public final char beginChar;
        public final char endChar;

        public Range(char beginChar, char endChar) {
            this.beginChar = beginChar;
            this.endChar = endChar;
        }
    }

    public static class Option implements Condition {

        public final char value;

        public Option(char value) {
            this.value = value;
        }
    }

}
