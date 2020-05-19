package gramat.util;

import java.util.Collection;

public class SetOps {

    public static <T> boolean intersects(Collection<T> left, Collection<T> right) {
        for (var r : right) {
            if (left.contains(r)) {
                return true;
            }
        }

        for (var l : left) {
            if (right.contains(l)) {
                return true;
            }
        }

        return false;
    }

}
