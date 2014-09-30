package jetree.util;

import jetree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread-safe JeTree implementation using deep copy of original tree.
 */
public final class SynchronizedJeTree implements JeTree {

    private final JeTree tree;

    public SynchronizedJeTree(JeTree tree) {
        this.tree = tree;
    }

    @Override
    public synchronized JeNode getRoot() throws JeTreeException {
        return deepCopyOf(tree.getRoot());
    }

    @Override
    public synchronized List<JeNode> expand(String id) throws JeNodeNotFoundException, JeCannotExpandException {
        return deepCopyOf(tree.expand(id));
    }

    @Override
    public synchronized void collapse(String id) throws JeNodeNotFoundException {
        tree.collapse(id);
    }

    private List<JeNode> deepCopyOf(List<JeNode> source) {
        List<JeNode> copy = new ArrayList<>(source.size());
        for (JeNode node : source) {
            copy.add(deepCopyOf(node));
        }
        return copy;
    }

    private JeNode deepCopyOf(JeNode source) {
        JeNode copy = new JeNode(source.getId(), source.isLeaf(), source.isContainer());
        copy.setName(source.getName());
        List<JeNode> children = source.getChildren();
        if (children != null) {
            List<JeNode> copyChildren = new ArrayList<JeNode>(children.size());
            for (JeNode child : children) {
                JeNode childCopy = deepCopyOf(child);
                copyChildren.add(childCopy);
            }
            copy.setChildren(copyChildren);
        }
        return copy;
    }
}
