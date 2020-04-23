package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.expressions.values.*;
import gramat.expressions.wrappers.DebugExp;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

public class ValueParsers {

    public static Expression parseValue(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.VALUE_MARK)) {
            return null;
        }

        String keyword = BaseParsers.readKeyword(source);

        if (keyword == null) {
            throw source.error("Expected keyword");
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull(Mark.GROUP_BEGIN)) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        var nameLit = BaseParsers.readString(source, Mark.TOKEN_DELIMITER);
        Expression nameExp;
        Expression valueExp;

        if (nameLit != null) {
            nameExp = null;

            BaseParsers.skipBlanks(source);

            if (!source.pull(Mark.NAME_SEPARATOR)) {
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

            if (source.pull(Mark.NAME_SEPARATOR)) {
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

        if (!source.pull(Mark.GROUP_END)) {
            throw source.error("Expected " + Mark.GROUP_END);
        }

        var loc0 = source.locationOf(pos0);

        return makeValue(context, loc0, keyword, nameLit, nameExp, valueExp);
    }

    public static Expression makeValue(ParseContext context, Location loc0, String keyword, String nameLit, Expression nameExp, Expression valueExp) {
        switch (keyword) {
            case Mark.SET_KEYWORD:
                if (nameLit != null) {
                    return new AttributeExp(loc0, nameLit, valueExp);
                }
                else if (nameExp == null) {
                    throw new ParseException("Expected name", loc0);
                }

                return new DynAttributeExp(loc0, nameExp, valueExp);
            case Mark.OBJECT_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    return new DynObjectExp(loc0, nameExp, valueExp);
                }
                else if (nameLit != null && nameExp == null) {
                    var type = context.getType(nameLit);

                    if (type != null) {
                        return new TypedObjectExp(loc0, type, valueExp);
                    }
                }

                return new ObjectExp(loc0, nameLit, valueExp);
            case Mark.LIST_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    return new DynListExp(loc0, nameExp, valueExp);
                }
                else if (nameLit != null && nameExp == null) {
                    var type = context.getType(nameLit);

                    if (type != null) {
                        return new TypedListExp(loc0, type, valueExp);
                    }
                }
                return new ListExp(loc0, nameLit, valueExp);
            case Mark.JOIN_KEYWORD:
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected name", loc0);
                }
                return new JoinExp(loc0, valueExp);
            case Mark.NULL_KEYWORD:
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected name", loc0);
                }
                return new NullExp(loc0, valueExp);
            case Mark.MAP_KEYWORD:
                if (nameLit == null) {
                    throw new ParseException("expected replacement.", loc0);
                }
                else if (nameExp != null) {
                    throw new ParseException("dynamic mappings are not implemented.", loc0);
                }
                return new MapExp(loc0, nameLit, valueExp);
            case Mark.DEBUG_KEYWORD:
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected name", loc0);
                }
                return new DebugExp(loc0, valueExp);
            default:
                if (nameLit != null || nameExp != null) {
                    throw new ParseException("Unexpected parser name: " + nameLit, loc0);
                }

                var parser = context.getParser(keyword);

                if (parser == null) {
                    throw new ParseException("Unsupported parser: " + keyword, loc0);
                }

                return new ValueExp(loc0, parser, valueExp);
        }
    }


}
