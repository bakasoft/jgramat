package org.bakasoft.gramat;

import org.bakasoft.gramat.elements.CharRange;
import org.bakasoft.gramat.elements.Element;

import org.bakasoft.gramat.elements.TypeElement;
import org.bakasoft.gramat.elements.ValueElement;
import org.bakasoft.gramat.parsing.*;

public class Gramat {

    private final Grammar grammar;
    private final Element root;

    public Gramat() {
        grammar = new Grammar();
        grammar.defineElement("whitespace", new CharRange("whitespace", c -> c == ' ' || c == '\n' || c == '\r' || c == '\t'));
        grammar.defineElement("letter", new CharRange("letter", c -> c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '-' || c == '@' || c == '$'));
        grammar.defineElement("digit", new CharRange("digit", c -> c >= '0' && c <= '9'));
        grammar.setParser(PropertyMode.class, text -> {
            if (":".equals(text)) {
                return PropertyMode.SET;
            }
            else if ("+:".equals(text)) {
                return PropertyMode.ADD;
            }
            else {
                throw new RuntimeException("unknown property mode: " + text);
            }
        });

        GrammarBuilder g = new GrammarBuilder(grammar);

        Element whitespace = g.ref("whitespace");
        Element letter = g.ref("letter");
        Element digitChar = g.ref("digit");

        Element cr = g.chr('\r');
        Element lf = g.chr('\n');
        Element slash = g.chr('/');
        Element asterisk = g.chr('*');
        Element dot = g.chr('.');
        Element semicolon = g.chr(';');

        Element exclamation = g.chr('!');
        Element quote = g.chr('"');
        Element invertedSlash = g.chr('\\');

        Element newLine = g.alt(g.seq(g.opt(cr), lf), g.end());
        Element inlineComment = g.seq(slash, slash, g.rep(g.not(newLine)), newLine);
        Element blockComment = g.seq(slash, asterisk, g.rep(g.not(asterisk, slash)), asterisk, slash);
        Element blank = g.alt(whitespace, inlineComment, blockComment);
        Element gap = g.rep(whitespace);

        Element setOp = g.chr(':');
        Element addOp = g.sym("+:");

        ////// from here all rules must end in gap

        Element end = g.end();
        Element angleOpen = g.seq(g.chr('<'), gap);
        Element angleClose = g.seq(g.chr('>'), gap);
        Element groupOpen = g.seq(g.chr('('), gap);
        Element groupClose = g.seq(g.chr(')'), gap);
        Element bracketOpen = g.seq(g.chr('['), gap);
        Element bracketClose = g.seq(g.chr(']'), gap);
        Element comma = g.seq(g.chr(','), gap);
        Element braceOpen = g.seq(g.chr('{'), gap);
        Element braceClose = g.seq(g.chr('}'), gap);
        Element pipe = g.seq(g.chr('|'), gap);
        Element equal = g.seq(g.chr('='), gap);
        Element tilde = g.seq(g.chr('~'), gap);

        ValueElement name = g.value(String.class, g.seq(letter, g.rep(g.alt(letter, digitChar))));
        ValueElement number = g.value(Integer.class, g.rep(digitChar, 1));
        ValueElement propMode = g.value(PropertyMode.class, g.alt(addOp, setOp));

        TypeElement exprList = g.type(ExpressionListData.class,
                g.rep(g.add("items", g.ref("expression")), 1));

        // symbol = "\"" !"\"" "\""
        TypeElement symbol = g.type(SymbolData.class,
                g.seq(
                        quote,
                        g.set("content", g.rep(g.not(quote))),
                        quote, gap));

        // reference = name
        TypeElement reference = g.type(ReferenceData.class,
                g.seq(g.set("name", name), gap));

        // property = name (add-op | set-op) [ name ] "<" expression ">"
        TypeElement property = g.type(PropertyData.class,
                g.seq(
                        angleOpen, gap,
                        g.set("name", name), gap,
                        g.set("mode", propMode), gap,
                        g.set("expression", exprList), angleClose, gap));

        // negation = "!" element
        TypeElement negation = g.type(NegationData.class,
                g.seq(exclamation, g.set("expression", exprList)));

        // group = "(" expression ")"
        TypeElement group = g.type(GroupData.class,
                g.seq(
                        groupOpen,
                        g.set("expression", exprList),
                        groupClose));

        // optional = "[" expression "]"
        TypeElement optional = g.type(OptionalData.class,
                g.seq(
                        bracketOpen,
                        g.set("expression", exprList),
                        bracketClose));

        // repetition = "{" [ number [ "," number ] ] expression [ "~" expression ] "}"
        TypeElement repetition = g.type(RepetitionData.class,
                g.seq(
                        braceOpen,
                        g.opt(g.set("minimum", number), gap, g.opt(comma, g.set("maximum", number), gap)),
                        g.set("expression", exprList),
                        g.opt(comma, gap, g.set("separator", exprList)),
                        braceClose));

        TypeElement alternation = g.type(AlternationMarkData.class, g.seq(pipe, gap));

        Element expression = g.alt(
                alternation,
                symbol,
                property,
                negation,
                optional,
                repetition,
                reference,
                group);

        grammar.defineElement("expression", expression);

        TypeElement rule = g.type(RuleData.class,
                g.seq(
                        g.sym("rule"), gap,
                        g.set("name", name), gap,
                        equal,
                        g.set("expression", exprList),
                        semicolon, gap));

        TypeElement value = g.type(ValueData.class,
                g.seq(
                        g.sym("value"), gap,
                        g.set("name", name), gap,
                        equal,
                        g.set("expression", exprList),
                        semicolon, gap));

        TypeElement type = g.type(TypeData.class,
                g.seq(
                        g.sym("type"), gap,
                        g.set("name", name), gap,
                        equal,
                        g.set("expression", exprList),
                        semicolon, gap));

        Element elems = g.alt(rule, value, type);

        TypeElement grammarData = g.type(GrammarData.class,
                g.seq(gap, g.rep(g.add("elements", elems)), end));

        root = grammarData.optimize();
    }

    public void load(Grammar g, Tape tape) {
        GrammarData data = (GrammarData)root.capture(tape);

        for (ElementData item : data.getElements()) {
            String name = item.getName();
            Element element = g.settle(item);

            g.defineElement(name, element);
        }
    }

}
