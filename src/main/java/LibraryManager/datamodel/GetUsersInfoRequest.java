package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

public class GetUsersInfoRequest {
    @SerializedName("employee_number")
    private final String employeeNumber;
    @SerializedName("name_kana")
    private final String nameKana;

    public GetUsersInfoRequest(String employeeNumber, String nameKana) {
        this.employeeNumber = employeeNumber;
        this.nameKana = nameKana;
    }

    public String employeeNumber() {
        return employeeNumber;
    }

    public String nameKana() {
        return nameKana;
    }
}
