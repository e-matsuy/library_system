package LibraryManager.datamodel;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * DBのユーザー情報を運ぶDTO
 */
public class User implements DataTrasferObject {
    private final int id;
    private final String employeeNumber;
    private final String name;
    private final String nameKana;
    private final String mailAddress;
    private final Role role;
    private final Date dateOfHire;
    private final String password;

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
