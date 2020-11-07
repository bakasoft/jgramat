package stone.binary.output.impl;

import stone.binary.output.DescriberRepository;
import stone.binary.output.ObjectDescriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDescriberRepository implements DescriberRepository {

    private final List<ObjectDescriber> describers;

    public DefaultDescriberRepository() {
        describers = new ArrayList<>();
    }

    public void addDescriber(ObjectDescriber describer) {
        if (getDescriber(describer.getTypeIndex().getIndex()) != null) {
            throw new RuntimeException();
        }

        describers.add(describer);
    }

    private ObjectDescriber getDescriber(int typeIndex) {
        for (var describer : describers) {
            if (describer.getTypeIndex().getIndex() == typeIndex) {
                return describer;
            }
        }
        return null;
    }

    public List<ObjectDescriber> getDescribers() {
        return Collections.unmodifiableList(describers);
    }

    public ObjectDescriber findDescriberFor(Object obj) {
        for (var describer : describers) {
            if (describer.accepts(obj)) {
                return describer;
            }
        }

        throw new RuntimeException("No describer found for " + obj);
    }

}
