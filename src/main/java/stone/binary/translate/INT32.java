package stone.binary.translate;

import java.io.IOException;
import java.io.InputStream;

public interface INT32 {

    int MASK_1B = 0b10000000;
    int MASK_2B = 0b11000000;
    int MASK_3B = 0b11100000;
    int MASK_4B = 0b11110000;
    int MASK_5B = 0b11111000;
    int MASK_6B = 0b11111100;
    int MASK_7B = 0b11111110;
    int MASK_8B = 0b11111111;

    static byte[] bytesOfInt(int value) {
        if ((value >>> 7) == 0) {
            return new byte[]{
                    (byte) (value),
            };
        }
        else if ((value >>> 14) == 0) {
            return new byte[]{
                    (byte) (MASK_1B | (value >>> 8)),
                    (byte) (0xFF & value),
            };
        }
        else if ((value >>> 21) == 0) {
            return new byte[]{
                    (byte) (MASK_2B | (value >>> 16)),
                    (byte) (0xFF & (value >>> 8)),
                    (byte) (0xFF & value),
            };
        }
        else if ((value >>> 28) == 0) {
            return new byte[]{
                    (byte) (MASK_3B | (value >>> 24)),
                    (byte) (0xFF & (value >>> 16)),
                    (byte) (0xFF & (value >>> 8)),
                    (byte) (0xFF & value),
            };
        }
        else {
            return new byte[]{
                    (byte) (MASK_4B),
                    (byte) (0xFF & (value >>> 24)),
                    (byte) (0xFF & (value >>> 16)),
                    (byte) (0xFF & (value >>> 8)),
                    (byte) (0xFF & value),
            };
        }
    }

    static int readRawInt(InputStream input) throws IOException {
        var b0 = input.read();

        if (b0 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 7) == 0b00000000) {
            return b0;
        }

        var b1 = input.read();

        if (b1 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 6) == 0b00000010) {
            return ((b0 ^ MASK_1B) << 8) | b1;
        }

        var b2 = input.read();
        if (b2 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 5) == 0b00000110) {
            return ((b0 ^ MASK_2B) << 16) | (b1 << 8) | b2;
        }

        var b3 = input.read();
        if (b3 == -1) {
            throw new RuntimeException();
        }

        if ((b0 >>> 4) == 0b00001110) {
            return ((b0 ^ MASK_3B) << 24) | (b1 << 16) | (b2 << 8) | b3;
        }

        var b4 = input.read();
        if (b4 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 3) == 0b00011110) {
            return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
        }
        else {
            throw new IOException();
        }
    }

}
