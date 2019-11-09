package org.gramat.util;

import org.gramat.PathResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class FileHelper {

    public static PathResolver createPathResolver(Path path) {
        Path root;

        if (!Files.isDirectory(path) && path.getParent() != null) {
            root = path.getParent();
        }
        else {
            root = path;
        }

        return p -> readAllString(root.resolve(p));
    }

    public static String readAllString(Path path) {
        try {
            byte[] data = Files.readAllBytes(path);

            return new String(data);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path findResourcePath(String name) {
        return findResourcePath(name, ClassLoader::getSystemResource);
    }

    public static Path findResourcePath(String name, Class<?> siblingClass) {
        return findResourcePath(name, siblingClass::getResource);
    }

    public static Path findResourcePath(String name, Function<String, URL> loader) {
        try {
            URL url = loader.apply(name);

            if (url == null) {
                throw new RuntimeException("resource not found: " + name);
            }

            URI uri = url.toURI();
            return Paths.get(uri);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
