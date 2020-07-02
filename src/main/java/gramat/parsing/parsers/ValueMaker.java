package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.actuators.*;
import gramat.builtin.IdentityParser;
import gramat.builtin.NullParser;
import gramat.builtin.TrueParser;
import gramat.parsing.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class ValueMaker {

    public static RawAutomaton make(Parser parser, Reader reader, String keyword, String nameLit, RawAutomaton nameExp, RawAutomaton valueExp) {
        switch (keyword) {
            case Mark.SET_KEYWORD:
                if (nameLit != null) {
                    return new RawAttribute(nameLit, valueExp);
                }
                else if (nameExp == null) {
                    throw reader.error("Expected name");
                }

                return new RawDynAttribute(nameExp, valueExp);
            case Mark.OBJECT_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    throw reader.error("not implemented dynamic types");
                }

                var type = parser.options.getType(nameLit);

                return new RawObject(valueExp, type);
            case Mark.LIST_KEYWORD:
                if (nameExp != null && nameLit == null) {
                    throw reader.error("not implemented dynamic types");
                }

                return new RawList(valueExp, parser.options.getType(nameLit));
            case Mark.JOIN_KEYWORD:
                if (nameExp != null) {
                    throw reader.error("Unexpected name");
                }
                return new RawJoin(valueExp);
            case Mark.NULL_KEYWORD:
                if (nameExp != null) {
                    throw reader.error("Unexpected name");
                }
                return new RawValue(valueExp, new NullParser());
            case Mark.TRUE_KEYWORD:
                if (nameExp != null) {
                    throw reader.error("Unexpected name");
                }
                return new RawValue(valueExp, new TrueParser());
            case Mark.MAP_KEYWORD:
                if (nameLit == null) {
                    throw reader.error("expected replacement.");
                }
                else if (nameExp != null) {
                    throw reader.error("dynamic mappings are not implemented.");
                }
                return new RawValue(valueExp, new IdentityParser(nameLit));
            default:
                if (nameExp != null) {
                    throw reader.error("Unexpected parser name: " + nameLit);
                }

                var valueParser = parser.options.getParser(keyword);

                if (valueParser == null) {
                    throw reader.error("Unsupported parser: " + keyword);
                }

                return new RawValue(valueExp, valueParser);
        }
    }

}
