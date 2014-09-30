package jetree.view;

import jetree.JeNode;

import java.util.Collection;

public interface JeNodeConverter {

    String convert(JeNode jeNode);

    String convert(Collection<JeNode> jeNodes);

}
