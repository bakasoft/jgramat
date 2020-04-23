package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.expressions.values.*;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

public class ValueParsers {

    public static Expression parseValue(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('@')) {
            return null;
        }

        String keyword = BaseParsers.readKeyword(source);

        if (keyword == null) {
            throw source.error("Expected keyword");
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull('(')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        var nameLit = BaseParsers.readString(source, '\'');
        Expression nameExp;
        Expression valueExp;

        if (nameLit != null) {
            nameExp = null;

            BaseParsers.skipBlanks(source);

            if (!source.pull(':')) {
                throw source.error("Expected :");
            }

            BaseParsers.skipBlanks(source);

            valueExp = CoreParsers.parseExpression(context, source);

            if (valueExp == null) {
                throw source.error("Expected expression");
            }
        }
        else {
            var expression = CoreParsers.parseExpression(context, source);

            if (expression == null) {
                throw source.error("Expected expression.");
            }

            BaseParsers.skipBlanks(source);

            if (source.pull(':')) {
                BaseParsers.skipBlanks(source);

                nameExp = expression;
                valueExp = CoreParsers.parseExpression(context, source);

                if (valueExp == null) {
                    throw source.error("Expected expression");
                }
            } else {
                nameExp = null;
                valueExp = expression;
            }
        }

        if (!source.pull(')')) {
            throw source.error("Expected )");
        }

        var loc0 = source.locationOf(pos0);

        return makeValue(loc0, keyword, nameLit, nameExp, valueExp);
    }

    public static Expression makeValue(Location loc0, String keyword, String nameLit, Expression nameExp, Expression valueExp) {
        switch (keyword) {
            case "set":
                if (nameLit != null) {
                    return new AttributeExp(loc0, nameLit, valueExp);
                }
                else if (nameExp == null) {
                    throw new ParseException("Expected name", loc0);
                }

                return new DynAttributeExp(loc0, nameExp, valueExp);
            case "object":
                if (nameExp != null) {
                    return new DynObjectExp(loc0, nameExp, valueExp);
                }
                return new ObjectExp(loc0, nameLit, valueExp);
            case "list":
                if (nameExp != null) {
                    return new DynListExp(loc0, nameExp, valueExp);
                }
                return new ListExp(loc0, nameLit, valueExp);
            case "join":
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected name", loc0);
                }
                return new JoinExp(loc0, valueExp);
            case "map":
                if (nameLit == null) {
                    throw new ParseException("expected replacement.", loc0);
                }
                else if (nameExp != null) {
                    throw new ParseException("dynamic mappings are not implemented.", loc0);
                }
                return new MapExp(loc0, nameLit, valueExp);
            default:
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected parser name: " + nameLit, loc0);
                }
                return new ValueExp(loc0, keyword, valueExp);
        }
    }


}
