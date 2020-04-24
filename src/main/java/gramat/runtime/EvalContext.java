package gramat.runtime;

import gramat.compiling.ValueParser;
import gramat.expressions.Expression;
import gramat.expressions.NamedExpression;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Map;
import java.util.function.Function;

public class EvalContext {

    private final Map<String, Class<?>> typeMapping;

    private final EditBuilder edits;

    public final Source source;

    private int debugTabs = 0;

    public boolean debugMode = false;
    public boolean softMode = false;

    public int lastCommitPosition;

    public String lastCommitName;

    public EvalContext(Source source, Map<String, Class<?>> typeMapping) {
        this.source = source;
        this.edits = new EditBuilder();
        this.typeMapping = typeMapping;
    }

    public Class<?> getType(String name) {
        return typeMapping.get(name);
    }

    public void begin(Expression expression) {
        edits.begin();

        if (debugMode && !softMode) {
            printDebugLine("begin", expression);
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
                lastCommitName = expression.toString();
            }

            lastCommitPosition = pos;
        }

        edits.commit();

        if (debugMode && !softMode) {
            debugTabs--;
            printDebugLine("commit", expression);
        }
    }

    public void rollback(Expression expression) {
        edits.rollback();

        if (debugMode && !softMode) {
            debugTabs--;
            printDebugLine("rollback", expression);
        }
    }

    private void printDebugLine(String action, Expression expression) {
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
            else {
                sample.append("  ");
            }
        }

        System.out.println(sample + "| " + " ".repeat(debugTabs) + expression.getDescription() + " - " + action);
    }

    public Object getValue() {
        return edits.build(source);
    }

    private boolean open_edit(Expression expression, Function<Location, Edit> editMaker) {
        var loc0 = source.getLocation();
        edits.add(editMaker.apply(loc0));
        if (expression.eval(this)) {
            var posF = source.getPosition();
            edits.add(new EditCloseValue(source.locationOf(posF)));
            return true;
        }
        return false;
    }

    public boolean useList(Expression expression, Class<?> type) {
        return open_edit(expression, loc -> new EditOpenTypedList(loc, type));
    }

    public boolean useList(Expression expression, String typeName) {
        return open_edit(expression, loc -> new EditOpenWildList(loc, typeName));
    }

    public boolean useObject(Expression expression, Class<?> type) {
        return open_edit(expression, loc -> new EditOpenTypedObject(loc, type));
    }

    public boolean useObject(Expression expression, String typeName) {
        return open_edit(expression, loc -> new EditOpenWildObject(loc, typeName));
    }

    public boolean useJoin(Expression expression) {
        return open_edit(expression, EditOpenJoin::new);
    }

    public void sendFragment(String value) {
        edits.add(new EditSendFragment(source.getLocation(), value));
    }

    public void sendSegment(int pos0, int posF, ValueParser parser) {
        edits.add(new EditSendSegment(source.locationOf(pos0), pos0, posF, parser));
    }

    public void sendNull() {
        edits.add(new EditSendNull(source.getLocation()));
    }

    public void mark(int pos0, int posF, String name) {
        edits.add(new EditMark(source.locationOf(pos0), pos0, posF, name));
    }

    public void set(Location location, String name) {
        edits.add(new EditSet(location, name));
    }

    public EvalContext createEmptyContext() {
        return new EvalContext(source, typeMapping);
    }


}
