package gramat.runtime;

import gramat.GramatException;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;
import gramat.values.*;

import java.util.ArrayList;
import java.util.List;
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

        if (current != null) {
            current.next = null;
        }
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

    private static Value collapseValues(List<Value> bufferedValues, Location location) {
        if (bufferedValues.isEmpty()) {
            throw new ParseException("missing value", location);
        } else if (bufferedValues.size() == 1) {
            return bufferedValues.get(0);
        }
        else {
            throw new ParseException("too much values!", location);
        }
    }

    public Object build(Source source) {
        var node = root;

        var bufferStack = new Stack<ArrayList<Value>>();
        var valueStack = new Stack<Value>();

        bufferStack.push(new ArrayList<>());

        var tab = 0;

        while (node != null) {
            if (node.edit instanceof EditOpenWildObject) {
                var edit = (EditOpenWildObject)node.edit;
                valueStack.push(new WildObject(edit.typeName));
                bufferStack.push(new ArrayList<>());
                tab++;
            }
            else if (node.edit instanceof EditOpenWildList) {
                var edit = (EditOpenWildList)node.edit;
                valueStack.push(new WildList(edit.typeName));
                bufferStack.push(new ArrayList<>());
                tab++;
            }
            else if (node.edit instanceof EditOpenTypedObject) {
                var edit = (EditOpenTypedObject)node.edit;
                valueStack.push(new TypedObject(edit.type));
                bufferStack.push(new ArrayList<>());
                tab++;
            }
            else if (node.edit instanceof EditOpenTypedList) {
                var edit = (EditOpenTypedList)node.edit;
                valueStack.push(new TypedList(edit.type));
                bufferStack.push(new ArrayList<>());
                tab++;
            }
            else if (node.edit instanceof EditOpenJoin) {
                var edit = (EditOpenJoin)node.edit;
                valueStack.push(new StringJoin());
                bufferStack.push(new ArrayList<>());
                tab++;
            }
            else if (node.edit instanceof EditSet) {
                var edit = (EditSet)node.edit;

                if (valueStack.isEmpty()) {
                    throw new ParseException("empty value stack", edit.location);
                }

                var currentValue = valueStack.peek();

                if (!(currentValue instanceof ObjectValue)) {
                    throw new GramatException("Cannot set to " + currentValue);
                }

                var currentObject = (ObjectValue) currentValue;

                if (bufferStack.isEmpty()) {
                    throw new GramatException("Nothing captured to set");
                }

                var bufferedValues = bufferStack.peek();

                var setValue = collapseValues(bufferedValues, edit.location);

                bufferedValues.clear();

                currentObject.set(edit.name, setValue);
            }
            else if (node.edit instanceof EditCloseValue) {
                tab--;

                var edit = (EditCloseValue)node.edit;
                var value = valueStack.pop();
                var buffer = bufferStack.pop();

                if (value instanceof ContainerValue) {
                    var container = (ContainerValue) value;

                    for (var item : buffer) {
                        container.add(item);
                    }
                }
                else if (!buffer.isEmpty()) {
                    // TODO give info of the unexpected object
                    throw new ParseException("expected list", edit.location);
                }

                bufferStack.peek().add(value);
            }
            else if (node.edit instanceof EditSendSegment) {
                var edit = (EditSendSegment)node.edit;
                bufferStack.peek().add(new PlainValue(source.extract(edit.pos0, edit.posF), edit.parser));
            }
            else if (node.edit instanceof EditSendFragment) {
                var edit = (EditSendFragment)node.edit;
                bufferStack.peek().add(new PlainValue(edit.fragment, null));
            }
            else if (node.edit instanceof EditSendValue) {
                var edit = (EditSendValue)node.edit;
                bufferStack.peek().add(new DirectValue(edit.value));
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
        var result = collapseValues(values, source.getLocation());
        return result.build();
    }

    private static class Node {

        public final Edit edit;

        public Node next;

        public Node(Edit edit) {
            this.edit = edit;
        }

    }

}
