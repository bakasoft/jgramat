package stone;

import stone.examiners.Examiner;
import stone.examiners.ExaminerRepository;
import stone.examiners.impl.GenericListExaminer;
import stone.examiners.impl.GenericMapExaminer;
import stone.examiners.impl.GenericObjectExaminer;
import stone.examiners.impl.StandardExaminerRepository;
import stone.producers.ProducerRepository;
import stone.producers.StandardProducerRepository;
import stone.text.StoneTextDecoder;
import stone.text.StoneTextEncoder;

import java.util.*;

public class StoneSchema {

    private final Map<String, Class<?>> types;

    public StoneSchema() {
        this.types = new LinkedHashMap<>();
    }

    public void addType(Class<?> type) {
        addType(type.getSimpleName(), type);
    }

    public void addType(String name, Class<?> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        if (types.containsKey(name)) {
            throw new RuntimeException();
        }

        types.put(name, type);
    }

    public ProducerRepository createMakerRepository() {
        var makers = new StandardProducerRepository();

        for (var entry : types.entrySet()) {
            var name = entry.getKey();
            var type = entry.getValue();

            makers.addObjectMakerFor(type, name);
        }

        return makers;
    }

    public ExaminerRepository createExaminerRepository() {
        var repo = new StandardExaminerRepository();

        for (var entry : types.entrySet()) {
            var name = entry.getKey();
            var type = entry.getValue();
            var examiner = createExaminer(type, name);

            repo.addExaminer(type, examiner);
        }

        return repo;
    }

    private Examiner createExaminer(Class<?> type, String name) {
        if (List.class.isAssignableFrom(type)) {
            return new GenericListExaminer(name);
        }
        else if (Map.class.isAssignableFrom(type)) {
            return new GenericMapExaminer(name);
        }
        else {
            return new GenericObjectExaminer(type, name);
        }
    }

    public StoneTextEncoder createTextEncoder() {
        return new StoneTextEncoder(createExaminerRepository());
    }

    public StoneTextDecoder createTextDecoder() {
        return new StoneTextDecoder(createMakerRepository());
    }

}
