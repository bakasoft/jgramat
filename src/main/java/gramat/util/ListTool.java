package gramat.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ListTool {

    public static <T, U extends T> void collapse(Collection<T> items, Class<U> type, Consumer<T> adder, Consumer<List<U>> joiner) {
        var joins = new ArrayList<U>();

        Runnable flush = () -> {
            if (joins.size() == 1) {
                adder.accept(joins.get(0));
                joins.clear();
            }
            else if (joins.size() >= 2) {
                joiner.accept(joins);
                joins.clear();
            }
        };

        for (T item : items) {
            if (type.isInstance(item)) {
                joins.add(type.cast(item));
            }
            else {
                flush.run();
                adder.accept(item);
            }
        }

        flush.run();
    }

}
