package util;

import gramat.input.Tape;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Resources {

    public static Tape loadTape(String path) {
        var text = loadText(path);

        return new Tape(text, path);
    }

    public static String loadText(String path) {
        var data = loadBytes(path);

        return new String(data, Charset.defaultCharset());
    }

    private static byte[] loadBytes(String path) {
        var url = Thread.currentThread().getContextClassLoader().getResource(path);

        if (url == null) {
            throw new AssertionError("Resource not found: " + path);
        }

        try {
            return Files.readAllBytes(Path.of(url.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new AssertionError(e);
        }
    }

}
