package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

import java.util.ArrayList;
import java.util.Map;

public class CoreParsers {

    public static boolean parseNamedExpression(ParseContext context, Source source, Map<String, Expression> rules) {
        int pos0 = source.getPosition();
        String keyword;

        if (source.pull(Mark.VALUE_MARK)) {
            keyword = BaseParsers.readKeyword(source);

            if (keyword == null) {
                throw source.error("Expected keyword");
            }

            BaseParsers.skipBlanks(source);
        }
        else {
            keyword = null;
        }

        String name = BaseParsers.readKeyword(source);

        if (name == null) {
            source.setPosition(pos0);
            return false;
        }

        BaseParsers.skipBlanks(source);

        boolean soft;

        if (source.pull(Mark.HARD_ASSIGNMENT_MARK)) {
            soft = false;
        }
        else if (source.pull(Mark.SOFT_ASSIGNMENT_MARK)) {
            soft = true;
        }
        else {
            source.setPosition(pos0);
            return false;
        }

        BaseParsers.skipBlanks(source);

        Expression expression = parseExpression(context, source);

        if (expression == null) {
            throw source.error("Expected an expression");
        }

        var location = source.locationOf(pos0);

        if (keyword != null) {
            expression = ValueParsers.makeValue(context, location, keyword, name, null, expression);
        }

        if (rules.containsKey(name)) {
            throw new ParseException("Duplicated expression name: " + name, location);
        }
        
        rules.put(name, expression);

        return true;
    }

    public static Expression parseItem(ParseContext context, Source source) {
        Expression e;

        e = TextParsers.parsePredicate(context, source);
        if (e != null) { return e; }

        e = TextParsers.parseLiteral(context, source);
        if (e != null) { return e; }

        e = OtherParsers.parseReference(context, source);
        if (e != null) { return e; }

        e = OtherParsers.parseBegin(context, source);
        if (e != null) { return e; }

        e = OtherParsers.parseEnd(context, source);
        if (e != null) { return e; }

        e = ValueParsers.parseValue(context, source);
        if (e != null) { return e; }

        e = GroupParsers.parseNegation(context, source);
        if (e != null) { return e; }

        e = GroupParsers.parseOptional(context, source);
        if (e != null) { return e; }

        e = GroupParsers.parseRepetition(context, source);
        if (e != null) { return e; }

        return GroupParsers.parseGroup(context, source);
    }

    public static Expression parseExpression(ParseContext context, Source source) {
        var pos0 = source.getPosition();
        var altSeq = new ArrayList<ArrayList<Expression>>();

        altSeq.add(new ArrayList<>());

        while (source.alive()) {
            var item = parseItem(context, source);

            if (item == null) {
                break;
            }

            altSeq.get(altSeq.size()-1).add(item);

            BaseParsers.skipBlanks(source);

            if (source.pull(Mark.ALTERNATION_MARK)) {
                BaseParsers.skipBlanks(source);

                if (altSeq.get(altSeq.size()-1).size() == 0) {
                    throw source.error("Expected expression.");
                }

                altSeq.add(new ArrayList<>());
            }
        }

        if (altSeq.isEmpty()) {
            throw source.error("Expected expression.");
        }

        var options = new ArrayList<Expression>();

        for (var seq : altSeq) {
            if (seq.isEmpty()) {
                throw source.error("Expected expression.");
            }
            else if (seq.size() == 1) {
                options.add(seq.get(0));
            }
            else{
                var s = new Sequence(seq.get(0).getLocation(), seq.toArray(Expression[]::new));

                options.add(s);
            }
        }

        if (options.isEmpty()) {
            throw source.error("Expected expression.");
        }
        else if (options.size() == 1) {
            return options.get(0);
        }

        return new Alternation(new Location(source, pos0), options.toArray(Expression[]::new));
    }

}
