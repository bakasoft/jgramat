package gramat.values;

import gramat.Gramat;
import gramat.GramatException;
import gramat.util.ReflectionTool;

import java.util.ArrayList;
import java.util.Stack;

public class ValueRecipe implements Value {

    private static final int PUSH = 1;
    private static final int POP = 2;
    private static final int SEND = 3;
    private static final int SET = 4;

    private static class Action {
        public final int type;
        public final Value value;
        public final String name;

        private Action(int type, Value value, String name) {
            this.type = type;
            this.value = value;
            this.name = name;
        }
    }

    private final ArrayList<Action> actions = new ArrayList<>();  // TODO make it lazy

    public void push(Value value) {
        actions.add(new Action(PUSH, value, null));
    }

    public void pop() {
        actions.add(new Action(POP, null, null));
    }

    public void send(Value value) {
        actions.add(new Action(SEND, value, null));
    }

    public void set(String name) {
        actions.add(new Action(SET, null, name));
    }

    @Override
    public Object build() {
        var bufferStack = new Stack<ArrayList<Value>>();
        var valueStack = new Stack<Value>();

        bufferStack.push(new ArrayList<>());

        for (var action : actions) {
            switch (action.type) {
                case PUSH:
                    valueStack.push(action.value);
                    bufferStack.push(new ArrayList<>());
                    break;
                case SET:
                    var currentValue = valueStack.peek();

                    if (!(currentValue instanceof ObjectValue)) {
                        throw new GramatException("Cannot set to " + currentValue);
                    }

                    var currentObject = (ObjectValue)currentValue;

                    if (bufferStack.isEmpty()) {
                        throw new GramatException("Nothing captured to set");
                    }

                    var bufferedValues = bufferStack.peek();

                    if (bufferedValues.isEmpty()) {
                        throw new GramatException("not captured value to set");
                    }
                    else if (bufferedValues.size() > 1) {
                        throw new GramatException("too much values to set");
                    }

                    var setValue = bufferedValues.get(0);

                    bufferedValues.clear();

                    currentObject.set(action.name, setValue);
                    break;
                case POP:
                    var value = valueStack.pop();
                    var buffer = bufferStack.pop();

                    if (!buffer.isEmpty()) {
                        if (value instanceof ListValue) {
                            var list = (ListValue) value;

                            for (var item : buffer) {
                                list.add(item);
                            }
                        }
                        else {
                            throw new GramatException("expected list");
                        }
                    }

                    bufferStack.peek().add(value);

                    break;
                case SEND:
                    bufferStack.peek().add(action.value);
                    break;
                default:
                    throw new GramatException("not implemented action: " + action.type);
            }
        }

        var values = bufferStack.pop();

        if (bufferStack.size() > 0) {
            throw new GramatException("too much values for a result");
        }

        if (values.isEmpty()) {
            return null;
        }
        else if (values.size() != 1){
            throw new GramatException("too much values!");
        }

        return values.get(0).build();
    }
}
