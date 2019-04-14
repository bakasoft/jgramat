package org.bakasoft.gramat;

import org.bakasoft.gramat.diff.Comparator;
import org.bakasoft.gramat.diff.Diff;
import org.bakasoft.gramat.diff.DiffException;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsers.Parser;
import org.bakasoft.gramat.parsing.*;
import org.bakasoft.gramat.parsing.elements.producers.GProducer;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.plugins.*;
import org.bakasoft.gramat.schema.Schema;
import org.bakasoft.gramat.util.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class Gramat {

    private final ArrayList<GRule> rules;
    private final ArrayList<GTest> tests;

    private final HashMap<String, Plugin> plugins;

    private Function<String, Class<?>> typeResolver;

    public Gramat() {
        this.rules = new ArrayList<>();
        this.tests = new ArrayList<>();
        this.plugins = new HashMap<>();

        // built-in parsers
        DefaultParsers.init(plugins);
    }

    public Gramat(Gramat base) {
        rules = new ArrayList<>(base.rules);
        tests = new ArrayList<>(base.tests);
        plugins = new HashMap<>(base.plugins);
        typeResolver = base.typeResolver;
    }

    public GExpression findExpression(String name) {
        GExpression expression = getExpression(name);

        if (expression == null) {
            throw new RuntimeException("not found: " + name);
        }

        return expression;
    }

    public GExpression getExpression(String name) {
        for (GRule rule : rules) {
            if (Objects.equals(rule.name, name)) {
                return rule.expression;
            }
        }

        return null;
    }

    public Class<?> getType(String name) {
        Plugin plugin = plugins.get(name);

        if (plugin != null) {
            if (plugin instanceof TypePlugin) {
                return ((TypePlugin)plugin).type;
            }
            else {
                throw new RuntimeException("not a type: " + name);
            }
        }

        if (typeResolver != null) {
            return typeResolver.apply(name);
        }

        return null;
    }

    public <T> void addType(Class<T> type) {
        addType(type.getSimpleName(), type);
    }

    public void addType(String name, Class<?> type) {
        addPlugin(name, new TypePlugin(type));
    }

    public void addPlugin(String name, Plugin plugin) {
        if (plugins.get(name) != null) {
            throw new RuntimeException("already defined plug-in " + name);
        }

        plugins.put(name, plugin);
    }

    public Function<String, Class<?>> getTypeResolver() {
        return typeResolver;
    }

    public void setTypeResolver(Function<String, Class<?>> typeResolver) {
        this.typeResolver = typeResolver;
    }

    public Function<String, ?> getParser(String name) {
        Plugin plugin = plugins.get(name);

        if (plugin != null) {
            if (plugin instanceof ParserPlugin) {
                return ((ParserPlugin)plugin).parser;
            }
            else {
                throw new RuntimeException("not a parser: " + name);
            }
        }

        return null;
    }

    public <T> void addParser(Class<T> type, Function<String, T> parser) {
        addParser(type.getSimpleName(), parser);
    }

    public void addParser(String name, Function<String, ?> parser) {
        addPlugin(name, new ParserPlugin(parser));
    }

    public void addTransformation(String name, Function<String, String> transformation) {
        addPlugin(name, new TransformationPlugin(transformation));
    }

    public Function<String, String> getTransformation(String name) {
        Plugin plugin = plugins.get(name);

        if (plugin != null) {
            if (plugin instanceof TransformationPlugin) {
                return ((TransformationPlugin)plugin).transformation;
            }
            else {
                throw new RuntimeException("not a transformation: " + name);
            }
        }

        return null;
    }

    public Map<String, Element> compile() {
        HashMap<String, Element> compiled = new HashMap<>();

        // TODO simplification should be done immediately after parsing
        // simplify rules
        for (int i = 0; i < rules.size(); i++) {
            GRule rule = rules.get(i).simplify();

            rules.set(i, rule);
        }

        // validate rules
        // TODO should the schema be validated?
        generateSchema();

        // compile rules
        for (GRule rule : rules) {
            rule.validate();

            Element element = rule.compile(compiled);

            compiled.put(rule.name, element);
        }

        // link
        resolveReferences(compiled);

        return compiled;
    }

    private void resolveReferences(HashMap<String, Element> compiled) {
        HashSet<Element> control = new HashSet<>();

        for (Map.Entry<String, Element> entry : compiled.entrySet()) {
            entry.getValue().resolveInto(compiled, control);
        }
    }

    public void removeTypes() {
        for (Map.Entry<String, Plugin> entry : plugins.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof TypePlugin || value instanceof ParserPlugin) {
                entry.setValue(null);
            }
        }
    }

    public void test() {
        Gramat testGramat = new Gramat(this);
        testGramat.removeTypes();

        Map<String, Element> elements = testGramat.compile();
        Comparator comparator = new Comparator();

        for (GTest test : tests) {
            Element rule = elements.get(test.rule);

            if (rule == null) {
                throw new RuntimeException("rule not found: " + test.rule);
            }

            Tape tape = new Tape(test.input);

            try {
                Object actual = rule.capture(tape);

                if (test.failMode) {
                    throw new RuntimeException("Expected to fail: " + actual);
                }

                if (test.output != null) {
                    Diff diff = comparator.diff(test.output, actual);

                    if (diff != null) {
                        throw new DiffException(diff);
                    }
                }
            }
            catch(GrammarException e) {
                if (!test.failMode) {
                    throw e;
                }
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
        Parser.readGrammarInto(this, resolver, Tape.fromPath(path));
    }

    public void eval(String code) {
        eval(code, path -> null);
    }

    public void eval(String code, PathResolver resolver) {
        Parser.readGrammarInto(this, resolver, new Tape(code));
    }

    public void setVariable(String name, Object value) {
        Plugin plugin = plugins.get(name);

        if (plugin == null) {
            addPlugin(name, new PluginVariable(value));
        }
        else if (plugin instanceof PluginVariable) {
            PluginVariable var = (PluginVariable)plugin;

            var.value = value;
        }
        else {
            throw new RuntimeException("plugin is not variable: " +  name);
        }
    }

    public Object getVariable(String name) {
        Plugin plugin = plugins.get(name);

        if (plugin == null) {
            return null;
        }
        else if (plugin instanceof PluginVariable) {
            PluginVariable var = (PluginVariable)plugin;

            return var.value;
        }
        else {
            throw new RuntimeException("plugin is not variable: " +  name);
        }
    }

    public Schema generateSchema() {
        Schema schema = new Schema();
        SchemaControl control = new SchemaControl(schema);

        for (GRule rule : rules) {
            // TODO should this be filtered?
            if (rule.expression.countWildMutations() > 0) {
                rule.expression.generateSchemaType(control, null, null);
            }
        }

        System.out.println(schema.inspect());

        return schema;
    }
}
