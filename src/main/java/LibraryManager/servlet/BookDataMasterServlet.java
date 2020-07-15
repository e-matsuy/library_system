package LibraryManager.servlet;

import LibraryManager.MasterDataController;
import LibraryManager.datamodel.AddBookMasterRequest;
import LibraryManager.datamodel.AddPublisherRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="bookmasterApi", urlPatterns = {"/api/bookdata"})
public class BookDataMasterServlet extends HttpServlet implements NeedAuth, ReturnJson {
    MasterDataController controller = new MasterDataController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        AddBookMasterRequest request = GsonSingleton.fromJson(req.getReader(), AddBookMasterRequest.class);
        String responseBody = controller.addBookMasterData(token, request);
        resp.getWriter().println(responseBody);
    }

}