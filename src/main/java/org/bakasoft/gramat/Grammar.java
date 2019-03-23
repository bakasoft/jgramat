package org.bakasoft.gramat;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.ExpressionData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Grammar {

    private final HashMap<String, ExpressionData> references;

    private final HashMap<String, Element> elements;

    private final HashMap<String, Class<?>> typeMap;

    private final HashMap<ExpressionData, Element> settled;

    private Function<String, Class<?>> typeResolver;

    private final HashMap<Class<?>, Function<Object, ?>> parsers;

    public Grammar() {
        references = new HashMap<>();
        typeMap = new HashMap<>();
        elements = new HashMap<>();
        settled = new HashMap<>();
        this.parsers = new HashMap<>();
    }

    public <T> void setParser(Class<T> type, Function<Object, T> parser) {
        parsers.put(type, parser);
    }

    public Function<Object, ?> getParser(Class<?> type) {
        Function<Object, ?> parser = parsers.get(type);

        if (parser != null) {
            return parser;
        }

        // built-in conversions
        parser = DefaultConversions.getDefaultParser(type);

        if (parser == null) {
            throw new RuntimeException("converter not found " + type);
        }

        return parser;
    }

    public Class<?> resolveType(String name) {
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

        type = DefaultConversions.getDefaultType(name);

        if (type == null) {
            throw new RuntimeException(name  + " could not be resolved");
        }

        return type;
    }

    public void defineElement(String name, Element element) {
        if (elements.containsKey(name)) {
            throw new RuntimeException("already defined element " + name);
        }

        elements.put(name, element);
    }

    public <T> void defineType(Class<T> type) {
        defineType(type.getSimpleName(), type);
    }

    public <T> void defineType(Class<T> type, Function<Object, T> parser) {
        defineType(type.getSimpleName(), type);
        setParser(type, parser);
    }

    public void defineType(String name, Class<?> type) {
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

    public void addElement(String name, ExpressionData element) {
        if (references.containsKey(name)) {
            throw new GrammarException();
        }

        references.put(name, element);
    }

    public Element resolveElement(String name) {
        Element element = elements.get(name);

        if (element != null) {
            return element;
        }

        ExpressionData expdat = references.get(name);

        if (expdat == null) {
            throw new RuntimeException("element not found: " + name);
        }

        element = settle(expdat);

        elements.put(name, element);

        return element;
    }

    public Element settle(ExpressionData data) {
        Element element = settled.get(data);

        if (element != null) {
            return element;
        }

        element = data._settle(this);

        settled.put(data, element);

        return element;
    }

    public void addElement(ExpressionData data, Element element) {
        settled.put(data, element);
    }

    public Element findElement(String name) {
        Element element = elements.get(name);

        if (element == null) {
            throw new RuntimeException("not found: " + name);
        }

        return element;
    }
}
