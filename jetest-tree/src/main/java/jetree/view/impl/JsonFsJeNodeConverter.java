package jetree.view.impl;

import jetree.JeNode;
import jetree.view.JeNodeConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonFsJeNodeConverter implements JeNodeConverter {

    public static final String DEFAULT_FOLDER_RESOURCE_NAME = "folder";
    public static final String DEFAULT_UNKNOWN_RESOURCE_NAME = "unknown";

    private static final String JSON_TEMPLATE = "\"id\":\"%s\",\"title\":\"%s\",\"iconUrl\":\"%s\",\"isLeaf\":\"%s\"";

    private Map<String, String> resourceMap = new HashMap<String, String>();

    private String folderResourceName = DEFAULT_FOLDER_RESOURCE_NAME;

    private String unknownResourceName = DEFAULT_UNKNOWN_RESOURCE_NAME;

    public void setResourceMap(Map<String, String> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public void setFolderResourceName(String folderResourceName) {
        this.folderResourceName = folderResourceName;
    }

    public void setUnknownResourceName(String unknownResourceName) {
        this.unknownResourceName = unknownResourceName;
    }

    @Override
    public String convert(JeNode jeNode) {
        StringBuilder sb = new StringBuilder();
        convertInternal(jeNode, sb);
        return sb.toString();
    }

    @Override
    public String convert(Collection<JeNode> jeNodes) {
        final StringBuilder stringBuilder = new StringBuilder();
        convertInternal(jeNodes, stringBuilder);
        return stringBuilder.toString();
    }

    private void convertInternal(JeNode jeNode, StringBuilder sb) {
        String obj = String.format(
                "{" + JSON_TEMPLATE,
                jeNode.getId(),
                jeNode.getName(),
                resolveUrl(jeNode),
                jeNode.isLeaf() ? "1" : "0");
        sb.append(obj);
        if (!jeNode.isLeaf() && jeNode.getChildren() != null) {
            sb.append(",");
            sb.append("\"children\":");
            convertInternal(jeNode.getChildren(), sb);
        }
        sb.append("}");
    }

    private void convertInternal(Collection<JeNode> jeNodes, StringBuilder stringBuilder) {
        stringBuilder.append("[");
        Iterator<JeNode> iterator = jeNodes.iterator();
        while (iterator.hasNext()) {
            JeNode next = iterator.next();
            convertInternal(next, stringBuilder);
            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
    }

    private String resolveUrl(JeNode node) {
        if (node.isContainer()) {
            return resourceMap.get(folderResourceName);
        }
        String name = node.getName();
        String extension = getExtension(name);
        String resourceByExt = resourceMap.get(extension);
        if (resourceByExt == null) {
            return resourceMap.get(unknownResourceName);
        }
        return resourceByExt;
    }

    private String getExtension(String name) {
        int i = name.lastIndexOf('.');
        if (i == -1) {
            return "";
        } else {
            return name.substring(i);
        }
    }
}
