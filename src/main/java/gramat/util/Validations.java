package gramat.util;

import java.util.Objects;

public class Validations {

    public static <T> boolean isAny(T target, T op1, T op2) {
        return target == op1 || target == op2;
    }

    public static <T> T requireEquals(T op1, T op2) {
        if (op1 != op2) {
            throw new RuntimeException();
        }

        return op1;
    }

}
