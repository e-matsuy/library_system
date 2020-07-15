package LibraryManager.servlet;

import LibraryManager.BookDataController;
import LibraryManager.datamodel.AddBookRequest;
import LibraryManager.datamodel.LoginRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="regierBookDataApi", urlPatterns = {"/api/addbook"})
public class BookDataServlet extends HttpServlet implements NeedAuth {
    static final String CONTENT_TYPE_JSON = "application/json";
    BookDataController bookDataController = new BookDataController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        String token = extractToken(req);
        AddBookRequest request = GsonSingleton.fromJson(req.getReader(), AddBookRequest.class);
        String responseBody = bookDataController.addBook(token, request);
        resp.getWriter().println(responseBody);
    }
}

