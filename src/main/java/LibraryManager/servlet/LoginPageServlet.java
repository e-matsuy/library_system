package LibraryManager.servlet;


import LibraryManager.LoginController;
import LibraryManager.datamodel.LoginRequest;
import LibraryManager.util.GsonSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="LibraryManager", urlPatterns = {"/login"})
public class LoginPageServlet extends HttpServlet {
    static final String CONTENT_TYPE_JSON = "application/json";
    LoginController loginController = new LoginController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/WEB-INF/index.html";
        resp.sendRedirect(url);
    }
}
