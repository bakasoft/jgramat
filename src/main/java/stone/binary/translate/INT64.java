package stone.binary.translate;

import java.io.IOException;
import java.io.InputStream;

public interface INT64 {

    int MASK_1B = 0b10000000;
    int MASK_2B = 0b11000000;
    int MASK_3B = 0b11100000;
    int MASK_4B = 0b11110000;
    int MASK_5B = 0b11111000;
    int MASK_6B = 0b11111100;
    int MASK_7B = 0b11111110;
    int MASK_8B = 0b11111111;

    static long readRawLong(InputStream input) throws IOException {
        long b0 = input.read();

        if (b0 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 7) == 0b00000000) {
            return b0;
        }

        long b1 = input.read();

        if (b1 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 6) == 0b00000010) {
            return ((b0 ^ MASK_1B) << 8) | b1;
        }

        long b2 = input.read();
        if (b2 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 5) == 0b00000110) {
            return ((b0 ^ MASK_2B) << 16) | (b1 << 8) | b2;
        }

        long b3 = input.read();
        if (b3 == -1) {
            throw new RuntimeException();
        }

        if ((b0 >>> 4) == 0b00001110) {
            return ((b0 ^ MASK_3B) << 24) | (b1 << 16) | (b2 << 8) | b3;
        }

        long b4 = input.read();
        if (b4 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 3) == 0b00011110) {
            return ((b0 ^ MASK_4B) << 32) | (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
        }

        long b5 = input.read();
        if (b5 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 2) == 0b00111110) {
            return ((b0 ^ MASK_5B) << 40) | (b1 << 32) | (b2 << 24) | (b3 << 16) | (b4 << 8) | b5;
        }

        var b6 = input.read();
        if (b6 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 1) == 0b01111110) {
            return ((b0 ^ MASK_6B) << 48) | (b1 << 40) | (b2 << 32) | (b3 << 24) | (b4 << 16) | (b5 << 8) | b6;
        }

        var b7 = input.read();
        if (b7 == -1) {
            throw new RuntimeException();
        }
        else if (b0 == 0b11111110) {
            return (b1 << 48) | (b2 << 40) | (b3 << 32) | (b4 << 24) | (b5 << 16) | (b6 << 8) | b7;
        }

        var b8 = input.read();
        if (b8 == -1) {
            throw new RuntimeException();
        }
        else if (b0 == 0b11111111) {
            return (b1 << 56) | (b2 << 48) | (b3 << 40) | (b4 << 32) | (b5 << 24) | (b6 << 16) | (b7 << 8) | b8;
        }
        else {
            throw new IOException();
        }
    }


    static byte[] bytesOfLong(long value) {
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
        else if ((value >>> 35) == 0) {
            return new byte[] {
                    (byte)(MASK_4B | (value >>> 32)),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else if ((value >>> 42) == 0) {
            return new byte[] {
                    (byte)(MASK_5B | (value >>> 40)),
                    (byte)(0xFF & (value >>> 32)),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else if ((value >>> 49) == 0) {
            return new byte[] {
                    (byte)(MASK_6B | (value >>> 48)),
                    (byte)(0xFF & (value >>> 40)),
                    (byte)(0xFF & (value >>> 32)),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else if ((value >>> 56) == 0) {
            return new byte[] {
                    (byte)(MASK_7B | (value >>> 56)),
                    (byte)(0xFF & (value >>> 48)),
                    (byte)(0xFF & (value >>> 40)),
                    (byte)(0xFF & (value >>> 32)),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
        else {
            return new byte[] {
                    (byte)(MASK_8B),
                    (byte)(0xFF & (value >>> 56)),
                    (byte)(0xFF & (value >>> 48)),
                    (byte)(0xFF & (value >>> 40)),
                    (byte)(0xFF & (value >>> 32)),
                    (byte)(0xFF & (value >>> 24)),
                    (byte)(0xFF & (value >>> 16)),
                    (byte)(0xFF & (value >>> 8)),
                    (byte)(0xFF & value),
            };
        }
    }
}
