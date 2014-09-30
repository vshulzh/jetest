import jetree.JeNode;
import jetree.JeTree;
import jetree.JeTreeException;
import jetree.impl.fs.FsJeTree;
import jetree.util.SynchronizedJeTree;
import jetree.view.impl.JsonFsJeNodeConverter;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws JeTreeException {
        JeTree jeTree = new FsJeTree(new File("E:/Utils"));
        JeNode root = jeTree.getRoot();
        JsonFsJeNodeConverter converter = new JsonFsJeNodeConverter();
        System.out.println(converter.convert(root));
        List<JeNode> expand = jeTree.expand(root.getId());
        jeTree.expand(expand.iterator().next().getId());
        System.out.println(converter.convert(root));

        SynchronizedJeTree syncJeTree = new SynchronizedJeTree(jeTree);
        System.out.println(converter.convert(syncJeTree.getRoot()));
    }
}
