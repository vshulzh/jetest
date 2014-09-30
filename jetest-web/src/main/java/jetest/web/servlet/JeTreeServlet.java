package jetest.web.servlet;

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

public class JeTreeServlet extends HttpServlet {

    private static final String JETREE_BASE_DIR_PROP_NAME = "jetree.base.dir";
    private SynchronizedJeTree jeTree;
    private JsonFsJeNodeConverter jsonFsJeNodeConverter = new JsonFsJeNodeConverter();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setStatus(200);
        try {
            jeTree.expand(jeTree.getRoot().getId());
            PrintWriter printWriter = new PrintWriter(resp.getOutputStream());
            printWriter.write(jsonFsJeNodeConverter.convert(jeTree.getRoot()));
            printWriter.flush();
        } catch (JeTreeException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            jeTree = new SynchronizedJeTree(new FsJeTree(new File(System.getProperty(JETREE_BASE_DIR_PROP_NAME))));
        } catch (JeTreeException e) {
            throw new ServletException(e);
        }
    }

}
