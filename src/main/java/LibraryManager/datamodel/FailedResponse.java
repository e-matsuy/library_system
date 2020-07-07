package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

public class FailedResponse {
    @SerializedName("ok")
    private final boolean OK = false;
    private final String text;

    public FailedResponse(String text) {
        this.text = text;
    }
}
