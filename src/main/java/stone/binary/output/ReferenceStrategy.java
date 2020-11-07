package stone.binary.output;

import java.util.OptionalLong;

public interface ReferenceStrategy {

    boolean accepts(Object value);

    OptionalLong lookUp(Object value);

    long save(Object value);

}
