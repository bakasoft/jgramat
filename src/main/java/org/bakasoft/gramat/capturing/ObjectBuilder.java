package org.bakasoft.gramat.capturing;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class ObjectBuilder {

    private final Stack<ArrayList<Edit>> stack;

    public ObjectBuilder() {
        stack = new Stack<>();

        stack.push(new ArrayList<>());
    }

    public void openObject(Class<?> type) {
        stack.peek().add(new OpenObject(type));
    }

    public void pushObject() {
        stack.peek().add(new PushObject());
    }

    public void openList(Class<?> type) {
        stack.peek().add(new OpenList(type));
    }

    public void pushList() {
        stack.peek().add(new PushList());
    }

    public void pushValue(String value) {
        stack.peek().add(new PushValue(value, null));
    }

    public void pushValue(String value, Function<String, ?> parser) {
        stack.peek().add(new PushValue(value, parser));
    }

    public void popValue(String name, boolean appendMode) {
        stack.peek().add(new PopValue(name, appendMode));
    }

    public void beginTransaction() {
        stack.push(new ArrayList<>());
    }

    public void commitTransaction() {
        ArrayList<Edit> items = stack.pop();

        stack.peek().addAll(items);
    }

    public void rollbackTransaction() {
        stack.pop();
    }

    public Object pop() {
        if (stack.isEmpty()) {
            return null;
        }

        ArrayList<Edit> edits = stack.pop();
        Stack<ObjectWrapper> wrappers = new Stack<>();
        Stack<Object> values = new Stack<>();

        for (Edit edit : edits) {
            edit.compile(wrappers, values);
        }

        if (values.isEmpty()) {
            return null;
        }
        else if (values.size() == 1) {
            return values.get(0);
        }
        else {
            throw new RuntimeException("too many results");
        }
    }

    abstract private static class Edit {

        abstract void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values);
    }

    private static class OpenObject extends Edit {
        final Class<?> objectType;

        OpenObject(Class<?> objectType) {
            this.objectType = objectType;
        }

        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            ObjectWrapper wrapper;

            if (objectType != null) {
                wrapper = new TypedWrapper(objectType);
            }
            else {
                wrapper = new DefaultMapWrapper();
            }

            wrappers.push(wrapper);
        }
    }

    public static class OpenList extends Edit {
        final Class<?> listType;

        OpenList(Class<?> listType) {
            this.listType = listType;
        }

        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            ObjectWrapper wrapper;

            if (listType != null) {
                wrapper = new TypedWrapper(listType);
            }
            else {
                wrapper = new DefaultListWrapper();
            }

            wrappers.push(wrapper);
        }
    }

    private static class PushObject extends Edit {
        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            if (wrappers.isEmpty()) {
                throw new RuntimeException();
            }

            ObjectWrapper wrapper = wrappers.pop();

            values.push(wrapper.getInstance());
        }
    }

    private static class PushList extends Edit { // TODO is it the same than PushObject?
        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            if (wrappers.isEmpty()) {
                throw new RuntimeException();
            }

            ObjectWrapper wrapper = wrappers.pop();

            values.push(wrapper.getInstance());
        }
    }

    private static class PushValue extends Edit {
        final String text;
        final Function<String, ?> parser;

        PushValue(String text, Function<String, ?> parser) {
            this.parser = parser;
            this.text = text;
        }

        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            Object value;

            if (parser != null) {
                value = parser.apply(text);
            }
            else {
                value = text;
            }

            values.push(value);
        }
    }

    private static class PopValue extends Edit {
        final String name;
        final boolean appendMode;

        PopValue(String name, boolean appendMode) {
            this.name = name;
            this.appendMode = appendMode;
        }

        @Override
        void compile(Stack<ObjectWrapper> wrappers, Stack<Object> values) {
            if (values.isEmpty() || wrappers.isEmpty()) {
                throw new RuntimeException();
            }

            Object value = values.pop();
            ObjectWrapper wrapper = wrappers.peek();

            if (appendMode) {
                wrapper.addValue(name, value);
            }
            else {
                wrapper.setValue(name, value);
            }
        }
    }

}
