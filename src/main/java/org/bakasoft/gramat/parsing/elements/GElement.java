package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GStringifier;
import org.bakasoft.gramat.parsing.elements.captures.GCapture;
import org.bakasoft.gramat.parsing.elements.captures.GObject;
import org.bakasoft.gramat.parsing.elements.captures.GNamedProperty;

import java.util.*;

abstract public class GElement {

    abstract public GElement simplify();

    abstract public Element compile(Gramat gramat, Map<String, Element> compiled);

    abstract public boolean isPlain(Gramat gramat);

    // is this method needed
    abstract public boolean isOptional(Gramat gramat);

    public String stringify() {
        return GStringifier.stringify(this);
    }

    public static Element[] compileAll(GElement[] elements, Gramat gramat, Map<String, Element> compiled) {
        return Arrays.stream(elements)
                .map(item -> item.compile(gramat, compiled))
                .toArray(Element[]::new);
    }

    public static boolean areAllPlain(GElement[] elements, Gramat gramat) {
        return Arrays.stream(elements).allMatch(item -> item.isPlain(gramat));
    }

    public static boolean isAnyPlain(GElement[] elements, Gramat gramat) {
        return Arrays.stream(elements).anyMatch(item -> item.isPlain(gramat));
    }

    public static GElement[] simplifyAll(GElement[] elements) {
        ArrayList<GElement> list = new ArrayList<>();

        for (GElement element : elements) {
            if (element != null) {
                GElement simplified = element.simplify();

                if (simplified != null) {
                    list.add(simplified);
                }
            }
        }

        return toArray(list);
    }

    public static GElement[] toArray(List<? extends GElement> elements) {
        return elements.toArray(new GElement[0]);
    }

    // parsing

    public static GElement expectExpression(Tape tape) {
        ArrayList<GElement> expressions = new ArrayList<>();
        ArrayList<GElement> buffer = new ArrayList<>();
        Runnable flushBuffer = () -> {
            if (buffer.isEmpty()) {
                throw new GrammarException("expected some elements", tape.getLocation());
            }
            else if (buffer.size() == 1) {
                expressions.add(buffer.get(0));
            }
            else {
                expressions.add(new GSequence(toArray(buffer)));
            }

            buffer.clear();
        };

        GElement unit;
        boolean createAlternation = false;

        while ((unit = tryExpressionUnit(tape)) != null) {
            if (createAlternation) {
                createAlternation = false;

                flushBuffer.run();
            }

            buffer.add(unit);

            skipVoid(tape);

            if (trySymbol(tape, '|')) {
                skipVoid(tape);

                createAlternation = true;
            }
        }

        if (createAlternation) {
            throw new GrammarException("Expected expression after " + inspect('|'), tape.getLocation());
        }

        flushBuffer.run();

        if (expressions.isEmpty()) {
            throw new GrammarException("Invalid expression", tape.getLocation());
        }
        else if (expressions.size() == 1) {
            return expressions.get(0);
        }

        return new GAlternation(toArray(expressions));
    }

    public static GElement expectExpressionUnit(Tape tape) {
        GElement unit = tryExpressionUnit(tape);

        if (unit == null) {
            throw new GrammarException("Expected expression", tape.getLocation());
        }

        return unit;
    }

    public static GElement tryExpressionUnit(Tape tape) {
        int pos0 = tape.getPosition();

        // check for statements beginning
        if (tryStatementBeginning(tape)) {
            tape.setPosition(pos0);
            return null;
        }
        else if (isLetter(tape)) {
            String name = expectName(tape, "reference or type name");

            skipVoid(tape);

            // check for object
            if (trySymbol(tape, ':')) {
                skipVoid(tape);

                return new GObject(name, expectExpressionUnit(tape));
            }
            // otherwise should be a reference
            else {
                return new GReference(name);
            }
        }
        else if (isChar(tape, '<')) {
            return GCapture.expectCapture(tape);
        }
        else if (isChar(tape, '{')) {
            return GRepetition.expectRepetition(tape);
        }
        else if (isChar(tape, '[')) {
            return GOptional.expectOptional(tape);
        }
        else if (isChar(tape, '!')) {
            return GNegation.expectNegation(tape);
        }
        else if (isChar(tape, '(')) {
            return expectGroup(tape);
        }
        else if (isChar(tape, '"')) {
            return GString.expectString(tape);
        }
        else if (isChar(tape, '`')) {
            return GPredicate.expectPredicate(tape);
        }
        else if (trySymbol(tape, '$')) {
            return new GTerminator();
        }

        // unknown char
        tape.setPosition(pos0);
        return null;
    }

    public static GElement expectGroup(Tape tape) {
        expectSymbol(tape, '(');

        skipVoid(tape);

        GElement expression = expectExpression(tape);

        skipVoid(tape);

        expectSymbol(tape, ')');

        return expression;
    }

    public static boolean trySymbol(Tape tape, char symbol) {
        int pos0 = tape.getPosition();

        if (!tape.alive()) {
            tape.setPosition(pos0);
            return false;
        }

        char actual = tape.peek();

        if (symbol != actual) {
            tape.setPosition(pos0);
            return false;
        }

        tape.moveForward();
        return true;
    }

    public static void expectSymbols(Tape tape, String text) {
        if (!trySymbols(tape, text)) {
            throw new GrammarException("Expected characters " + inspect(text), tape.getLocation());
        }
    }

    public static void expectSymbol(Tape tape, char c) {
        if (!trySymbol(tape, c)) {
            throw new GrammarException("Expected character " + inspect(c), tape.getLocation());
        }
    }

    public static boolean trySymbols(Tape tape, String symbol) {
        int pos0 = tape.getPosition();
        int index = 0;

        while (index < symbol.length()) {
            char expected = symbol.charAt(index);

            if (tape.alive()) {
                char actual = tape.peek();
                if (expected == actual) {
                    index++;
                    tape.moveForward();
                }
                else {
                    tape.setPosition(pos0);
                    return false;
                }
            }
            else {
                tape.setPosition(pos0);
                return false;
            }
        }

        return true;
    }

    public static String expectName(Tape tape, String description) {
        if (isChar(tape, '\'')) {
            return expectQuotedToken(tape, '\'');
        }

        String name = tryName(tape);

        if (name == null) {
            throw new GrammarException("Expected " + description, tape.getLocation());
        }

        return name;
    }

    public static String tryName(Tape tape) {
        int pos0 = tape.getPosition();
        if (isLetter(tape)) {
            StringBuilder name = new StringBuilder();

            while (isLetter(tape) || isDigit(tape)) {
                name.append(tape.peek());
                tape.moveForward();
            }

            return name.toString();
        }

        tape.setPosition(pos0);
        return null;
    }

    public static Integer expectInteger(Tape tape) {
        Integer value = tryInteger(tape);

        if (value == null) {
            throw new GrammarException("Expected integer", tape.getLocation());
        }

        return value;
    }

    public static Integer tryInteger(Tape tape) {
        StringBuilder digits = new StringBuilder();

        while (isDigit(tape)) {
            digits.append(tape.peek());
            tape.moveForward();
        }

        if (digits.length() == 0) {
            return null;
        }

        return Integer.parseInt(digits.toString());
    }

    public static void skipVoid(Tape tape) {
        boolean active;

        do {
            active = false;

            while (isWhitespace(tape)) {
                tape.moveForward();
            }

            if (trySymbols(tape, "//")) {
                active = true;
                while (!trySymbol(tape, '\n')) {
                    if (tape.alive()) {
                        tape.moveForward();
                    }
                    else {
                        break;
                    }
                }
            }
            else if (trySymbols(tape, "/*")) {
                active = true;
                while (!trySymbols(tape, "*/")) {
                    if (tape.alive()) {
                        tape.moveForward();
                    }
                    else {
                        break;
                    }
                }
            }
        }
        while (active);
    }

    public static boolean tryStatementBeginning(Tape tape) {
        int pos0 = tape.getPosition();
        boolean result;

        if (tryName(tape) != null) {
            skipVoid(tape);

            // if there is a name followed by the assignment symbol, it's a rule!
            result = trySymbol(tape, '=');
        }
        else if (trySymbol(tape, '@')) {
            // if there is an @ followed by a name, it's a directive!
            result = tryName(tape) != null;
        }
        else {
            skipVoid(tape);

            result = !tape.alive();
        }

        tape.setPosition(pos0);
        return result;
    }

    public static boolean isLetter(Tape tape) {
        return is(tape, c -> c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '-');
    }

    public static boolean isDigit(Tape tape) {
        return is(tape, c -> c >= '0' && c <= '9');
    }

    public static boolean isWhitespace(Tape tape) {
        return is(tape, GElement::isWhitespace);
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public static boolean isChar(Tape tape, char expected) {
        if (tape.alive()) {
            char actual = tape.peek();

            return actual == expected;
        }

        return false;
    }

    public static boolean is(Tape tape, CharPredicate predicate) {
        if (tape.alive()) {
            char c = tape.peek();

            return predicate.test(c);
        }

        return false;
    }

    public static String inspect(Object obj) {
        if (obj instanceof Character) {
            // TODO escape string
            return "'" + obj + "'";
        }

        return obj.toString();
    }

    public static String expectQuotedToken(Tape tape, char delimiter) {
        String literal = tryQuotedToken(tape, delimiter);

        if (literal == null) {
            throw new RuntimeException("Expected string literal");
        }

        return literal;
    }

    public static String tryQuotedToken(Tape tape, char delimiter) {
        int pos0 = tape.getPosition();
        StringBuilder content = new StringBuilder();

        if (!trySymbol(tape, delimiter)) {
            tape.setPosition(pos0);
            return null;
        }

        while (!isChar(tape, delimiter)) {
            char c = readStringChar(tape);

            content.append(c);
        }

        expectSymbol(tape, delimiter);

        return content.toString();
    }

    public static char readStringChar(Tape tape) {
        char c = tape.peek();
        tape.moveForward();

        if (c == '\\') {
            char escaped = tape.peek();
            tape.moveForward();

            switch (escaped) {
                case '"':
                case '\'':
                case '`':
                case '\\':
                    return '\\';
                case 's':
                    return ' ';
                case 'n':
                    return '\n';
                case 'r':
                    return '\r';
                case 't':
                    return '\t';
                case 'u':
                    return expectCharFromHex(tape);
                default:
                    throw new GrammarException("Invalid escape sequence: " + inspect(c), tape.getLocation());
            }
        }

        return c; // TODO allow only accepted characters
    }

    private static char expectCharFromHex(Tape tape) {
        char[] hex = new char[4];

        for (int i = 0; i < hex.length; i++) {
            char c = tape.peek();
            if (c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F' || c >= '0' && c <= '9') {
                hex[i] = c;

                tape.moveForward();
            }
            else {
                throw new GrammarException("expected hexadecimal character", tape.getLocation());
            }
        }

        return (char)Integer.parseInt(new String(hex), 16);
    }

}
