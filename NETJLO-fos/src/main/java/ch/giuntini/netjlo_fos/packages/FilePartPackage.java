package ch.giuntini.netjlo_fos.packages;

import ch.giuntini.netjlo_core.packages.BasePackage;

import java.io.Serializable;

public class FilePartPackage extends BasePackage<String> implements Serializable {
    private final boolean EOF;

    public FilePartPackage(String information) {
        super(information);
        EOF = false;
    }

    public FilePartPackage(boolean EOF) {
        super("");
        this.EOF = EOF;
    }

    public boolean isEOF() {
        return EOF;
    }
}
