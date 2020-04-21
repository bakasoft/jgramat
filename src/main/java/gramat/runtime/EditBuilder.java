package gramat.runtime;

import gramat.GramatException;
import gramat.util.parsing.Source;
import gramat.values.*;

import java.util.ArrayList;
import java.util.Stack;

public class EditBuilder {

    public Node root;
    public Node current;

    private final Stack<Node> stack;

    public EditBuilder() {
        stack = new Stack<>();
    }

    public void begin() {
        stack.push(current);
    }

    public void commit() {
        stack.pop();
    }

    public void rollback() {
        current = stack.pop();
        current.next = null;
    }

    public void add(Edit edit) {
        var node = new Node(edit);

        if (current == null) {
            root = node;
        }
        else {
            current.next = node;
        }

        current = node;
    }

    public Object build(Source source) {
        var node = root;

        var bufferStack = new Stack<ArrayList<Value>>();
        var valueStack = new Stack<Value>();

        bufferStack.push(new ArrayList<>());

        while (node != null) {
            if (node.edit instanceof EditOpenWildObject) {
                var edit = (EditOpenWildObject)node.edit;
                valueStack.push(new WildObject(edit.typeName));
                bufferStack.push(new ArrayList<>());
            }
            else if (node.edit instanceof EditOpenWildList) {
                var edit = (EditOpenWildList)node.edit;
                valueStack.push(new WildList(edit.typeName));
                bufferStack.push(new ArrayList<>());
            }
            else if (node.edit instanceof EditOpenTypedObject) {
                var edit = (EditOpenTypedObject)node.edit;
                valueStack.push(new TypedObject(edit.type));
                bufferStack.push(new ArrayList<>());
            }
            else if (node.edit instanceof EditOpenTypedList) {
                var edit = (EditOpenTypedList)node.edit;
                valueStack.push(new TypedList(edit.type));
                bufferStack.push(new ArrayList<>());
            }
            else if (node.edit instanceof EditSet) {
                var edit = (EditSet)node.edit;
                var currentValue = valueStack.peek();

                if (!(currentValue instanceof ObjectValue)) {
                    throw new GramatException("Cannot set to " + currentValue);
                }

                var currentObject = (ObjectValue) currentValue;

                if (bufferStack.isEmpty()) {
                    throw new GramatException("Nothing captured to set");
                }

                var bufferedValues = bufferStack.peek();

                if (bufferedValues.isEmpty()) {
                    throw new GramatException("not captured value to set");
                } else if (bufferedValues.size() > 1) {
                    throw new GramatException("too much values to set");
                }

                var setValue = bufferedValues.get(0);

                bufferedValues.clear();

                currentObject.set(edit.name, setValue);
            }
            else if (node.edit instanceof EditCloseValue) {
                var edit = (EditCloseValue)node.edit;
                var value = valueStack.pop();
                var buffer = bufferStack.pop();

                if (!buffer.isEmpty()) {
                    if (value instanceof ListValue) {
                        var list = (ListValue) value;

                        for (var item : buffer) {
                            list.add(item);
                        }
                    } else {
                        throw new GramatException("expected list");
                    }
                }

                bufferStack.peek().add(value);
            }
            else if (node.edit instanceof EditSendSegment) {
                var edit = (EditSendSegment)node.edit;
                bufferStack.peek().add(new PlainValue(source.extract(edit.pos0, edit.posF), edit.parser));
            }
            else if (node.edit instanceof EditMark) {
                var edit = (EditMark)node.edit;
            }
            else {
                throw new GramatException("not implemented edit: " + node.edit);
            }

            node = node.next;
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

    private static class Node {

        public final Edit edit;

        public Node next;

        public Node(Edit edit) {
            this.edit = edit;
        }

    }

}
