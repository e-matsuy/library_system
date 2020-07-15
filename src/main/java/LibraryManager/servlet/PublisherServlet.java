package LibraryManager.servlet;

import LibraryManager.MasterDataController;
import LibraryManager.datamodel.AddCategoryRequest;
import LibraryManager.datamodel.AddPublisherRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="publisherApi", urlPatterns = {"/api/publishers"})
public class PublisherServlet extends HttpServlet implements NeedAuth {
    static final String CONTENT_TYPE_JSON = "application/json";
    MasterDataController controller = new MasterDataController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        AddPublisherRequest request = GsonSingleton.fromJson(req.getReader(), AddPublisherRequest.class);
        String responseBody = controller.addPublisher(token, request);
        resp.getWriter().println(responseBody);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        String responseBody = controller.getPublishers(token);
        resp.getWriter().println(responseBody);
    }
}

