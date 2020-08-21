package gramat.engine.actions.capturing.marks;

import gramat.util.ClassUtils;

public class PositionMark implements Mark {

    public final int beginPosition;

    public PositionMark(int beginPosition) {
        this.beginPosition = beginPosition;
    }

    @Override
    public String toString() {
        return ClassUtils.prettyString(this, beginPosition);
    }
}