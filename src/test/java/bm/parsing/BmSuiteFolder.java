package bm.parsing;

import bm.parsing.data.SourceFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BmSuiteFolder {

    private final Path folder;

    private final List<BmSuiteEntry> entries;

    private BmFile bmFile;

    public BmSuiteFolder(Path folder) {
        this.folder = folder.toAbsolutePath().normalize();
        this.entries = new ArrayList<>();
    }

    public List<BmSuiteEntry> getEntries() {
        return entries;
    }

    public void add(Path file, SourceFile source) {
        var fileRef = computeFileRef(file);
        var module = computeModule(file);
        entries.add(new BmSourceEntry(fileRef, module, source));
    }

    public String computeFileRef(Path file) {
        return folder.relativize(file).toString();
    }

    public String[] computeModule(Path file) {
        var names = folder.relativize(file.getParent());
        var module = new String[names.getNameCount()];

        for (int i = 0; i < names.getNameCount(); i++) {
            module[i] = names.getName(i).toString();
        }

        return module;
    }


    public Path getFolder() {
        return folder;
    }

    public void setBmFile(BmFile bmFile) {
        this.bmFile = bmFile;
    }

    public BmFile getBmFile() {
        return Objects.requireNonNull(bmFile);
    }
}
