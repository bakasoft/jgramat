package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.Stringifier;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.CharRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.SingleChar;
import org.bakasoft.gramat.parsing.GElement;

import java.util.ArrayList;
import java.util.Map;

public class GPredicate extends GElement {

    public final Condition[] conditions;

    public GPredicate(Condition[] conditions) {
        this.conditions = conditions;
    }

    public static GPredicate expectPredicate(Tape tape) {
        ArrayList<Condition> conditions = new ArrayList<>();

        expectSymbol(tape, '`');

        while (!isChar(tape, '`')) {
            if (isWhitespace(tape) && conditions.isEmpty()) {
                throw new RuntimeException("Predicate expression cannot start with whitespace");
            }

            char c = readStringChar(tape);

            // if is the separator or the end of string
            if (isWhitespace(tape) || isChar(tape, '`')) {
                skipVoid(tape);

                conditions.add(new Option(c));
            }
            else {
                expectSymbols(tape, "..");

                char begin = c;
                char end = readStringChar(tape);

                conditions.add(new Range(begin, end));
            }
        }

        expectSymbol(tape, '`');

        return new GPredicate(conditions.toArray(new Condition[0]));
    }

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
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
                name.append(Stringifier.literal(range.beginChar))
                        .append("..")
                        .append(Stringifier.literal(range.endChar));
            }
            else if (conditions[i] instanceof Option) {
                Option option = (Option)conditions[i];
                predicates[i] = c -> (c == option.value);
                name.append(Stringifier.literal(option.value));
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
    public boolean isPlain(Gramat gramat) {
        return true;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return conditions.length == 0;
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
