package gramat.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WorkingFolder {

    private final Path path;

    public WorkingFolder(Path path) {
        this.path = path;
    }

    public List<WorkingFile> searchFiles(Predicate<WorkingFile> condition) {
        if (!Files.exists(path)) {
            throw new RuntimeException("Folder not found: " + path);
        }

        try(var stream = Files.walk(path)) {
            return stream
                    .sorted()
                    .filter(Files::isRegularFile)
                    .map(file -> new WorkingFile(this, path.relativize(file)))
                    .filter(condition)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WorkingFolder resolveFolder(String relativePath) {
        var resolvedPath = path.resolve(relativePath);

        if (!Files.isDirectory(resolvedPath)) {
            throw new RuntimeException();
        }

        return new WorkingFolder(resolvedPath);
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
