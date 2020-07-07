package LibraryManager.datamodel;

import LibraryManager.datamodel.DataTrasferObject;
import com.google.gson.annotations.SerializedName;

public class SuccessResponse {
    @SerializedName("ok")
    private final boolean OK = true;
    private final DataTrasferObject data;

    public SuccessResponse(DataTrasferObject data) {
        this.data = data;
    }
}
