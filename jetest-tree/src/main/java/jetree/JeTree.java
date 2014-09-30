package jetree;

import java.util.List;

public interface JeTree {
    JeNode getRoot() throws JeTreeException;
    List<JeNode> expand(String id) throws JeNodeNotFoundException, JeCannotExpandException;
    void collapse(String id) throws JeNodeNotFoundException;
}
