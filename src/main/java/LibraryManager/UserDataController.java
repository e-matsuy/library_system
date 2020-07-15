package LibraryManager;

import LibraryManager.datamodel.GetUsersInfoRequest;
import LibraryManager.datamodel.GetUsersInfoResponse;
import LibraryManager.datamodel.Role;
import LibraryManager.datamodel.User;
import LibraryManager.service.UserDataService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UserDataController extends AbstractController{
    private final String EMPLOYEE_NUMBER_PARAMETER = "employee_number";
    private final String NAME_KANA_PARAMETER = "name_kana";
    private UserDataService userDataService = null;

    public String getUsersData(String token, HttpServletRequest httpServletRequest){
        List<User> list = null;
        GetUsersInfoResponse response = null;
        GetUsersInfoRequest request;

        /* トークンの期限と署名を確認してユーザーを取り出す */
        String checkTokenResult = checkToken(token);
        /* トークンに異常あり */
        if(nonNull(checkTokenResult)){
            return checkTokenResult;
        }
        /* 権限が無い時 */
        if(requestUser.role() != Role.admin){
            return returnFailedResponse(INVALID_USER_REQUEST_MESSAGE);
        }

        request = new GetUsersInfoRequest(
                httpServletRequest.getParameter(EMPLOYEE_NUMBER_PARAMETER),
                httpServletRequest.getParameter(NAME_KANA_PARAMETER)
        );

        /* 検索条件すべてがnullなら無効なリクエストとして処理 */
        if(isNull(request.employeeNumber()) && isNull(request.nameKana())){
            return returnFailedResponse(INVALID_REQUEST_MESSAGE);
        }

        userDataService = new UserDataService();
        try{
            list = userDataService.searchUsers(request);
            response = new GetUsersInfoResponse(list);
            return returnSuccessResponse(response);
        }catch(SQLException sqlException){
            userDataService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }
}
