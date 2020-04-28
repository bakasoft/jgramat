package gramat.util;

import gramat.GramatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileTool {
    public static String loadString(Path file) {
        byte[] data;

        try {
            data = Files.readAllBytes(file);
        } catch (IOException e) {
            throw new GramatException(e);
        }

        return new String(data, StandardCharsets.UTF_8);
    }

    public static List<Path> listVisibleFiles(Path folder) {
        try {
            return Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .filter(file -> {
                        for (var item : file) {
                            if (item.toString().startsWith(".")) {
                                return false;
                            }
                        }

                        return true;
                    })
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read folder: " + folder, e);
        }
    }

    public static String getFolderName(Path file) {
        var parent = Objects.requireNonNull(file).getParent();

        if (parent == null) {
            throw new RuntimeException("Cannot get the folder name of: " + file);
        }

        return parent.getFileName().toString();
    }
}
