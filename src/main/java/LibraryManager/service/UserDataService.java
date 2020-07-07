package LibraryManager.service;

import LibraryManager.datamodel.Role;
import LibraryManager.datamodel.User;
import LibraryManager.util.DataBaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.isNull;

public class UserDataService {
    private final String GET_USER_BY_EMPLOYEE_NUMBER_QUERY = "select users.id, employee_number, users.name, name_kana, mail_address, date_of_hire, roles.name as role_name, password, last_login_date from users INNER JOIN roles ON roles.id = users.role where users.employee_number = ? and users.deleted_at is Null;";
    private final String GET_USER_BY_ID_QUERY = "select users.id, employee_number, users.name, name_kana, mail_address, date_of_hire, roles.name as role_name, password, last_login_date from users INNER JOIN roles ON roles.id = users.role where users.id = ? and users.deleted_at is Null;";
    private DataBaseController dataBaseController = null;
    private PreparedStatement preparedStatement = null;

    private User parseDbdata(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("employee_number") ,
                rs.getString("name"),
                rs.getString("name_kana"),
                rs.getString("mail_address"),
                Role.getByJpName(rs.getString("role_name")),
                rs.getDate("date_of_hire"),
                rs.getString("password")
        );
    }


    public User getUser(int id) throws SQLException, NullPointerException{
        User target = null;
        ResultSet resultSet = null;
        dataBaseController = new DataBaseController();
        try {
            preparedStatement = dataBaseController.preparedStatement(GET_USER_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                target = parseDbdata(resultSet);
            }
            if (isNull(target)) {
                throw new NullPointerException();
            }
        }catch (SQLException sqlException){
            dataBaseController.close();
        }
        return target;
    }

    public User getUserByEmployeeNumber(String employeeNumber) throws SQLException, NullPointerException{
        User target = null;
        ResultSet resultSet = null;
        dataBaseController = new DataBaseController();
        try {
            preparedStatement = dataBaseController.preparedStatement(GET_USER_BY_EMPLOYEE_NUMBER_QUERY);
            preparedStatement.setString(1, employeeNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                target = parseDbdata(resultSet);
            }
        }catch (SQLException sqlException){
            dataBaseController.close();
        }
        return target;
    }
}
