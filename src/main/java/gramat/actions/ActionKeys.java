package gramat.actions;

public enum ActionKeys {

    ARRAY_BEGIN("beginArray", ArrayBegin.class),
    ARRAY_END("endArray", ArrayEnd.class),
    ATTRIBUTE_BEGIN("beginAttribute", AttributeBegin.class),
    ATTRIBUTE_END("endAttribute", AttributeEnd.class),
    NAME_BEGIN("beginName", NameBegin.class),
    NAME_END("endName", NameEnd.class),
    OBJECT_BEGIN("beginObject", ObjectBegin.class),
    OBJECT_END("endObject", ObjectEnd.class),
    RECURSION_ENTER("enterRecursion", RecursionEnter.class),
    RECURSION_EXIT("exitRecursion", RecursionExit.class),
    VALUE_BEGIN("beginValue", ValueBegin.class),
    VALUE_END("endValue", ValueEnd.class),

    ;

    public final String key;
    public final Class<? extends Action> type;

    ActionKeys(String key, Class<? extends Action> type) {
        this.key = key;
        this.type = type;
    }

}
