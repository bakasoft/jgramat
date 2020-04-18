package gramat.parsers;

import gramat.expressions.*;
import gramat.util.parsing.Source;

public class ValueParsers {

    public static StringLiteralExp parseStringLiteral(Source source) {
        var pos0 = source.getPosition();
        var value = BaseParsers.readString(source, '\'');

        if (value == null) {
            return null;
        }
        return new StringLiteralExp(source.locationOf(pos0), value);
    }

    public static StringExp parseString(Source source) {
        // TODO implement "@string" "(" expression ")"
        return null;
    }

    public static ObjectExp parseObject(Source source) {
        // TODO implement "@object" "(" [ typeName ":"] expression ")"
        return null;
    }

    public static ListExp parseList(Source source) {
        var pos0 = source.getPosition();

        if (!source.pull("@list")) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull('(')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        var expression = CoreParsers.parseExpression(source);

        BaseParsers.skipBlanks(source);

        if (!source.pull(')')) {
            throw source.error("Expected )");
        }

        return new ListExp(source.locationOf(pos0), expression);
    }

    public static Attribute parseAttribute(Source source) {
        var pos0 = source.getPosition();

        if (!source.pull("@set")) {
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull('(')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        var nameExp = CoreParsers.parseExpression(source);

        BaseParsers.skipBlanks(source);

        if (!source.pull(':')) {
            throw source.error("Expected :");
        }

        BaseParsers.skipBlanks(source);

        var valueExp = CoreParsers.parseExpression(source);

        BaseParsers.skipBlanks(source);

        if (!source.pull(')')) {
            throw source.error("Expected )");
        }

        return new Attribute(source.locationOf(pos0), nameExp, valueExp);
    }
}
