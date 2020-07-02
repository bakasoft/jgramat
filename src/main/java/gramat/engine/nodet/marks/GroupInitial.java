package gramat.engine.nodet.marks;

import gramat.engine.nodet.NMark;

public class GroupInitial extends NMark {

    public final int number;

    public GroupInitial(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Initial[" + number + "]";
    }
}
