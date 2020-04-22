package gramat.runtime;

import gramat.expressions.Expression;
import gramat.util.parsing.Source;

public class EvalContext {

    public final Source source;

    public int debugTabs = 0;

    private final EditBuilder edits;

    public EvalContext(Source source) {
        this.source = source;
        this.edits = new EditBuilder();
    }

    public void begin() {
        edits.begin();
    }

    public void commit() {
        edits.commit();
    }

    public void rollback() {
        edits.rollback();
    }

    public Object getValue() {
        return edits.build(source);
    }

    private boolean open_edit(Expression expression, Edit edit) {
        edits.add(edit);
        if (expression.eval(this)) {
            edits.add(new EditCloseValue());
            return true;
        }
        return false;
    }

    public boolean useList(Expression expression, Class<?> type) {
        return open_edit(expression, new EditOpenTypedList(type));
    }

    public boolean useList(Expression expression, String typeName) {
        return open_edit(expression, new EditOpenWildList(typeName));
    }

    public boolean useObject(Expression expression, Class<?> type) {
        return open_edit(expression, new EditOpenTypedObject(type));
    }

    public boolean useObject(Expression expression, String typeName) {
        return open_edit(expression, new EditOpenWildObject(typeName));
    }

    public void sendFragment(String value) {
        edits.add(new EditSendFragment(value));
    }

    public void sendSegment(int pos0, int posF, String parser) {
        edits.add(new EditSendSegment(pos0, posF, parser));
    }

    public void mark(int pos0, int posF, String name) {
        edits.add(new EditMark(pos0, posF, name));
    }

    public void set(String name) {
        edits.add(new EditSet(name));
    }
}
