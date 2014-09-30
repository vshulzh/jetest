package jetree.impl.fs;

import jetree.JeNode;

import java.io.File;

public class FsJeNode extends JeNode {

    private final File file;

    public FsJeNode(String id, boolean leaf, boolean container, File file) {
        super(id, leaf, container);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
