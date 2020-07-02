package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Alternation;
import gramat.expressions.Expression;
import gramat.expressions.LiteralChar;
import gramat.expressions.LiteralRange;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

import java.util.ArrayList;

public class PredicateParser {

    public static Expression parse(Reader reader) {
        if (!reader.pull(Mark.PREDICATE_DELIMITER)) {
            return null;
        }

        var items = new ArrayList<Expression>();

        boolean expectMore = false;

        while (reader.isAlive()) {
            var begin = reader.readStringChar(Mark.PREDICATE_DELIMITER);

            if (begin == null) {
                if (expectMore) {
                    throw new TextException("expected more", reader.getLocation());
                }
                break;
            }

            var sep = reader.readStringChar(Mark.PREDICATE_DELIMITER);

            if (sep == null || sep == Mark.PREDICATE_ITEM_SEPARATOR) {
                items.add(new LiteralChar(begin));

                expectMore = (sep != null);
            }
            else if (sep == Mark.PREDICATE_RANGE_SEPARATOR) {
                var end = reader.readStringChar(Mark.PREDICATE_DELIMITER);

                if (end == null) {
                    throw new TextException("expected end char", reader.getLocation());
                }

                items.add(new LiteralRange(begin, end));

                var sep2 = reader.readStringChar(Mark.PREDICATE_DELIMITER);

                if (sep2 == null) {
                    break;
                }
                else if (sep2 == Mark.PREDICATE_ITEM_SEPARATOR) {
                    expectMore = true;
                }
                else {
                    throw new TextException("Invalid separator: " + sep, reader.getLocation());
                }
            }
            else {
                throw new TextException("Invalid separator: " + sep, reader.getLocation());
            }
        }

        if (!reader.pull(Mark.PREDICATE_DELIMITER)) {
            throw new TextException("expected predicate delimiter", reader.getLocation());
        }


        if (items.size() == 1){
            return items.get(0);
        }

        return new Alternation(items);
    }

}
