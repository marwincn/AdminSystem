package cn.marwin.adminsystem.facade;

import java.io.File;

public class FileFacade {
    private String name;
    private long size;

    public FileFacade(File file) {
        this.name = file.getName();
        this.size = file.length();
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
