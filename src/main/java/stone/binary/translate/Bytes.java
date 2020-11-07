package stone.binary.translate;

public interface Bytes {

    int ARRAY_VALUE = 0xA0;
    int ARRAY_ITEM = 0xA1;
    int ARRAY_END = 0xAF;

    int OBJECT_VALUE = 0xB0;
    int OBJECT_FIELD = 0xB1;
    int OBJECT_END = 0xBF;

    int NULL_VALUE = 0xC0;
    int STRING_VALUE = 0xC1;
    int INTEGER_VALUE = 0xC2;
    int TRUE_VALUE = 0xC3;
    int FALSE_VALUE = 0xC4;
    int LONG_VALUE = 0xC5;
    int REFERENCE_VALUE = 0xC6;
    int POINTER_VALUE = 0xC7;

    int[] ALLOWED_VALUES = {
            NULL_VALUE,
            STRING_VALUE,
            INTEGER_VALUE,
            TRUE_VALUE,
            FALSE_VALUE,
            LONG_VALUE,
            REFERENCE_VALUE,
            POINTER_VALUE,
            OBJECT_VALUE,
            ARRAY_VALUE,
    };


}
