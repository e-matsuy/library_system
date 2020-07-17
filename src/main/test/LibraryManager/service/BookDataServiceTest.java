package LibraryManager.service;
import LibraryManager.datamodel.AddBookRequest;
import LibraryManager.datamodel.Book;
import LibraryManager.datamodel.BookListRequest;
import LibraryManager.datamodel.User;
import LibraryManager.util.GsonSingleton;
import com.google.gson.Gson;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class BookDataServiceTest {
    @Test
    public void tetetest() throws Exception{
        BookDataService bookDataService = new BookDataService();
        BookListRequest bookListRequest = new BookListRequest();
        bookListRequest.setIsbn("1234567890123");
        //bookListRequest.setYou(true);
        //bookListRequest.setOverdue(true);
        UserDataService userDataService = new UserDataService();
        User user = userDataService.getUser(1);
        List<Book> books = bookDataService.getBookList(bookListRequest, user);
        System.out.println(GsonSingleton.toJson(books));
        System.out.println(bookDataService.getNumberOfResultThatQuery(bookListRequest, user));
    }

    @Test
    public void addNewBookTest() throws SQLException, ParseException {
        BookDataService bookDataService = new BookDataService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        AddBookRequest request = new AddBookRequest(3, sdf.parse("2020/07/01"), 2);

        bookDataService.addNewBook(request);
    } }