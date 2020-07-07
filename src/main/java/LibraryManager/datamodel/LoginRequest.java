package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

public class LoginRequest implements DataTrasferObject{
    @SerializedName("employee_number")
    private final String employeeNumber;
    private final String password;

    public LoginRequest(String employeeNumber, String password) {
        this.employeeNumber = employeeNumber;
        this.password = password;
    }

    public String password(){
        return this.password;
    }

    public String employeeNumber(){
        return this.employeeNumber;
    }
}
