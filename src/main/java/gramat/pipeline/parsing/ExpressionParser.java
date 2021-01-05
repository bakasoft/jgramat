package gramat.pipeline.parsing;

import gramat.scheme.data.expressions.ExpressionDataFactory;
import gramat.scheme.data.expressions.ExpressionData;

import java.util.ArrayList;

public interface ExpressionParser extends BaseParser, ValueParser {

    default ExpressionData readExpression() {
        var alternation = new ArrayList<ExpressionData>();
        var sequence = new ArrayList<ExpressionData>();

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
                alternation.add(ExpressionDataFactory.sequence(sequence));
                sequence.clear();
            }
        }

        // Flush left items
        if (sequence.size() > 0) {
            alternation.add(ExpressionDataFactory.sequence(sequence));
        }

        if (alternation.isEmpty()) {
            throw new RuntimeException();
        }
        else if (alternation.size() == 1) {
            return alternation.get(0);
        }
        else {
            return ExpressionDataFactory.alternation(alternation);
        }
    }

    default ExpressionData tryExpressionItem() {
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
            return ExpressionDataFactory.literal(literal);
        }

        var charClass = tryQuotedString('\'');
        if (charClass != null) {
            return ExpressionDataFactory.characterClass(charClass);
        }

        var reference = tryKeyword();
        if (reference != null) {
            return ExpressionDataFactory.reference(reference);
        }

        var call = tryCall();
        if (call != null) {
            return ExpressionDataFactory.call(call);
        }

        return null;
    }

    default ExpressionData tryOptional() {
        if (tryToken('[')) {
            var expr = readExpression();

            expectToken(']');

            return ExpressionDataFactory.optional(expr);
        }
        return null;
    }

    default ExpressionData tryGroup() {
        if (tryToken('(')) {
            var expr = readExpression();

            expectToken(')');

            return expr;
        }
        return null;
    }

    default ExpressionData tryRepetition() {
        if (tryToken('{')) {
            var minimum = 0;

            if (tryToken('+')) {
                minimum = 1;
            }

            var content = readExpression();
            var separator = (ExpressionData)null;

            if (tryToken('/')) {
                separator = readExpression();
            }

            expectToken('}');

            return ExpressionDataFactory.repetition(content, separator, minimum);
        }
        return null;
    }

    default ExpressionData tryWild() {
        if (tryToken('*')) {
            return ExpressionDataFactory.wild();
        }
        return null;
    }

}
