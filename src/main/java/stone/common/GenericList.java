package stone.common;

import java.util.ArrayList;
import java.util.List;

public class GenericList extends ArrayList<Object> {

    private final String type;

    public GenericList(String type) {
        this.type = type;
    }

    public GenericList(String type, List<?> items) {
        super(items);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
