package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.expressions.flat.CharAutomaton;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.ArrayList;

public class TextParsers {

    public static Expression parseLiteral(ParseContext context, Source source) {
        var pos0 = source.getPosition();
        var value = BaseParsers.readString(source, Mark.LITERAL_DELIMITER);

        if (value == null) {
            return null;
        }

        return new CharAutomaton(new Location(source, pos0), value);
    }

    public static Expression parsePredicate(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.PREDICATE_DELIMITER)) {
            return null;
        }

        var items = new ArrayList<Expression>();

        boolean expectMore = false;

        while (source.alive()) {
            var begin = BaseParsers.readStringChar(source, Mark.PREDICATE_DELIMITER);

            if (begin == null) {
                if (expectMore) {
                    throw source.error("expected more");
                }
                break;
            }

            var sep = BaseParsers.readStringChar(source, Mark.PREDICATE_DELIMITER);

            if (sep == null || sep == Mark.PREDICATE_ITEM_SEPARATOR) {
                var location = source.locationOf(source.getPosition() - 1);

                items.add(new CharAutomaton(location, begin));

                expectMore = (sep != null);
            }
            else if (sep == Mark.PREDICATE_RANGE_SEPARATOR) {
                var end = BaseParsers.readStringChar(source, Mark.PREDICATE_DELIMITER);

                if (end == null) {
                    throw source.error("expected end char");
                }

                var location = source.locationOf(source.getPosition() - 3);

                items.add(new CharAutomaton(location, begin, end));

                var sep2 = BaseParsers.readStringChar(source, Mark.PREDICATE_DELIMITER);

                if (sep2 == null) {
                    break;
                }
                else if (sep2 == Mark.PREDICATE_ITEM_SEPARATOR) {
                    expectMore = true;
                }
                else {
                    throw source.error("Invalid separator: " + sep);
                }
            }
            else {
                throw source.error("Invalid separator: " + sep);
            }
        }

        source.expect(Mark.PREDICATE_DELIMITER);

        if (items.size() == 1){
            return items.get(0);
        }

        return new Alternation(source.locationOf(pos0), items.toArray(Expression[]::new));
    }

}
