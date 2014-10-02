package jetest.web.servlet;

import jetree.JeNode;
import jetree.JeTree;
import jetree.JeTreeException;
import jetree.impl.fs.FsJeTree;
import jetree.util.SynchronizedJeTree;
import jetree.view.impl.JsonFsJeNodeConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;

public class JeTreeServlet extends HttpServlet {

    private static final String JETREE_BASE_DIR_PROP_NAME = "jetree.base.dir";
    private static final String JE_TREE = "JE_TREE";

    private JsonFsJeNodeConverter jsonFsJeNodeConverter = new JsonFsJeNodeConverter();

    @Override
    public void init() throws ServletException {
        HashMap<String, String> resourceMap = new HashMap<String, String>();
        resourceMap.put(jsonFsJeNodeConverter.getFolderResourceName(), "images/folder.gif");
        resourceMap.put(jsonFsJeNodeConverter.getUnknownResourceName(), "images/unknown.gif");
        jsonFsJeNodeConverter.setResourceMap(resourceMap);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String nodeId = req.getParameter("nodeId");
            boolean collapse = req.getParameter("collapse") != null;
            PrintWriter printWriter = new PrintWriter(resp.getOutputStream());
            JeTree jeTree = getJeTree(req);
            if (nodeId == null) {
                printWriter.write(jsonFsJeNodeConverter.convert(jeTree.getRoot()));
            } else {
                if (!collapse) {
                    printWriter.write(jsonFsJeNodeConverter.convert(jeTree.expand(nodeId)));
                } else {
                    jeTree.collapse(nodeId);
                    printWriter.write(jsonFsJeNodeConverter.convert(Collections.<JeNode>emptyList()));
                }
            }
            printWriter.flush();
            resp.setContentType("application/json");
            resp.setStatus(200);
        } catch (JeTreeException e) {
            throw new ServletException(e);
        }
    }

    private JeTree getJeTree(HttpServletRequest request) throws ServletException {
        Object attribute = request.getSession().getAttribute(JE_TREE);
        if (attribute == null) {
            try {
                JeTree jeTree = new SynchronizedJeTree(new FsJeTree(new File(System.getProperty(JETREE_BASE_DIR_PROP_NAME))));
                request.getSession().setAttribute(JE_TREE, jeTree);
                return jeTree;
            } catch (JeTreeException e) {
                throw new ServletException(e);
            }
        } else {
            return (JeTree) attribute;
        }
    }

}
