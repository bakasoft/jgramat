package gramat.parsing;

import gramat.models.ModelFactory;
import gramat.models.expressions.ModelExpression;
import gramat.models.source.ModelCall;
import gramat.input.Tape;

import java.util.ArrayList;

public interface AmExpressionParser extends AmBase, AmValue {

    default ModelExpression readExpression(Tape tape) {
        var alternation = new ArrayList<ModelExpression>();
        var sequence = new ArrayList<ModelExpression>();

        while (true) {
            var item = tryExpressionItem(tape);

            if (item == null) {
                break;
            }

            sequence.add(item);

            if (tryToken(tape, '|')) {
                if (sequence.isEmpty()) {
                    throw new RuntimeException();
                }
                alternation.add(ModelFactory.sequence(sequence));
                sequence.clear();
            }
        }

        // Flush left items
        if (sequence.size() > 0) {
            alternation.add(ModelFactory.sequence(sequence));
        }

        if (alternation.isEmpty()) {
            throw new RuntimeException();
        }
        else if (alternation.size() == 1) {
            return alternation.get(0);
        }
        else {
            return ModelFactory.alternation(alternation);
        }
    }

    default ModelExpression tryExpressionItem(Tape tape) {
        var group = tryGroup(tape);
        if (group != null) {
            return group;
        }
        var optional = tryOptional(tape);
        if (optional != null) {
            return optional;
        }

        var repetition = tryRepetition(tape);
        if (repetition != null) {
            return repetition;
        }

        var wild = tryWild(tape);
        if (wild != null) {
            return wild;
        }

        var literal = tryQuotedString(tape, '\"');
        if (literal != null) {
            return ModelFactory.literal(literal);
        }

        var charClass = tryQuotedString(tape, '\'');
        if (charClass != null) {
            return ModelFactory.characterClass(charClass);
        }

        var reference = tryKeyword(tape);
        if (reference != null) {
            return ModelFactory.reference(reference);
        }

        var call = tryCall(tape);
        if (call != null) {
            return ModelFactory.call(call);
        }

        return null;
    }

    default ModelExpression tryOptional(Tape tape) {
        if (tryToken(tape, '[')) {
            var expr = readExpression(tape);

            expectToken(tape, ']');

            return ModelFactory.optional(expr);
        }
        return null;
    }

    default ModelExpression tryGroup(Tape tape) {
        if (tryToken(tape, '(')) {
            var expr = readExpression(tape);

            expectToken(tape, ')');

            return expr;
        }
        return null;
    }

    default ModelExpression tryRepetition(Tape tape) {
        if (tryToken(tape, '{')) {
            var minimum = 0;

            if (tryToken(tape, '+')) {
                minimum = 1;
            }

            var content = readExpression(tape);
            var separator = (ModelExpression)null;

            if (tryToken(tape, '/')) {
                separator = readExpression(tape);
            }

            expectToken(tape, '}');

            return ModelFactory.repetition(content, separator, minimum);
        }
        return null;
    }

    default ModelExpression tryWild(Tape tape) {
        if (tryToken(tape, '*')) {
            return ModelFactory.wild();
        }
        return null;
    }

    default ModelCall tryCall(Tape tape) {
        if (tryToken(tape, '@')) {
            var call = new ModelCall();

            call.keyword = readString(tape);

            if (tryToken(tape, '<')) {
                call.arguments = readArguments(tape);

                expectToken(tape, '>');
            }

            if (tryToken(tape, '(')) {
                call.expression = readExpression(tape);

                expectToken(tape, ')');
            }

            return call;
        }
        return null;
    }

}
