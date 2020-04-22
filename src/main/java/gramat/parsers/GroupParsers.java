package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.Negation;
import gramat.expressions.wrappers.Optional;
import gramat.expressions.wrappers.Repetition;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class GroupParsers {

    public static Repetition parseRepetition(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('{')) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        Integer min;
        Integer max;

        if (source.pull('+')) {
            BaseParsers.skipBlanks(source);

            min = 1;
            max = null;
        }
        else {
            min = BaseParsers.readInteger(source);

            if (min != null) {
                BaseParsers.skipBlanks(source);

                if (source.pull(',')) {
                    BaseParsers.skipBlanks(source);

                    max = BaseParsers.readInteger(source);

                    if (max == null) {
                        throw source.error("expected max");
                    }

                    BaseParsers.skipBlanks(source);
                } else if (source.pull(';')) {
                    max = min;

                    BaseParsers.skipBlanks(source);
                } else {
                    max = null;
                }
            } else {
                max = null;
            }
        }

        Expression expression = CoreParsers.parseExpression(context, source);

        if (expression == null) {
            throw source.error("expected expression");
        }

        BaseParsers.skipBlanks(source);

        Expression separator;

        if (source.pull('/')) {
            BaseParsers.skipBlanks(source);

            separator = CoreParsers.parseExpression(context, source);

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

    public static Optional parseOptional(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('[')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        Expression expression = CoreParsers.parseExpression(context, source);

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


    public static Expression parseGroup(ParseContext context, Source source) {
        if (!source.pull('(')) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        var pos0 = source.getPosition();
        var expression = CoreParsers.parseExpression(context, source);

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

    public static Negation parseNegation(ParseContext context, Source source) {
        if (!source.pull('<')) {
            return null;
        }

        var pos0 = source.getPosition();
        var expression = CoreParsers.parseExpression(context, source);

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
