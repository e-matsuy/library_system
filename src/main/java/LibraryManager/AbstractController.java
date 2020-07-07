package LibraryManager;

import LibraryManager.datamodel.DataTrasferObject;
import LibraryManager.datamodel.FailedResponse;
import LibraryManager.datamodel.SuccessResponse;
import LibraryManager.util.GsonSingleton;

public abstract class AbstractController {
    protected static String DATABASE_CONNECTION_ERROR_MESSAGE = "データベース接続に問題が発生しました";
    String jsonString = null;

    public String returnFailedResponse(String errorMessage){
        FailedResponse failedResponse = new FailedResponse(errorMessage);
        jsonString = GsonSingleton.toJson(failedResponse);
        return jsonString;
    }

    public String returnSuccessResponse(DataTrasferObject data){
        SuccessResponse successResponse = new SuccessResponse(data);
        jsonString = GsonSingleton.toJson(successResponse);
        return jsonString;
    }
}
