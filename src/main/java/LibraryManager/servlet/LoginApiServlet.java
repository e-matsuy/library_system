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

@WebServlet(name="LibraryManager", urlPatterns = {"/api/login"})
public class LoginApiServlet extends HttpServlet {
    static final String CONTENT_TYPE_JSON = "application/json";
    LoginController loginController = new LoginController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request/responseの設定
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");
        LoginRequest request = GsonSingleton.fromJson(req.getReader(), LoginRequest.class);
        String responseBody = loginController.login(request);
        resp.getWriter().println(responseBody);
    }
}
