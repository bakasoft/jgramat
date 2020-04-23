package gramat.runtime;

import gramat.expressions.Expression;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.function.Function;

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

    public void sendSegment(int pos0, int posF, String parser) {
        edits.add(new EditSendSegment(source.locationOf(pos0), pos0, posF, parser));
    }

    public void mark(int pos0, int posF, String name) {
        edits.add(new EditMark(source.locationOf(pos0), pos0, posF, name));
    }

    public void set(Location location, String name) {
        edits.add(new EditSet(location, name));
    }
}
