package gramat.binary;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private final Map<IndexedType, DefaultObjectEditor<?>> editors;

    public Repository() {
        this.editors = new HashMap<>();
    }

    public BinaryWriter createWriter(OutputStream output) {
        var writer = new BinaryWriter(output);

        for (var editor : editors.values()) {
            writer.addEditor(editor.getTypeClass(), editor);
        }

        return writer;
    }

    public <T> DefaultObjectEditor<T> createEditor(int index, Class<T> type) {
        if (getIndexedType(index) != null) {
            throw new RuntimeException();
        }
        var indexedType = new DefaultIndexedType(index);
        var editor = new DefaultObjectEditor<>(indexedType, type);

        editors.put(indexedType, editor);

        return editor;
    }

    private IndexedType getIndexedType(int index) {
        for (var item : editors.keySet()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        return null;
    }

}
