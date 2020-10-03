package gramat.parsing;

import gramat.am.ExpressionFactory;
import gramat.am.expression.AmExpression;
import gramat.am.source.AmCall;
import gramat.input.Tape;

import java.util.ArrayList;

public interface AmExpressionParser extends AmBase, AmValue {

    default AmExpression readExpression(Tape tape) {
        var alternation = new ArrayList<AmExpression>();
        var sequence = new ArrayList<AmExpression>();

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
                alternation.add(ExpressionFactory.sequence(sequence));
                sequence.clear();
            }
        }

        // Flush left items
        if (sequence.size() > 0) {
            alternation.add(ExpressionFactory.sequence(sequence));
        }

        if (alternation.isEmpty()) {
            throw new RuntimeException();
        }
        else if (alternation.size() == 1) {
            return alternation.get(0);
        }
        else {
            return ExpressionFactory.alternation(alternation);
        }
    }

    default AmExpression tryExpressionItem(Tape tape) {
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
            return ExpressionFactory.literal(literal);
        }

        var charClass = tryQuotedString(tape, '\'');
        if (charClass != null) {
            return ExpressionFactory.characterClass(charClass);
        }

        var reference = tryKeyword(tape);
        if (reference != null) {
            return ExpressionFactory.reference(reference);
        }

        var call = tryCall(tape);
        if (call != null) {
            return ExpressionFactory.call(call);
        }

        return null;
    }

    default AmExpression tryOptional(Tape tape) {
        if (tryToken(tape, '[')) {
            var expr = readExpression(tape);

            expectToken(tape, ']');

            return ExpressionFactory.optional(expr);
        }
        return null;
    }

    default AmExpression tryRepetition(Tape tape) {
        if (tryToken(tape, '{')) {
            var content = readExpression(tape);
            var separator = (AmExpression)null;

            if (tryToken(tape, '/')) {
                separator = readExpression(tape);
            }

            expectToken(tape, '}');

            return ExpressionFactory.repetition(content, separator);
        }
        return null;
    }

    default AmExpression tryWild(Tape tape) {
        if (tryToken(tape, '*')) {
            return ExpressionFactory.wild();
        }
        return null;
    }

    default AmCall tryCall(Tape tape) {
        if (tryToken(tape, '@')) {
            var call = new AmCall();

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
