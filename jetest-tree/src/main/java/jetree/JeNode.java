package jetree;

import java.util.List;

public class JeNode {

    private final String id;
    private boolean leaf;
    private boolean container;
    private String name;
    private List<JeNode> children;

    public JeNode(String id, boolean leaf, boolean container) {
        this.id = id;
        this.leaf = leaf;
        this.container = container;
    }

    public String getId() {
        return id;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public boolean isContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<JeNode> children) {
        this.children = children;
    }

    public List<JeNode> getChildren() {
        return children;
    }
}