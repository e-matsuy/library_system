package LibraryManager.servlet;

import LibraryManager.BookDataController;
import LibraryManager.datamodel.CheckoutRequest;
import LibraryManager.datamodel.ReturnRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="returnApi", urlPatterns = {"/api/return"})
public class ReturnServlet extends HttpServlet implements NeedAuth, ReturnJson {
    BookDataController bookDataController = new BookDataController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        ReturnRequest request = GsonSingleton.fromJson(req.getReader(), ReturnRequest.class);
        String token = extractToken(req);
        String responseBody = bookDataController.returnBook(token, request);
        resp.getWriter().println(responseBody);
    }
}
