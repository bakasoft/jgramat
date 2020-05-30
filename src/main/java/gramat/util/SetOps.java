package gramat.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetOps {

    public static <T> Set<T> sub(Set<T> from, Set<T> remove1, Set<T> remove2) {
        var result = new HashSet<>(from);
        result.removeAll(remove1);
        result.removeAll(remove2);
        return result;
    }

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
