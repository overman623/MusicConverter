package com.happyhouse.music.converter.soundtouch;

import java.io.File;

public class BgmStorageListItem {

    private File file;
    boolean isFile = false;

    public BgmStorageListItem(File file, boolean isFile) {
        this.file = file;
        this.isFile = isFile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
