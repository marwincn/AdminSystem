package cn.marwin.adminsystem.facade;

import java.io.File;

public class FileFacade {
    private String name;
    private long size;

    public FileFacade(File file) {
        if (file.exists()) {
            this.name = file.getName();
            this.size = file.length();
        } else {
            this.name = "Nonexistent file";
            this.size = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
