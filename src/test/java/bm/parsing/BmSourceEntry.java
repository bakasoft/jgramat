package bm.parsing;

import bm.parsing.data.SourceFile;

public class BmSourceEntry extends BmSuiteEntry {

    private final String fileRef;
    private final String[] module;
    private final SourceFile sourceFile;

    public BmSourceEntry(String fileRef, String[] module, SourceFile sourceFile) {
        this.fileRef = fileRef;
        this.module = module;
        this.sourceFile = sourceFile;
    }

}
