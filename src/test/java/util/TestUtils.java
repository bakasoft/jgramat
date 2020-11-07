package util;

import gramat.util.WorkingFile;
import gramat.util.WorkingFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

public class TestUtils {

    public static byte[] parseBinaryBytes(String input) {
        var items = input.split(" +");
        var result = new byte[items.length];

        for (int i = 0; i < items.length; i++) {
            var value = Integer.parseInt(items[i], 2);

            if (value >= 0 && value <= 255) {
                result[i] = (byte)value;
            }
            else {
                throw new AssertionError(String.format("Binary number is not a valid byte: %s", items[i]));
            }
        }

        return result;
    }

    public static String formatBinaryBytes(byte[] data) {
        var result = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            var b = data[i];

            if (i > 0) {
                result.append(' ');
            }

            var bin = Integer.toBinaryString(0xFF & b);

            result.append("0".repeat(8 - bin.length()));
            result.append(bin);
        }

        return result.toString();
    }

    public static String sha1(byte[] data) {
        try {
            var md = MessageDigest.getInstance("SHA-1");

            return toHex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] hash) {
        var formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static WorkingFolder getResourcesFolder() {
        var cwd = System.getProperty("user.dir");
        return new WorkingFolder(Path.of(cwd, "src", "test", "resources"));
    }

    public static WorkingFolder getOutputFolder() {
        return getResourcesFolder().resolveFolder("output");
    }
}
