package LibraryManager.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * DBのユーザー情報を運ぶDTO
 */
public class User implements DataTrasferObject {
    private final int id;
    @SerializedName("employee_number")
    private final String employeeNumber;
    private final String name;
    @SerializedName("name_kana")
    private final String nameKana;
    private transient final String mailAddress;
    private transient final Role role;
    private transient final Date dateOfHire;
    private transient final String password;

    public User(int id, String employeeNumber, String name, String nameKana, String mailAddress, Role role, Date datOfHire, String password) {
        this.id = id;
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.nameKana = nameKana;
        this.mailAddress = mailAddress;
        this.role = role;
        this.dateOfHire = datOfHire;
        this.password = password;
    }

    public int id(){
        return this.id;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String name(){
        return name;
    }

    public String nameKana(){
        return nameKana;
    }

    public String mailAddress(){
        return mailAddress;
    }

    public Role role(){
        return role;
    }

    public Date dateOfHire(){
        return dateOfHire;
    }

    public String password(){
        return this.password;
    }
}
