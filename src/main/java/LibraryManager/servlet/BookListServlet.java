package LibraryManager.servlet;

import LibraryManager.BookDataController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="getBookList", urlPatterns = {"/api/booklist"})
public class BookListServlet extends HttpServlet implements NeedAuth, ReturnJson{
    BookDataController bookDataController = new BookDataController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");

        String token = extractToken(req);
        String responseBody = bookDataController.getBooks(token, req);
        resp.getWriter().println(responseBody);
    }
}
