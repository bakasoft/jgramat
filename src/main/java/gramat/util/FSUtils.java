package gramat.util;

import java.nio.file.Path;

public class FSUtils {

    public static String baseName(Path path) {
        return baseName(path.toString());
    }

    public static String baseName(String filename) {
        var pointIndex = filename.lastIndexOf('.');
        if (pointIndex != -1) {
            return filename.substring(0, pointIndex);
        }
        return filename;
    }

    public static NameExtension extractExtension(Path path) {
        return extractExtension(path.toString());
    }

    public static NameExtension extractExtension(String filename) {
        var pointIndex = filename.lastIndexOf('.');
        if (pointIndex != -1) {
            return new NameExtension(
                    filename.substring(0, pointIndex),
                    filename.substring(pointIndex+1));
        }
        return new NameExtension(filename, null);
    }

    public static class NameExtension {
        public final String name;
        public final String extension;
        public NameExtension(String name, String extension) {
            this.name = name;
            this.extension = extension;
        }

        public String changeTo(String ext) {
            return name + ext;
        }
    }

}
