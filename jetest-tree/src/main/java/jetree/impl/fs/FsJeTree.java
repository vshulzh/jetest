package jetree.impl.fs;

import jetree.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Not thread-safe
public class FsJeTree implements JeTree {

    private final FsJeNode root;
    private final FsJeNodeIdGenerator fsJeNodeIdGenerator;

    public FsJeTree(File rootFile, FsJeNodeIdGenerator fsJeNodeIdGenerator) throws JeTreeException {
        this.fsJeNodeIdGenerator = fsJeNodeIdGenerator;
        if (!rootFile.exists()) {
            throw new JeTreeException("Cannot build tree from non-existent path.");
        }
        if (!rootFile.isDirectory()) {
            throw new IllegalStateException("Cannot build tree from non-directory file.");
        }
        root = createJeNode(rootFile);
    }

    public FsJeTree(File rootFile) throws JeTreeException {
        this(rootFile, new SimpleFsJeNodeIdGenerator());
    }

    @Override
    public JeNode getRoot() throws JeTreeException {
        return root;
    }

    @Override
    public List<JeNode> expand(String id) throws JeNodeNotFoundException, JeCannotExpandException {
        FsJeNode node = findFromRoot(id);
        if (node.isLeaf()) {
            throw new JeCannotExpandException();
        }
        // Already expanded
        if (node.getChildren() != null) {
            return node.getChildren();
        }
        File[] files = node.getFile().listFiles();
        if (files == null) {
            throw new JeCannotExpandException();
        }
        List<JeNode> children = new ArrayList<JeNode>(files.length);
        for (File file : files) {
            children.add(createJeNode(file));
        }
        node.setChildren(children);
        return children;
    }

    @Override
    public void collapse(String id) throws JeNodeNotFoundException {
        findFromRoot(id).setChildren(null);
    }

    private FsJeNode createJeNode(File file) {
        FsJeNode fsJeNode = new FsJeNode(
                fsJeNodeIdGenerator.evaluateId(file),
                !(file.isDirectory() && file.list().length > 0),
                file.isDirectory(),
                file);
        fsJeNode.setName(file.getName());
        return fsJeNode;
    }

    private FsJeNode findFromRoot(String id) throws JeNodeNotFoundException {
        FsJeNode found = find(root, id);
        if (found == null) {
            throw new JeNodeNotFoundException();
        }
        return found;
    }

    private FsJeNode find(FsJeNode current, String id) {
        if (id.equals(current.getId())) {
            return current;
        }
        if (current.isLeaf() || current.getChildren() == null) { // is leaf or not expanded yet
            return null;
        }

        for (JeNode jeNode : current.getChildren()) {
            FsJeNode found = find((FsJeNode) jeNode, id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}
