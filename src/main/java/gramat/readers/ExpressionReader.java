package gramat.readers;

import gramat.expressions.ExpressionFactory;
import gramat.expressions.models.ModelExpression;

import java.util.ArrayList;

public interface ExpressionReader extends BaseReader, ValueReader {

    default ModelExpression readExpression() {
        var alternation = new ArrayList<ModelExpression>();
        var sequence = new ArrayList<ModelExpression>();

        while (true) {
            var item = tryExpressionItem();

            if (item == null) {
                break;
            }

            sequence.add(item);

            if (tryToken('|')) {
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

    default ModelExpression tryExpressionItem() {
        var group = tryGroup();
        if (group != null) {
            return group;
        }
        var optional = tryOptional();
        if (optional != null) {
            return optional;
        }

        var repetition = tryRepetition();
        if (repetition != null) {
            return repetition;
        }

        var wild = tryWild();
        if (wild != null) {
            return wild;
        }

        var literal = tryQuotedString('\"');
        if (literal != null) {
            return ExpressionFactory.literal(literal);
        }

        var charClass = tryQuotedString('\'');
        if (charClass != null) {
            return ExpressionFactory.characterClass(charClass);
        }

        var reference = tryKeyword();
        if (reference != null) {
            return ExpressionFactory.reference(reference);
        }

        var call = tryCall();
        if (call != null) {
            return ExpressionFactory.call(call);
        }

        return null;
    }

    default ModelExpression tryOptional() {
        if (tryToken('[')) {
            var expr = readExpression();

            expectToken(']');

            return ExpressionFactory.optional(expr);
        }
        return null;
    }

    default ModelExpression tryGroup() {
        if (tryToken('(')) {
            var expr = readExpression();

            expectToken(')');

            return expr;
        }
        return null;
    }

    default ModelExpression tryRepetition() {
        if (tryToken('{')) {
            var minimum = 0;

            if (tryToken('+')) {
                minimum = 1;
            }

            var content = readExpression();
            var separator = (ModelExpression)null;

            if (tryToken('/')) {
                separator = readExpression();
            }

            expectToken('}');

            return ExpressionFactory.repetition(content, separator, minimum);
        }
        return null;
    }

    default ModelExpression tryWild() {
        if (tryToken('*')) {
            return ExpressionFactory.wild();
        }
        return null;
    }

}
