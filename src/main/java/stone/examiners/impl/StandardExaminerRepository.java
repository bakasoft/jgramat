package stone.examiners.impl;

import stone.errors.StoneException;
import stone.examiners.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StandardExaminerRepository implements ExaminerRepository {

    private final Map<Class<?>, Examiner> examiners;

    public StandardExaminerRepository() {
        this.examiners = new LinkedHashMap<>();
    }

    public void addExaminer(Class<?> type, Examiner examiner) {
        if (examiners.containsKey(type)) {
            throw new StoneException();
        }
        examiners.put(type, examiner);
    }

    @Override
    public Examiner findExaminer(Class<?> type) {
        var examiner = examiners.get(type);

        if (examiner != null) {
            return examiner;  // Best scenario
        }
        else if (type.isArray()) {
            return GenericArrayExaminer.INSTANCE;  // Any array
        }
        else if (List.class.isAssignableFrom(type)) {
            return GenericListExaminer.ANONYMOUS_INSTANCE;  // Any List
        }
        else if (Map.class.isAssignableFrom(type)) {
            return GenericMapExaminer.ANONYMOUS_INSTANCE;  // Any List
        }

        throw new StoneException("Examiner not found: " + type);
    }
}
