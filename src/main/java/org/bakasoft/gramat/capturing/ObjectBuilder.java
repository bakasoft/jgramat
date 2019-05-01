package org.bakasoft.gramat.capturing;

import org.bakasoft.gramat.capturing.edits.*;
import org.bakasoft.gramat.capturing.models.DefaultListModel;
import org.bakasoft.gramat.capturing.models.DefaultMapModel;
import org.bakasoft.gramat.capturing.models.TypedListModel;
import org.bakasoft.gramat.capturing.models.TypedObjectModel;
import org.bakasoft.polysynth.Polysynth;
import org.bakasoft.polysynth.schemas.ArraySchema;
import org.bakasoft.polysynth.schemas.ObjectSchema;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class ObjectBuilder {

    private final Polysynth polysynth;
    private final Stack<ArrayList<Edit>> stack;

    public ObjectBuilder() {
        polysynth = new Polysynth();
        stack = new Stack<>();

        stack.push(new ArrayList<>());
    }

    public void openObject(Class<?> type) {
        stack.peek().add(new OpenObject(this, type));
    }

    public void pushObject() {
        stack.peek().add(new PushObject());
    }

    public void openList(Class<?> type) {
        stack.peek().add(new OpenList(this, type));
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
        Stack<ObjectModel> wrappers = new Stack<>();
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

    public ObjectModel createObjectWrapper(Class<?> objectType) {
        ObjectModel wrapper;

        if (objectType != null) {
            ObjectSchema schema = polysynth.getSchema(objectType).toObject();

            wrapper = new TypedObjectModel(schema);
        }
        else {
            wrapper = new DefaultMapModel();
        }

        return wrapper;
    }

    public ObjectModel createListWrapper(Class<?> listType) {
        ObjectModel wrapper;

        if (listType != null) {
            ArraySchema schema = polysynth.getSchema(listType).toArray();

            wrapper = new TypedListModel(schema);
        }
        else {
            wrapper = new DefaultListModel();
        }

        return wrapper;
    }
}
