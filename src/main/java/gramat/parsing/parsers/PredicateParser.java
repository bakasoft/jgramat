package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawParallelAutomaton;
import gramat.automata.raw.units.RawCharAutomaton;
import gramat.automata.raw.units.RawRangeAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

import java.util.ArrayList;

public class PredicateParser {

    public static RawAutomaton parse(Reader reader) {
        if (!reader.pull(Mark.PREDICATE_DELIMITER)) {
            return null;
        }

        var items = new ArrayList<RawAutomaton>();

        boolean expectMore = false;

        while (reader.isAlive()) {
            var begin = reader.readStringChar(Mark.PREDICATE_DELIMITER);

            if (begin == null) {
                if (expectMore) {
                    throw reader.error("expected more");
                }
                break;
            }

            var sep = reader.readStringChar(Mark.PREDICATE_DELIMITER);

            if (sep == null || sep == Mark.PREDICATE_ITEM_SEPARATOR) {
                items.add(new RawCharAutomaton(begin));

                expectMore = (sep != null);
            }
            else if (sep == Mark.PREDICATE_RANGE_SEPARATOR) {
                var end = reader.readStringChar(Mark.PREDICATE_DELIMITER);

                if (end == null) {
                    throw reader.error("expected end char");
                }

                items.add(new RawRangeAutomaton(begin, end));

                var sep2 = reader.readStringChar(Mark.PREDICATE_DELIMITER);

                if (sep2 == null) {
                    break;
                }
                else if (sep2 == Mark.PREDICATE_ITEM_SEPARATOR) {
                    expectMore = true;
                }
                else {
                    throw reader.error("Invalid separator: " + sep);
                }
            }
            else {
                throw reader.error("Invalid separator: " + sep);
            }
        }

        if (!reader.pull(Mark.PREDICATE_DELIMITER)) {
            throw reader.error("expected predicate delimiter");
        }


        if (items.size() == 1){
            return items.get(0);
        }

        return new RawParallelAutomaton(items);
    }

}
