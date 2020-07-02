package gramat.tools;

import gramat.GramatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTool {

    public static String loadString(Path file) {
        byte[] data;

        try {
            data = Files.readAllBytes(file);
        } catch (IOException e) {
            // TODO improve message
            throw new GramatException(e);
        }

        return new String(data, StandardCharsets.UTF_8);
    }
}
