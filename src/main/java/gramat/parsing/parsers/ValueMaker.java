package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.engine.parsers.IdentityParser;
import gramat.engine.parsers.NullParser;
import gramat.engine.parsers.TrueParser;
import gramat.expressions.Expression;
import gramat.expressions.capturing.*;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class ValueMaker {

    public static Expression make(Grammar grammar, Reader reader, String keyword, String nameLit, Expression nameExp, Expression valueExp) {
        switch (keyword) {
            case Mark.SET_KEYWORD:
                if (nameLit != null) {
                    return new CAttribute(nameLit, valueExp);
                }
                else if (nameExp == null) {
                    throw new TextException("Expected name", reader.getLocation());
                }

                return new CAttributeDynamic(nameExp, valueExp);
            case Mark.OBJECT_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    throw new TextException("not implemented dynamic types", reader.getLocation());
                }

                var type = grammar.options.getType(nameLit);

                return new CObject(valueExp, type);
            case Mark.LIST_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    throw new TextException("not implemented dynamic types", reader.getLocation());
                }

                return new CList(valueExp, grammar.options.getType(nameLit));
            case Mark.JOIN_KEYWORD:
                if (nameExp != null) {
                    throw new TextException("Unexpected name", reader.getLocation());
                }
                return new CJoin(valueExp);
            case Mark.NULL_KEYWORD:
                if (nameExp != null) {
                    throw new TextException("Unexpected name", reader.getLocation());
                }
                return new CValue(valueExp, new NullParser());
            case Mark.TRUE_KEYWORD:
                if (nameExp != null) {
                    throw new TextException("Unexpected name", reader.getLocation());
                }
                return new CValue(valueExp, new TrueParser());
            case Mark.MAP_KEYWORD:
                if (nameLit == null) {
                    throw new TextException("expected replacement.", reader.getLocation());
                }
                else if (nameExp != null) {
                    throw new TextException("dynamic mappings are not implemented.", reader.getLocation());
                }
                return new CValue(valueExp, new IdentityParser(nameLit));
            default:
                if (nameExp != null) {
                    throw new TextException("Unexpected parser name: " + nameLit, reader.getLocation());
                }

                var valueParser = grammar.options.getParser(keyword);

                if (valueParser == null) {
                    throw new TextException("Unsupported parser: " + keyword, reader.getLocation());
                }

                return new CValue(valueExp, valueParser);
        }
    }

}
