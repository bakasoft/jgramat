package gramat.engine.nodet.marks;

import gramat.engine.nodet.NMark;

public class GroupAccepted extends NMark {

    public final int number;

    public GroupAccepted(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Accepted[" + number + "]";
    }
}
