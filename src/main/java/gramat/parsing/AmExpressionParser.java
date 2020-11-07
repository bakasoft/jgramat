package gramat.parsing;

import gramat.models.factories.ExpressionFactory;
import gramat.models.expressions.ModelExpression;
import gramat.input.Tape;

import java.util.ArrayList;

public interface AmExpressionParser extends AmBase, AmValue {

    default ModelExpression readExpression(Parser parser) {
        var alternation = new ArrayList<ModelExpression>();
        var sequence = new ArrayList<ModelExpression>();

        while (true) {
            var item = tryExpressionItem(parser);

            if (item == null) {
                break;
            }

            sequence.add(item);

            if (tryToken(parser, '|')) {
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

    default ModelExpression tryExpressionItem(Parser parser) {
        var group = tryGroup(parser);
        if (group != null) {
            return group;
        }
        var optional = tryOptional(parser);
        if (optional != null) {
            return optional;
        }

        var repetition = tryRepetition(parser);
        if (repetition != null) {
            return repetition;
        }

        var wild = tryWild(parser);
        if (wild != null) {
            return wild;
        }

        var literal = tryQuotedString(parser, '\"');
        if (literal != null) {
            return ExpressionFactory.literal(literal);
        }

        var charClass = tryQuotedString(parser, '\'');
        if (charClass != null) {
            return ExpressionFactory.characterClass(charClass);
        }

        var reference = tryKeyword(parser);
        if (reference != null) {
            return ExpressionFactory.reference(reference);
        }

        var call = tryCall(parser);
        if (call != null) {
            return ExpressionFactory.call(call);
        }

        return null;
    }

    default ModelExpression tryOptional(Parser parser) {
        if (tryToken(parser, '[')) {
            var expr = readExpression(parser);

            expectToken(parser, ']');

            return ExpressionFactory.optional(expr);
        }
        return null;
    }

    default ModelExpression tryGroup(Parser parser) {
        if (tryToken(parser, '(')) {
            var expr = readExpression(parser);

            expectToken(parser, ')');

            return expr;
        }
        return null;
    }

    default ModelExpression tryRepetition(Parser parser) {
        if (tryToken(parser, '{')) {
            var minimum = 0;

            if (tryToken(parser, '+')) {
                minimum = 1;
            }

            var content = readExpression(parser);
            var separator = (ModelExpression)null;

            if (tryToken(parser, '/')) {
                separator = readExpression(parser);
            }

            expectToken(parser, '}');

            return ExpressionFactory.repetition(content, separator, minimum);
        }
        return null;
    }

    default ModelExpression tryWild(Parser parser) {
        if (tryToken(parser, '*')) {
            return ExpressionFactory.wild();
        }
        return null;
    }

}
