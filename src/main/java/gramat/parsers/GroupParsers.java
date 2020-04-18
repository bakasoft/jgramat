package gramat.parsers;

import gramat.expressions.Expression;
import gramat.expressions.Negation;
import gramat.expressions.Optional;
import gramat.expressions.Repetition;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class GroupParsers {

    public static Repetition parseRepetition(Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('{')) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        Integer min = BaseParsers.readInteger(source);
        Integer max;

        if (min != null) {
            BaseParsers.skipBlanks(source);

            if (source.pull(',')) {
                BaseParsers.skipBlanks(source);

                max = BaseParsers.readInteger(source);

                if (max == null) {
                    throw source.error("expected max");
                }

                BaseParsers.skipBlanks(source);
            }
            else if (source.pull(';')) {
                max = min;

                BaseParsers.skipBlanks(source);
            }
            else {
                max = null;
            }
        }
        else {
            max = null;
        }

        Expression expression = CoreParsers.parseExpression(source);

        if (expression == null) {
            throw source.error("expected expression");
        }

        BaseParsers.skipBlanks(source);

        Expression separator;

        if (source.pull('/')) {
            BaseParsers.skipBlanks(source);

            separator = CoreParsers.parseExpression(source);

            if (separator == null) {
                throw source.error("expected expression");
            }
        }
        else {
            separator = null;
        }

        if (!source.pull('}')) {
            throw source.error("expected }");
        }

        return new Repetition(new Location(source, pos0), expression, min, max, separator);
    }

    public static Optional parseOptional(Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('[')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        Expression expression = CoreParsers.parseExpression(source);

        if (expression == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull(']')) {
            source.setPosition(pos0);
            return null;
        }

        return new Optional(new Location(source, pos0), expression);
    }


    public static Expression parseGroup(Source source) {
        if (!source.pull('(')) {
            return null;
        }

        var pos0 = source.getPosition();
        var expression = CoreParsers.parseExpression(source);

        if (expression == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull(')')) {
            source.setPosition(pos0);
            return null;
        }

        return expression;
    }

    public static Negation parseNegation(Source source) {
        if (!source.pull('<')) {
            return null;
        }

        var pos0 = source.getPosition();
        var expression = CoreParsers.parseExpression(source);

        if (expression == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull('>')) {
            source.setPosition(pos0);
            return null;
        }

        return new Negation(new Location(source, pos0), expression);
    }

}
