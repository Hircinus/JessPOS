package com.example.jesspos;

import java.io.File;

public class FileHandler {
    private File source;

    public FileHandler(File source) {
        this.source = source;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }
}
