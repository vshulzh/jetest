package jetree.impl.fs;

import java.io.File;

public interface FsJeNodeIdGenerator {
    String evaluateId(File file);
}
