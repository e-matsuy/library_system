package LibraryManager.util;

import java.sql.*;
import java.util.List;

import static java.util.Objects.nonNull;

public class DataBaseController {

    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    private final String url;
    private final String userName;
    private final String password;

    public DataBaseController(String jdbcUrl, String userName, String password){
        this.url = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        //PostgreSQLへ接続
        Connection conn = DriverManager.getConnection(url, userName, password);

        //自動コミットOFF
        conn.setAutoCommit(false);

        return conn;
    }

    public void close() throws SQLException {
       if(nonNull(connection)) {
           connection.close();
       }
       if(nonNull(statement)) {
            statement.close();
       }
    }

    public ResultSet select(String query) throws SQLException{
        connection = connect();
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        return resultSet;
    }

    public ResultSet select(String query, List<String> parameters) throws SQLException{
        connection = connect();
        statement = connection.prepareStatement(query);
        statement = setValuesToStatement(statement, parameters);
        resultSet = statement.executeQuery();
        return resultSet;
    }

    public int insert(String table, List columnLabels, List parameters) throws SQLException{
        return 0;
    }

    public int update(String table, List columnLabels, List parameters) throws SQLException{
        return 0;
    }

    private PreparedStatement setValuesToStatement(PreparedStatement statement, List<String> parameters) throws SQLException {
        for(int countOfParameter = 1; countOfParameter <= parameters.size(); countOfParameter++){
            statement.setString(countOfParameter, parameters.get(countOfParameter - 1));
        }
        return statement;
    }
}
