package LibraryManager.servlet;

import LibraryManager.UserDataController;
import LibraryManager.datamodel.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="userdataApi", urlPatterns = {"/api/usersinfo"})
public class UserInfoServlet extends HttpServlet implements NeedAuth, ReturnJson{
    UserDataController controller = new UserDataController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE_JSON + "; charset=UTF-8");

        String token = extractToken(req);
        String responseBody = controller.getUsersData(token, req);
        resp.getWriter().println(responseBody);
    }
}
