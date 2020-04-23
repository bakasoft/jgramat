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

        if (!source.pull(Mark.REPETITION_BEGIN)) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        Integer min;
        Integer max;

        if (source.pull(Mark.REPETITION_PLUS)) {
            BaseParsers.skipBlanks(source);

            min = 1;
            max = null;
        }
        else {
            min = BaseParsers.readInteger(source);

            if (min != null) {
                BaseParsers.skipBlanks(source);

                if (source.pull(Mark.REPETITION_RANGE_SEPARATOR)) {
                    BaseParsers.skipBlanks(source);

                    max = BaseParsers.readInteger(source);

                    if (max == null) {
                        throw source.error("expected max");
                    }

                    BaseParsers.skipBlanks(source);
                } else if (source.pull(Mark.REPETITION_COUNT_MARK)) {
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

        if (source.pull(Mark.REPETITION_SEPARATOR_MARK)) {
            BaseParsers.skipBlanks(source);

            separator = CoreParsers.parseExpression(context, source);

            if (separator == null) {
                throw source.error("expected expression");
            }
        }
        else {
            separator = null;
        }

        if (!source.pull(Mark.REPETITION_END)) {
            throw source.error("expected }");
        }

        return new Repetition(new Location(source, pos0), expression, min, max, separator);
    }

    public static Optional parseOptional(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.OPTIONAL_BEGIN)) {
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

        if (!source.pull(Mark.OPTIONAL_END)) {
            source.setPosition(pos0);
            return null;
        }

        return new Optional(new Location(source, pos0), expression);
    }


    public static Expression parseGroup(ParseContext context, Source source) {
        if (!source.pull(Mark.GROUP_BEGIN)) {
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

        if (!source.pull(Mark.GROUP_END)) {
            source.setPosition(pos0);
            return null;
        }

        return expression;
    }

    public static Negation parseNegation(ParseContext context, Source source) {
        if (!source.pull(Mark.NEGATION_BEGIN)) {
            return null;
        }

        var pos0 = source.getPosition();
        var expression = CoreParsers.parseExpression(context, source);

        if (expression == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull(Mark.NEGATION_END)) {
            source.setPosition(pos0);
            return null;
        }

        return new Negation(new Location(source, pos0), expression);
    }

}
