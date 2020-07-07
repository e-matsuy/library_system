package LibraryManager;

import LibraryManager.datamodel.LoginRequest;
import LibraryManager.datamodel.LoginResponse;
import LibraryManager.datamodel.User;
import LibraryManager.service.UserDataService;
import LibraryManager.util.GsonSingleton;
import LibraryManager.util.PasswordCertificator;
import LibraryManager.util.TokenCertificator;

import java.sql.SQLException;

import static java.util.Objects.isNull;

public final class LoginController extends AbstractController{

    private static String INVALID_EMPLOYEE_NUMBER_ERROR_MESSAGE = "登録されていない社員番号です";
    private static String INVALID_PASSWORD_ERROR_MESSAGE = "パスワードが間違っています";
    private UserDataService userDataService = null;

    private String generateLoginResponseString(String newToken){
        LoginResponse loginResponse = new LoginResponse(newToken, TokenCertificator.TOKEN_EXPIRE_RANGE_BY_SECONDS);
        return returnSuccessResponse(loginResponse);
    }

    public String login(LoginRequest request){
        User requestUser = null;
        String generatedToken = "";
        userDataService = new UserDataService();

        try{
            requestUser = userDataService.getUserByEmployeeNumber(request.employeeNumber());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }

        if(isNull(requestUser)){
            return returnFailedResponse(INVALID_EMPLOYEE_NUMBER_ERROR_MESSAGE);
        }

        if(PasswordCertificator.passwordCertificate(request, requestUser)){
            generatedToken = TokenCertificator.getInstance().generateToken(requestUser);
        }else{
            return returnFailedResponse(INVALID_PASSWORD_ERROR_MESSAGE);
        }

        return generateLoginResponseString(generatedToken);
    }
}
