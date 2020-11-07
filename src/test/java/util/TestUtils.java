package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static void printHex(byte[] data) {
        var col = 0;

        for (int i = 0; i < data.length; i++) {
            var b = 0xFF & data[i];

            if (col > 0) {
                System.out.print(' ');
            }

            var hex = Integer.toHexString(b).toUpperCase();

            if (hex.length() == 1) {
                System.out.print("0");
            }

            System.out.print(hex);

            col++;

            if (col == 50) {
                col = 0;
                System.out.println();
            }
        }

        System.out.println();
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

    public static List<Path> getGrammarFiles() {
        return getResourceFiles(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".gm"));
    }

    public static Path getResourcesFolder() {
        var cwd = System.getProperty("user.dir");
        return Path.of(cwd, "src", "test", "resources");
    }

    public static Path getOutputFolder() {
        return getResourcesFolder().resolve("output");
    }

    public static List<Path> getResourceFiles(Predicate<Path> condition) {
        var resourcesFolder = getResourcesFolder();

        if (!Files.exists(resourcesFolder)) {
            throw new RuntimeException("Not found: " + resourcesFolder);
        }

        try {
            return Files.walk(resourcesFolder)
                    .sorted()
                    .filter(condition)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
