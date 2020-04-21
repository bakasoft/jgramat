package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.expressions.values.*;
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

        switch (keyword) {
            case "set":
                if (nameLit != null) {
                    return new AttributeExp(loc0, nameLit, valueExp);
                }
                else if (nameExp == null) {
                    throw new ParseException("Expected name", loc0);
                }

                return new DynAttributeExp(loc0, nameExp, valueExp);
            case "list":
                if (nameExp != null) {
                    return new DynListExp(loc0, nameExp, valueExp);
                }
                return new ListExp(loc0, nameLit, valueExp);
            case "object":
                if (nameExp != null) {
                    return new DynObjectExp(loc0, nameExp, valueExp);
                }
                return new ObjectExp(loc0, nameLit, valueExp);
            case "value":
                if (nameExp != null) {
                    throw source.error("dynamic parsers not implemented.");
                }
                return new ValueExp(loc0, nameLit, valueExp);
            default:
                if (nameLit != null || nameExp != null) {
                    throw source.error("Unexpected parser name.");
                }
                return new ValueExp(loc0, keyword, valueExp);
        }
    }


}
