package gramat.actions;

public enum ActionKeys {

    ARRAY_KEEP("keepArray", ArrayKeep.class),
    ARRAY_HALT("haltArray", ArrayHalt.class),
    ATTRIBUTE_KEEP("keepAttribute", AttributeKeep.class),
    ATTRIBUTE_HALT("haltAttribute", AttributeHalt.class),
    NAME_KEEP("keepName", NameKeep.class),
    NAME_HALT("haltName", NameHalt.class),
    OBJECT_KEEP("keepObject", ObjectKeep.class),
    OBJECT_HALT("haltObject", ObjectHalt.class),
    RECURSION_ENTER("enterRecursion", RecursionEnter.class),
    RECURSION_EXIT("exitRecursion", RecursionExit.class),
    VALUE_KEEP("keepValue", ValueKeep.class),
    VALUE_HALT("haltValue", ValueHalt.class),

    ;

    public final String key;
    public final Class<? extends Action> type;

    ActionKeys(String key, Class<? extends Action> type) {
        this.key = key;
        this.type = type;
    }

}
