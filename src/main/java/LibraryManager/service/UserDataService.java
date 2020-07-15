package LibraryManager.service;

import LibraryManager.datamodel.GetUsersInfoRequest;
import LibraryManager.datamodel.Role;
import LibraryManager.datamodel.User;
import LibraryManager.util.DataBaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UserDataService extends AbstractService{
    private final String GET_USER_BY_EMPLOYEE_NUMBER_QUERY = "select users.id, employee_number, users.name, name_kana, mail_address, date_of_hire, roles.name as role_name, password, last_login_date from users INNER JOIN roles ON roles.id = users.role where users.employee_number = ? and users.deleted_at is Null;";
    private final String GET_USER_BY_ID_QUERY = "select users.id, employee_number, users.name, name_kana, mail_address, date_of_hire, roles.name as role_name, password, last_login_date from users INNER JOIN roles ON roles.id = users.role where users.id = ? and users.deleted_at is Null;";
    private final String GET_USER_BY_KANA_QUERY ="select users.id, employee_number, users.name, name_kana, mail_address, date_of_hire, roles.name as role_name, password, last_login_date from users INNER JOIN roles ON roles.id = users.role where users.name_kana LIKE ? and users.deleted_at is Null;";

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

    private List<User> getUserList(ResultSet resultSet) throws SQLException{
        List<User> list = new ArrayList<User>();
        while (resultSet.next()){
            list.add(parseDbdata(resultSet));
        }
        return list;
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
            /*
             SQL 接続エラー以外のSQLExceptionなのでひとまず閉じる。
             このときこのメソッドの戻り値はnullになる
             */
            dataBaseController.close();
        }
        close();
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
            /*
             SQL 接続エラー以外のSQLExceptionなのでひとまず閉じる。
             このときこのメソッドの戻り値はnullになる
             */
            dataBaseController.close();
        }
        close();
        return target;
    }

    public List<User> searchUsers(GetUsersInfoRequest request) throws SQLException{
        close();
        ResultSet resultSet = null;
        List<User> targets = new ArrayList<User>();
        dataBaseController = new DataBaseController();

        try{
            if(nonNull(request.employeeNumber())){
                preparedStatement = dataBaseController.preparedStatement(GET_USER_BY_EMPLOYEE_NUMBER_QUERY);
                preparedStatement.setString(1, request.employeeNumber());
            }else{
                preparedStatement = dataBaseController.preparedStatement(GET_USER_BY_KANA_QUERY);
                preparedStatement.setString(1, request.nameKana() + "%");
            }

            resultSet = preparedStatement.executeQuery();
            targets = getUserList(resultSet);
            close();
        }catch(SQLException sqlException){
            close();
        }
        return targets;
    }

}
