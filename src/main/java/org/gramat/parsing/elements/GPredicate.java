package org.gramat.parsing.elements;

import org.gramat.*;
import org.gramat.elements.CharRange;
import org.gramat.elements.Element;
import org.gramat.elements.SingleChar;
import org.bakasoft.framboyan.inspect.Inspector;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression0C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;
import org.gramat.CharPredicate;
import org.gramat.Gramat;
import org.gramat.LocationRange;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

        StringBuilder output = new StringBuilder();
        Inspector inspector = new Inspector(output);
        CharPredicate[] predicates = new CharPredicate[conditions.length];
        for (int i = 0; i < predicates.length; i++) {
            if (i > 0) {
                inspector.write(' ');
            }

            if (conditions[i] instanceof Range) {
                Range range = (Range)conditions[i];
                predicates[i] = c -> (c >= range.beginChar && c <= range.endChar);
                inspector.write("from ");
                inspector.writeString(range.beginChar, '"');
                inspector.write(" to ");
                inspector.writeString(range.endChar, '"');
            }
            else if (conditions[i] instanceof Option) {
                Option option = (Option)conditions[i];
                predicates[i] = c -> (c == option.value);
                inspector.writeString(option.value, '"');
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        return new CharRange(output.toString(), c -> {
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
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return null; // empty type
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
