package gramat.runtime;

import gramat.GramatException;
import gramat.compiling.ValueParser;
import gramat.expressions.Expression;
import gramat.expressions.NamedExpression;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;
import gramat.values.*;

import java.util.*;
import java.util.function.Function;

public class EvalContext {

    private final Map<String, Class<?>> typeMapping;

    public final Source source;

    private int debugTabs = 0;
    public boolean debugMode = false;
    public boolean softMode = false;

    public int lastCommitPosition;

    public String lastCommitName;

    public Node root;
    public Node current;

    private StackItem stack;

    private final HashMap<Expression, HashSet<Integer>> circuitState;

    public EvalContext(Source source, Map<String, Class<?>> typeMapping) {
        this.source = source;
        this.typeMapping = typeMapping;
        this.circuitState = new HashMap<>();
    }

    public boolean enter(Expression expression, int position) {
        var positions = circuitState.get(expression);

        if (positions != null) {
            return positions.add(position);
        }

        positions = new HashSet<>();
        circuitState.put(expression, positions);
        return positions.add(position);
    }

    public void remove(Expression expression, int position) {
        var positions = circuitState.get(expression);

        if (positions != null) {
            positions.remove(position);
        }
    }

    public Class<?> getType(String name) {
        return typeMapping.get(name);
    }

    public void begin(Expression expression) {
        stack = new StackItem(current, stack);

        if (debugMode && !softMode) {
            printDebugLine(">>", expression);
            debugTabs++;
        }
    }

    public void commit(Expression expression) {
        var pos = source.getPosition();
        if (pos > lastCommitPosition) {
            if (expression instanceof NamedExpression) {
                lastCommitName = ((NamedExpression)expression).getName();
            }
            else {
                lastCommitName = expression.getDescription();
            }

            lastCommitPosition = pos;
        }

        stack = stack.previous;

        if (debugMode && !softMode) {
            debugTabs--;
            printDebugLine("**", expression);
        }
    }

    public void rollback(Expression expression) {
        stack = stack.previous;
        current = stack != null ? stack.node : null;

        if (current != null) {
            current.next = null;
        }

        if (debugMode && !softMode) {
            debugTabs--;
            printDebugLine("!!", expression);
        }
    }

    public void printDebugLine(String action, Expression expression) {
        StringBuilder sample = new StringBuilder();

        int pos = source.getPosition();

        for (int i = pos - 10; i <= pos + 9; i++) {
            if (i >= 0 && i < source.getLength()) {
                var c = source.getChar(i);

                if (Character.isISOControl(c)) {
                    c = ' ';
                }

                if (i == pos) {
                    sample.append('[');
                    sample.append(c);
                    sample.append(']');
                }
                else if (i == pos - 1) {
                    sample.append(c);
                }
                else {
                    sample.append(c);
                    sample.append(' ');
                }
            }
            else if (i == pos) {
                sample.append("[ ]");
            }
            else if (i == pos - 1) {
                sample.append(" ");
            }
            else {
                sample.append("  ");
            }
        }

        System.out.println(sample + "| " + " ".repeat(debugTabs) + action + " - " + expression.getDescription());

        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
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

    public EvalContext createEmptyContext() {
        return new EvalContext(source, typeMapping);
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

    private static class Node {

        public final Edit edit;

        public Node next;

        public Node(Edit edit) {
            this.edit = edit;
        }

    }

    private static class StackItem {

        public final Node node;
        public final StackItem previous;

        public StackItem(Node node, StackItem previous) {
            this.node = node;
            this.previous = previous;
        }

    }
}
