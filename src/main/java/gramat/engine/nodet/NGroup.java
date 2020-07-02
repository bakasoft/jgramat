package gramat.engine.nodet;

import gramat.engine.Action;
import gramat.engine.nodet.marks.GroupAccepted;
import gramat.engine.nodet.marks.GroupContent;
import gramat.engine.nodet.marks.GroupInitial;

public class NGroup {

    public final int number;
    public final Action beginAction;
    public final Action commitAction;
    public final Action rollbackAction;
    public final GroupInitial initialMark;
    public final GroupContent contentMark;
    public final GroupAccepted acceptedMark;

    public NGroup(int number, Action beginAction, Action commitAction, Action rollbackAction) {
        this.number = number;
        this.beginAction = beginAction;
        this.commitAction = commitAction;
        this.rollbackAction = rollbackAction;
        this.initialMark = new GroupInitial(number);
        this.contentMark = new GroupContent(number);
        this.acceptedMark = new GroupAccepted(number);
    }
}
