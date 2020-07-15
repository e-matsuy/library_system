import LibraryManager.datamodel.AddPublisherRequest;
import LibraryManager.datamodel.FailedResponse;
import LibraryManager.datamodel.Mail;
import LibraryManager.util.DataBaseController;
import LibraryManager.util.GsonSingleton;
import LibraryManager.util.PasswordCertificator;
import LibraryManager.util.PostClient;
import okhttp3.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseConnectionTest {
    @Test
    public void connectTest() {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        //接続文字列
        String url = "jdbc:postgresql://127.0.0.1:5433/library";
        String user = "admin";
        String password = "e-jan";

        try {
            //PostgreSQLへ接続
            conn = DriverManager.getConnection(url, user, password);

            //自動コミットOFF
            conn.setAutoCommit(false);

            //SELECT文の実行
            stmt = conn.createStatement();
            String sql = "SELECT 1";
            rset = stmt.executeQuery(sql);

            //SELECT結果の受け取り
            while(rset.next()){
                String col = rset.getString(1);
                System.out.println(col);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Test
    public void certificationTest() throws Exception{
        DataBaseController dbc = new DataBaseController();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String password = "qwertyuiop";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateOfHire = sdf.parse("2020/04/01");
        System.out.println(PasswordCertificator.passwordEncryption(password, simpleDateFormat.format(dateOfHire)));
        //System.out.println(PasswordCertificator.passwordCertificate("0001", "qwertyuiop"));
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Test
    public void postJson() throws Exception {
        /*
        PostClient postClient = new PostClient();
        Mail mail = new Mail("わたしです", "c0116248c3@edu.teu.ac.jp", "くそが", "お前も頑張れ");
        mail.setToken("25ECB42D-CA33-4FFF-939E-1245C8096CA3");
        String a = GsonSingleton.toJson(mail);
        System.err.println(postClient.doPostToGasAPI(a));
        */
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    }

    @Test
    public void parseBookListRequest(){
        String origin = "{'name':'O\'Reilly'}";
        AddPublisherRequest req = new AddPublisherRequest("O'Reilly");
        System.out.println(GsonSingleton.toJson(req));
    }
}
