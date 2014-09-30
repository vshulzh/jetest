package jetree.impl.fs;

import java.io.File;

// Not  thread-safe
public class SimpleFsJeNodeIdGenerator implements FsJeNodeIdGenerator {

    private long counter;

    @Override
    public String evaluateId(File file) {
        return String.valueOf(counter++);
    }

}