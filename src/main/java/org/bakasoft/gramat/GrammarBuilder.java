package org.bakasoft.gramat;

import org.bakasoft.gramat.elements.*;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class GrammarBuilder {

    private final Grammar grammar;
    private final HashMap<String, Supplier<Element>> generators;

    public GrammarBuilder() {
        this(new Grammar());
    }

    public GrammarBuilder(Grammar grammar) {
        this.grammar = grammar;
        this.generators = new HashMap<>();
    }

    public Element ref(String name) {
        return new Reference(name, () -> grammar.findElement(name));
    }

    public SingleChar chr(char c) {
        return new SingleChar(c);
    }

    public Alternation alt(Element... elements) {
        return new Alternation(elements);
    }

    public Optional opt(Element element) {
        return new Optional(element);
    }

    public Optional opt(Element... elements) {
        return new Optional(new Sequence(elements));
    }

    public Repetition rep(Element element) {
        return new Repetition(element, 0, 0, null);
    }

    public Repetition rep(Element element, int minimum) {
        return new Repetition(element, minimum, 0, null);
    }

    public Repetition rep(Element element, int minimum, int maximum) {
        return new Repetition(element, minimum, maximum, null);
    }

    public Repetition rep(Element element, Element separator) {
        return new Repetition(element, 0, 0, separator);
    }

    public Repetition rep(Element element, int minimum, Element separator) {
        return new Repetition(element, minimum, 0, separator);
    }

    public Repetition rep(Element element, int minimum, int maximum, Element separator) {
        return new Repetition(element, minimum, maximum, separator);
    }

    public Sequence seq(Element... elements) {
        return new Sequence(elements);
    }

    public Symbol sym(String symbol) {
        return new Symbol(symbol);
    }

    public Negation not(Element element) {
        return new Negation(element);
    }

    public Negation not(Element... elements) {
        return new Negation(new Sequence(elements));
    }

    public Termination end() {
        return new Termination();
    }

    public Property add(String name, Element element) {
        return new Property(name, true, element);
    }

    public Property set(String name, Element element) {
        return new Property(name, false, element);
    }

    public TypeElement type(Class<?> type, Element element) {
        return new TypeElement(type, element);
    }

    public ValueElement value(Class<?> type, Element element) {
        Function<Object, ?> converter = grammar.getParser(type);
        return new ValueElement(type, converter, element);
    }

}
