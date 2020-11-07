package gramat.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class WorkingFile {

    private final WorkingFolder root;

    private final Path path;

    public WorkingFile(WorkingFolder root, Path path) {
        this.root = root;
        this.path = path;
    }

    public String getName() {
        return path.toString();
    }

    public String getBaseName() {
        var name = getName();
        var pointIndex = name.lastIndexOf('.');
        if (pointIndex != -1) {
            return name.substring(0, pointIndex);
        }
        return name;
    }

    public String getExtension() {
        var name = getName();
        var pointIndex = name.lastIndexOf('.');
        if (pointIndex != -1) {
            return name.substring(pointIndex + 1);
        }
        return null;
    }

    public WorkingFolder getRoot() {
        return root;
    }

    public boolean hasExtension(String... exts) {
        if (exts.length == 0) {
            return getExtension() != null;
        }
        for (var ext : exts) {
            if (Objects.equals(getExtension(), ext)) {
                return true;
            }
        }
        return false;
    }

    public WorkingFile withExtension(String ext) {
        var newName = getBaseName() + "." + ext;
        var newAbs = root.getPath().resolve(newName);
        var newRel = root.getPath().relativize(newAbs);
        return new WorkingFile(root, newRel);
    }

    public Path getAbsolutePath() {
        return root.getPath().resolve(path);
    }

    public PrintStream openPrintStream() throws IOException {
        return new PrintStream(Files.newOutputStream(getAbsolutePath(), StandardOpenOption.CREATE_NEW));
    }

    public boolean exists() {
        return Files.exists(getAbsolutePath());
    }

    public Writer openWriter() throws IOException {
        return Files.newBufferedWriter(getAbsolutePath(), StandardOpenOption.CREATE_NEW);
    }

    public void delete() throws IOException {
        Files.delete(getAbsolutePath());
    }

    public Reader openReader() throws IOException {
        return Files.newBufferedReader(getAbsolutePath());
    }

    public boolean deleteIfExists() throws IOException {
        return Files.deleteIfExists(getAbsolutePath());
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
