package gramat.binary;

import java.nio.charset.StandardCharsets;

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

    int MASK_1B = 0b10000000;
    int MASK_2B = 0b11000000;
    int MASK_3B = 0b11100000;
    int MASK_4B = 0b11110000;

    static byte[] bytesOf(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    static byte[] bytesOf(int value) {
        if ((value >>> 7) == 0) {
            return new byte[] {
                    (byte)(value),
            };
        }
        else if ((value >>> 14) == 0) {
            return new byte[] {
                    (byte)(MASK_1B | (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else if ((value >>> 21) == 0) {
            return new byte[] {
                    (byte)(MASK_2B | (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else if ((value >>> 28) == 0) {
            return new byte[] {
                    (byte)(MASK_3B | (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else {
            return new byte[] {
                    (byte)(MASK_4B),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
    }

}
