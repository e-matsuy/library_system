package LibraryManager.servlet;

import LibraryManager.BookDataController;
import LibraryManager.MasterDataController;
import LibraryManager.datamodel.AddBookRequest;
import LibraryManager.datamodel.AddCategoryRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="categoryApi", urlPatterns = {"/api/categories"})
public class CategoryServlet extends HttpServlet implements NeedAuth, ReturnJson {
    MasterDataController controller = new MasterDataController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        AddCategoryRequest request = GsonSingleton.fromJson(req.getReader(), AddCategoryRequest.class);
        String responseBody = controller.addCategory(token, request);
        resp.getWriter().println(responseBody);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        String responseBody = controller.getCategories(token);
        resp.getWriter().println(responseBody);
    }
}

