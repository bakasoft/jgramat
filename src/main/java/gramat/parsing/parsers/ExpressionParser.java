package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Alternation;
import gramat.expressions.Expression;
import gramat.expressions.Sequence;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

import java.util.ArrayList;

public class ExpressionParser {

    private static Expression parseItem(Grammar grammar, Reader reader) {
        Expression e;

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

        e = ReferenceParser.parse(grammar, reader);
        if (e != null) { return e; }

        e = ValueParser.parse(grammar, reader);
        if (e != null) { return e; }

//        e = NegationParser.parse(parser);
//        if (e != null) { return e; }

        e = OptionalParser.parse(grammar, reader);
        if (e != null) { return e; }

        e = RepetitionParser.parse(grammar, reader);
        if (e != null) { return e; }

        return GroupParser.parse(grammar, reader);
    }

    public static Expression parse(Grammar grammar, Reader reader) {
        var altSeq = new ArrayList<ArrayList<Expression>>();

        altSeq.add(new ArrayList<>());

        while (reader.isAlive()) {
            var item = parseItem(grammar, reader);

            if (item == null) {
                break;
            }

            altSeq.get(altSeq.size()-1).add(item);

            reader.skipBlanks();

            if (reader.pull(Mark.ALTERNATION_MARK)) {
                reader.skipBlanks();

                if (altSeq.get(altSeq.size()-1).size() == 0) {
                    throw new TextException("Expected expression.", reader.getLocation());
                }

                altSeq.add(new ArrayList<>());
            }
        }

        if (altSeq.isEmpty()) {
            throw new TextException("Expected expression.", reader.getLocation());
        }

        var options = new ArrayList<Expression>();

        for (var seq : altSeq) {
            if (seq.isEmpty()) {
                throw new TextException("Expected expression.", reader.getLocation());
            }
            else if (seq.size() == 1) {
                options.add(seq.get(0));
            }
            else{
                var s = new Sequence(seq);

                options.add(s);
            }
        }

        if (options.isEmpty()) {
            throw new TextException("Expected expression.", reader.getLocation());
        }
        else if (options.size() == 1) {
            return options.get(0);
        }

        return new Alternation(options);
    }

}
