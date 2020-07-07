package LibraryManager.util;

import java.sql.*;
import java.util.List;

import static java.util.Objects.nonNull;

public class DataBaseController {

    private Connection connection = null;
    private final String url;
    private final String userName;
    private final String password;

    public DataBaseController() throws SQLException {
        this(LoadConfigurations.getJdbcUrl(),
                LoadConfigurations.getDatabaseUserName(),
                LoadConfigurations.getDatabasePassword());
    }

    public DataBaseController(String jdbcUrl, String userName, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            //ここに到達してはいけない
            e.printStackTrace();
        }
        this.url = jdbcUrl;
        this.userName = userName;
        this.password = password;
        this.connection = connect();
    }

    private Connection connect() throws SQLException {
        //PostgreSQLへ接続
        Connection conn = DriverManager.getConnection(url, userName, password);
        //自動コミットOFF
        conn.setAutoCommit(false);
        return conn;
    }

    /**
     * このオブジェクトが生成したConnectionを閉じる。Connection.closeのラッパーメソッド
     * @throws SQLException データベース・アクセス・エラーが発生した場合はSQLException
     */
    public void close() throws SQLException {
       if(nonNull(connection)) {
           connection.close();
       }
    }

    /**
     * このオブジェクトが生成したConnectionの変更をデータベースへ送信/反映する。Connection.commitのラッパーメソッド
     * @throws SQLException データベース・アクセス・エラーが発生した場合、分散トランザクションに関係している間にこのメソッドが呼び出された場合、このメソッドがクローズされた接続について呼び出された場合。
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * このオブジェクトが生成したConnectionの変更をすべて破棄する。Connection.rollbackのラッパーメソッド
     * @throws SQLException データベース・アクセス・エラーが発生した場合、分散トランザクションに関係している間にこのメソッドが呼び出された場合、このメソッドがクローズされた接続について呼び出された場合。
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Connection.preparedStatementのラッパーメソッド
     * @param query 次を含めることができるSQL文: 1つ以上の'?'INパラメータ・プレースホルダー
     * @return プリコンパイルされたSQL文を含む新しいデフォルトのPreparedStatementオブジェクト
     * @throws SQLException データベース・アクセス・エラーが発生した場合、またはこのメソッドがクローズされた接続について呼び出された場合
     */
    public PreparedStatement preparedStatement(String query) throws SQLException{
        return connection.prepareStatement(query);
    }

}
