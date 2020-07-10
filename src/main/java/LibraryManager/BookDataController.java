package LibraryManager;

import LibraryManager.datamodel.Book;
import LibraryManager.datamodel.BookListRequest;
import LibraryManager.datamodel.BookListResponse;
import LibraryManager.datamodel.Role;
import LibraryManager.service.BookDataService;
import LibraryManager.util.GsonSingleton;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

public class BookDataController extends AbstractController{

    private static String INVALID_USER_REQUEST_MESSAGE = "一般ユーザーに許可されていないリクエストです";
    private BookDataService bookDataService = null;

    private int calcNowPageNumber(int offset, int size){
        return (offset / size ) + 1;
    }

    private String generateBookListResponseString(BookListResponse bookListResponse){
        return GsonSingleton.toJson(bookListResponse);
    }

    public String getBooks(String token, HttpServletRequest httpServletRequest){
        BookListRequest bookListRequest = null;
        bookDataService = new BookDataService();
        List<Book> books = null;
        int resultCount = 0;
        int pageCount = 0;
        BookListResponse bookListResponse = null;

        /* トークンの期限と署名を確認してユーザーを取り出す */
        String checkTokenResult = checkToken(token);
        if(nonNull(checkTokenResult)){
            return checkTokenResult;
        }

        /* リクエストをDTOにパース */
        bookListRequest = bookDataService.parseRequest(httpServletRequest);

        /* 非管理者による延滞書籍一覧のリクエストを強制的にfalseにする */
        if(requestUser.role() != Role.admin && bookListRequest.overdue()){
            return returnFailedResponse(INVALID_USER_REQUEST_MESSAGE);
        }

        /* リクエストされた情報を集める */
        try{
            books = bookDataService.getBookList(bookListRequest, requestUser);
            resultCount = bookDataService.getNumberOfResultThatQuery(bookListRequest, requestUser);
            pageCount = calcNowPageNumber(bookListRequest.offset(), bookListRequest.size());
        } catch (SQLException sqlException) {
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }

        bookListResponse = new BookListResponse(books, pageCount, resultCount);

        return generateBookListResponseString(bookListResponse);
    }
}
