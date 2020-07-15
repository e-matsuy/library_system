package LibraryManager.servlet;

import javax.servlet.http.HttpServletRequest;

public interface NeedAuth {
     default String extractToken(HttpServletRequest request){
         String token;
         token = request.getHeader("Authorization").replace("Bearer ","");
         return token;
    }
}
