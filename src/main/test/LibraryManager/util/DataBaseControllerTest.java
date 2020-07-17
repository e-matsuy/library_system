package LibraryManager.util;

import LibraryManager.BookDataController;
import LibraryManager.datamodel.AddBookRequest;
import org.junit.Test;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DataBaseControllerTest {
    String url = "jdbc:postgresql://127.0.0.1:5433/library";
    String user = "admin";
    String password = "e-jan";

    @Test
    public void ちゃんと接続できることをテスト() throws Exception{
        DataBaseController dbc = new DataBaseController(url,user,password);
    }


    @Test
    public void selectのテスト() throws Exception{
        /*
        DataBaseController dbc = new DataBaseController(url,user,password);
        ResultSet rs = dbc.select("select * from Roles");
        while(rs.next()){
            System.out.print(rs.getInt("id"));
            System.out.print("/" + rs.getString("name"));
            System.out.print("/" + rs.getString("updated_at"));
            System.out.println();
        }
        rs.close();

        String query = "insert into roles (name) values (?)";
        ArrayList<String> parameters = new ArrayList<String>();
        parameters.add("hohohoho");
        int rss = dbc.insert(query, parameters);
        System.out.println(rss);
         */
    }

    @Test
    public void selectの複数テスト() throws Exception{
        /*
        DataBaseController dbc = new DataBaseController(url,user,password);
        String query = "select * from Roles where id = ?";
        ArrayList<String> parameters = new ArrayList<String>();
        parameters.add("1");
        ResultSet rs = dbc.select(query, parameters);
        while(rs.next()){
            System.out.print(rs.getInt("id"));
            System.out.print("/" + rs.getString("name"));
            System.out.print("/" + rs.getString("updated_at"));
            System.out.println();
        }
         */

    }
    @Test
    public void addBookTest() throws Exception{
        BookDataController bookDataController = new BookDataController();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNTk0NzIxODM3fQ.rYRpD2phFxwIibU9XQ6cA9rcJ3AnzlnDVKTUqaJkub8";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        AddBookRequest request = new AddBookRequest(4, sdf.parse("2020/07/01"), 2);

        String returnString = bookDataController.addBook(token,request);

        System.err.println(returnString);
    }

    @Test
    public void GsonTest(){
        String a = "{'book_id':2, 'bought_date':'2020/07/01', 'purchaser_id':1}";
        AddBookRequest req = GsonSingleton.fromJson(a, AddBookRequest.class);
        System.out.println(Integer.toString(req.bookId()) + req.boughtDate() + req.purchaserId());
    }

}