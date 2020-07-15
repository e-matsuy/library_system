package LibraryManager;

import LibraryManager.datamodel.*;
import LibraryManager.service.BookDataService;
import LibraryManager.util.GsonSingleton;
import org.postgresql.util.PSQLException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

public class BookDataController extends AbstractController{

    private BookDataService bookDataService = null;

    private int calcNowPageNumber(int offset, int size){
        return (offset / size ) + 1;
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

        return returnSuccessResponse(bookListResponse);
    }

    public String addBook(String token, AddBookRequest request){
        boolean result = false;
        bookDataService = new BookDataService();

        /* トークンの期限と署名を確認してユーザーを取り出す */
        String checkTokenResult = checkToken(token);
        /* トークンに異常あり */
        if(nonNull(checkTokenResult)){
            return checkTokenResult;
        }
        /* 権限が無い時 */
        if(requestUser.role() != Role.admin){
            return returnFailedResponse(INVALID_USER_REQUEST_MESSAGE);
        }

        try {
            if (bookDataService.addNewBook(request)) {
                bookDataService.close();
                return returnSuccessResponse(null);
            }else{
                return returnFailedResponse(INSERT_FAILED_ERROR_MESSAGE);
            }
        }catch(PSQLException psqlException){
            /* 外部キー制約に引っかかった時のException */
            bookDataService.close();
            return returnFailedResponse(FOREIGN_KEY_ERROR_MESSAGE);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            bookDataService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }

    }
}
