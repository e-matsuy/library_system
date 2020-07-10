package LibraryManager.servlet;

import LibraryManager.BookDataController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="getBookList", urlPatterns = {"/api/booklist"})
public class BookListServlet extends HttpServlet {
    static final String CONTENT_TYPE_JSON = "application/json";
    BookDataController bookDataController = new BookDataController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");

        String token;
        token = req.getHeader("Authorization").replace("Bearer ","");
        String responseBody = bookDataController.getBooks(token, req);
        resp.getWriter().println(responseBody);
    }
}
