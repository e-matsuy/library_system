package LibraryManager;

import LibraryManager.datamodel.DataTrasferObject;
import LibraryManager.datamodel.FailedResponse;
import LibraryManager.datamodel.SuccessResponse;
import LibraryManager.datamodel.User;
import LibraryManager.util.GsonSingleton;
import LibraryManager.util.TokenCertificator;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import java.sql.SQLException;

public abstract class AbstractController {
    protected final static String DATABASE_CONNECTION_ERROR_MESSAGE = "データベース接続に問題が発生しました";
    protected final static String INVALID_TOKEN_ERROR_MESSAGE = "認証情報に問題があるようです。ログインし直してください";
    protected final static String EXPIRED_TOKEN_ERROR_MESSAGE = "認証情報の期限がきれています。ログインし直してください";
    protected final static String FOREIGN_KEY_ERROR_MESSAGE = "存在しないユーザーや書籍情報,ジャンル情報が指定されています";
    protected final static String INSERT_FAILED_ERROR_MESSAGE = "データの追加に失敗しました";
    protected final static String INVALID_USER_REQUEST_MESSAGE = "一般ユーザーに許可されていないリクエストです";
    protected final static String INVALID_REQUEST_MESSAGE = "無効なリクエストです";
    protected User requestUser = null;
    String jsonString = null;

    public String returnFailedResponse(String errorMessage) {
        FailedResponse failedResponse = new FailedResponse(errorMessage);
        jsonString = GsonSingleton.toJson(failedResponse);
        return jsonString;
    }

    public String returnSuccessResponse(DataTrasferObject data) {
        SuccessResponse successResponse = new SuccessResponse(data);
        jsonString = GsonSingleton.toJson(successResponse);
        return jsonString;
    }

    public String checkToken(String token) {
        try {
            requestUser = TokenCertificator.getInstance().decodeToken(token);
        } catch (TokenExpiredException tokenExpiredException) {
            tokenExpiredException.printStackTrace();
            return returnFailedResponse(EXPIRED_TOKEN_ERROR_MESSAGE);
        } catch (SignatureVerificationException|NullPointerException signatureVerificationException) {
            return returnFailedResponse(INVALID_TOKEN_ERROR_MESSAGE);
        } catch (SQLException sqlException) {
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
        return null;
    }
}
