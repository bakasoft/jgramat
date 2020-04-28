package bm.parsing;

import bm.BmException;
import gramat.util.FileTool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class BmFile {

    public static final String DEFAULT_SOURCE_FILE_SUFFIX = ".bm";

    public static BmFile parse(Path file) {
        return BmJson.loadFrom(file, BmFile.class);
    }

    public static BmFile create(Path file) {
        var bmFile = new BmFile(file);

        bmFile.save(file);

        return bmFile;
    }

    private final UUID id;
    private final String name;
    private final BmVersion version;
    private String sourceFileSuffix;

    public BmFile(Path file) {
        this(UUID.randomUUID(), FileTool.getFolderName(file), new BmVersion());
    }

    public BmFile(UUID id, String name, BmVersion version) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.version = Objects.requireNonNull(version);
    }

    public void save(Path path) {
        BmJson.saveTo(path, this);
    }

    public BmVersion getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setSourceFileSuffix(String suffix) {
        this.sourceFileSuffix = suffix;
    }

    public String getSourceFileSuffix() {
        if (sourceFileSuffix == null || sourceFileSuffix.isBlank()) {
            return DEFAULT_SOURCE_FILE_SUFFIX;
        }
        return sourceFileSuffix;
    }

    public Pattern getSourceFilePattern() {
        var suffix = getSourceFileSuffix();
        var pattern = ".*" + Pattern.quote(suffix);
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }
}
