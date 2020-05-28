package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawParallelAutomaton;
import gramat.automata.raw.RawSeriesAutomaton;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

import java.util.ArrayList;

public class ExpressionParser {

    private static RawAutomaton parseItem(Parser parser, Reader reader) {
        RawAutomaton e;

        e = PredicateParser.parse(reader);
        if (e != null) { return e; }

        e = LiteralParser.parse(reader);
        if (e != null) { return e; }

        e = BeginParser.parse(reader);
        if (e != null) { return e; }

        e = EndParser.parse(reader);
        if (e != null) { return e; }

        e = WildParser.parse(reader);
        if (e != null) { return e; }

        e = MiniWildParser.parse(reader);
        if (e != null) { return e; }

        e = ReferenceParser.parse(parser, reader);
        if (e != null) { return e; }

        e = ValueParser.parse(parser, reader);
        if (e != null) { return e; }

//        e = NegationParser.parse(parser);
//        if (e != null) { return e; }

        e = OptionalParser.parse(parser, reader);
        if (e != null) { return e; }

        e = RepetitionParser.parse(parser, reader);
        if (e != null) { return e; }

        return GroupParser.parse(parser, reader);
    }

    public static RawAutomaton parse(Parser parser, Reader reader) {
        var altSeq = new ArrayList<ArrayList<RawAutomaton>>();

        altSeq.add(new ArrayList<>());

        while (reader.isAlive()) {
            var item = parseItem(parser, reader);

            if (item == null) {
                break;
            }

            altSeq.get(altSeq.size()-1).add(item);

            reader.skipBlanks();

            if (reader.pull(Mark.ALTERNATION_MARK)) {
                reader.skipBlanks();

                if (altSeq.get(altSeq.size()-1).size() == 0) {
                    throw reader.error("Expected expression.");
                }

                altSeq.add(new ArrayList<>());
            }
        }

        if (altSeq.isEmpty()) {
            throw reader.error("Expected expression.");
        }

        var options = new ArrayList<RawAutomaton>();

        for (var seq : altSeq) {
            if (seq.isEmpty()) {
                throw reader.error("Expected expression.");
            }
            else if (seq.size() == 1) {
                options.add(seq.get(0));
            }
            else{
                var s = new RawSeriesAutomaton(seq);

                options.add(s);
            }
        }

        if (options.isEmpty()) {
            throw reader.error("Expected expression.");
        }
        else if (options.size() == 1) {
            return options.get(0);
        }

        return new RawParallelAutomaton(options);
    }

}
