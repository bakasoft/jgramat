package gramat.actions;

public enum ActionKeys {

    ARRAY_KEEP("makeArray", ArrayKeep.class),
    ARRAY_HALT("haltArray", ArrayHalt.class),
    ATTRIBUTE_KEEP("makeAttribute", AttributeKeep.class),
    ATTRIBUTE_HALT("haltAttribute", AttributeHalt.class),
    NAME_KEEP("makeName", NameKeep.class),
    NAME_HALT("haltName", NameHalt.class),
    OBJECT_KEEP("makeObject", ObjectKeep.class),
    OBJECT_HALT("haltObject", ObjectHalt.class),
    RECURSION_KEEP("makeRecursion", RecursionKeep.class),
    RECURSION_HALT("haltRecursion", RecursionHalt.class),
    VALUE_KEEP("makeValue", ValueKeep.class),
    VALUE_HALT("haltValue", ValueHalt.class),

    ;

    public final String key;
    public final Class<? extends Action> type;

    ActionKeys(String key, Class<? extends Action> type) {
        this.key = key;
        this.type = type;
    }

}
