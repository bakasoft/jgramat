package org.bakasoft.gramat;

import org.bakasoft.gramat.diff.Comparator;
import org.bakasoft.gramat.diff.Diff;
import org.bakasoft.gramat.diff.DiffException;
import org.bakasoft.gramat.elements.Context;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.*;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;
import org.bakasoft.gramat.util.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class Gramat {

    private final ArrayList<GRule> rules;
    private final ArrayList<GTest> tests;

    // TODO ensure that parser and type names are not duplicated
    private final HashMap<String, Function<String,?>> parsers;
    private final HashMap<String, Class<?>> typeMap;

    private Function<String, Class<?>> typeResolver;

    public Gramat() {
        this.rules = new ArrayList<>();
        this.tests = new ArrayList<>();
        this.parsers = new HashMap<>();
        this.typeMap = new HashMap<>();

        // built-in parsers
        DefaultParsers.init(parsers);
    }

    public void clear() {
        rules.clear();
        tests.clear();
        parsers.clear();
        typeMap.clear();
        typeResolver = null;
    }

    public Class<?> getType(String name) {
        Class<?> type = typeMap.get(name);

        if (type != null) {
            return type;
        }

        if (typeResolver != null) {
            type = typeResolver.apply(name);

            if (type != null) {
                return type;
            }
        }

        return null;
    }

    public <T> void addType(Class<T> type) {
        addType(type.getSimpleName(), type);
    }

    public void addType(String name, Class<?> type) {
        if (typeMap.containsKey(name)) {
            throw new RuntimeException("already defined type " + name);
        }

        typeMap.put(name, type);
    }

    public Function<String, Class<?>> getTypeResolver() {
        return typeResolver;
    }

    public void setTypeResolver(Function<String, Class<?>> typeResolver) {
        this.typeResolver = typeResolver;
    }

    public Function<String, ?> getParser(String name) {
        Function<String, ?> parser = parsers.get(name);

        if (parser != null) {
            return parser;
        }

        return null;
    }

    public <T> void addParser(Class<T> type, Function<String, T> parser) {
        addParser(type.getSimpleName(), parser);
    }

    public void addParser(String name, Function<String, ?> parser) {
        if (parsers.containsKey(name)) {
            throw new RuntimeException("parser already added: " + name);
        }

        parsers.put(name, parser);
    }

    public Map<String, Element> compile() {
        HashMap<String, Element> compiled = new HashMap<>();

        // simplify & compile
        for (int i = 0; i < rules.size(); i++) {
            GRule simpleRule = rules.get(i).simplify();

            rules.set(i, simpleRule);

            Element element = simpleRule.compile(this, compiled);

            compiled.put(simpleRule.name, element);
        }

        // link
        for (Map.Entry<String, Element> entry : compiled.entrySet()) {
            Element linked = entry.getValue().link();

            entry.setValue(linked);
        }

        return compiled;
    }

    public void test() {
        Gramat testGramat = new Gramat();
        testGramat.clear();

        testGramat.rules.addAll(rules);

        Map<String, Element> elements = testGramat.compile();
        Comparator comparator = new Comparator(value -> {
            if (value instanceof GMap) {
                return ((GMap)value).getMap();
            }
            else if (value instanceof GArray) {
                return ((GArray)value).getList();
            }
            else if (value instanceof GToken) {
                return ((GToken)value).content;
            }

            return value;
        });

        for (GTest test : tests) {
            Element rule = elements.get(test.rule);

            if (rule == null) {
                throw new RuntimeException("rule not found: " + test.rule);
            }

            Tape tape = new Tape("test", test.input);
            Object actual = rule.capture(tape);
            Diff diff = comparator.diff(test.output, actual);

            if (diff != null) {
                throw new DiffException(diff);
            }
        }
    }

    public List<GRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public List<GTest> getTests() {
        return Collections.unmodifiableList(tests);
    }

    public boolean containsRule(String name) {
        return rules.stream().anyMatch(rule -> Objects.equals(rule.name, name));
    }

    public void addRule(GRule rule) {
        if (containsRule(rule.name)) {
            throw new RuntimeException("rule already exists: " + rule.name);
        }

        rules.add(rule);
    }

    public GRule findRule(String name) {
        GRule rule = rules.stream()
                .filter(r -> Objects.equals(r.name, name))
                .findFirst()
                .orElse(null);

        if (rule == null) {
            throw new RuntimeException("rule not found: " + name);
        }

        return rule;
    }

    public void addTest(GTest test) {
        tests.add(test);
    }

    public void load(String file) {
        load(Paths.get(file));
    }

    public void load(Path path) {
        load(path, FileHelper.createPathResolver(path));
    }

    public void load(Path path, PathResolver resolver) {
        GGramat.readGrammarInto(this, resolver, Tape.fromPath(path));
    }

    public void eval(String code) {
        eval(code, path -> null);
    }

    public void eval(String code, PathResolver resolver) {
        GGramat.readGrammarInto(this, resolver, new Tape(null, code));
    }

    // parsing
}
