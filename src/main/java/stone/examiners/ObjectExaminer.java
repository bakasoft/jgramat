package stone.examiners;

import java.util.Set;

public interface ObjectExaminer extends Examiner {
    Set<String> getKeys(Object value);

    Object getValue(Object value, String key);
}
