package gramat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataUtils {

    public static <T, R> List<R> map(List<T> items, Function<T, R> mapper) {
        var result = new ArrayList<R>(items.size());

        for (var item : items) {
            result.add(mapper.apply(item));
        }

        return result;
    }

}
