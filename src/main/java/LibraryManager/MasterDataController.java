package LibraryManager;

import LibraryManager.datamodel.*;
import LibraryManager.service.BookDataService;
import LibraryManager.service.CategoryService;
import LibraryManager.service.PublisherService;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

public class MasterDataController extends AbstractController {
    private CategoryService categoryService = null;
    private PublisherService publisherService = null;
    private BookDataService bookDataService = null;

    private void initServices() {
        if (nonNull(categoryService)) {
            categoryService.close();
        }
        if (nonNull(publisherService)) {
            publisherService.close();
        }
        if (nonNull(bookDataService)) {
            bookDataService.close();
        }
    }

    public String getCategories(String token) {
        List<Category> targets = null;
        GetCategoriesResponse getCategoriesResponse;
        initServices();

        /* トークンの期限と署名を確認してユーザーを取り出す */
        String checkTokenResult = checkToken(token);
        /* トークンに異常あり */
        if (nonNull(checkTokenResult)) {
            return checkTokenResult;
        }

        categoryService = new CategoryService();
        try {
            targets = categoryService.getCategories();
            getCategoriesResponse = new GetCategoriesResponse(targets);
            return returnSuccessResponse(getCategoriesResponse);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            categoryService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }

    public String getPublishers(String token) {
        List<Publisher> targets = null;
        GetPublishersResponse getPublishersResponse;
        initServices();

        /* トークンの期限と署名を確認してユーザーを取り出す */
        String checkTokenResult = checkToken(token);
        /* トークンに異常あり */
        if (nonNull(checkTokenResult)) {
            return checkTokenResult;
        }

        publisherService = new PublisherService();
        try {
            targets = publisherService.getPuiblishers();
            getPublishersResponse = new GetPublishersResponse(targets);
            return returnSuccessResponse(getPublishersResponse);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            categoryService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }

    public String addCategory(String token, AddCategoryRequest request) {
        initServices();
        Integer returnId = null;
        AddCategoryResponse addCategoryResponse = null;
        categoryService = new CategoryService();


        try {
            returnId = categoryService.addCategory(request);
            if (nonNull(returnId)) {
                categoryService.close();
                addCategoryResponse = new AddCategoryResponse(returnId);
                return returnSuccessResponse(addCategoryResponse);
            } else {
                return returnFailedResponse(INSERT_FAILED_ERROR_MESSAGE);
            }
        } catch (PSQLException psqlException) {
            /* 外部キー制約に引っかかった時のException */
            categoryService.close();
            return returnFailedResponse(FOREIGN_KEY_ERROR_MESSAGE);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            categoryService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }

    public String addPublisher(String token, AddPublisherRequest request) {
        initServices();
        Integer returnId = null;
        AddPublisherResponse addPublisherResponse = null;
        publisherService = new PublisherService();

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
            returnId = publisherService.addPublisher(request);
            if (nonNull(returnId)) {
                categoryService.close();
                addPublisherResponse = new AddPublisherResponse(returnId);
                return returnSuccessResponse(addPublisherResponse);
            } else {
                return returnFailedResponse(INSERT_FAILED_ERROR_MESSAGE);
            }
        } catch (PSQLException psqlException) {
            /* 外部キー制約に引っかかった時のException */
            categoryService.close();
            return returnFailedResponse(FOREIGN_KEY_ERROR_MESSAGE);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            categoryService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }

    public String addBookMasterData(String token, AddBookMasterRequest request){
        initServices();
        Integer returnId = null;
        AddBookMasterResponse addBookMasterResponse = null;
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
            returnId = bookDataService.addNewMasterData(request);
            if (nonNull(returnId)) {
                bookDataService.close();
                addBookMasterResponse = new AddBookMasterResponse(returnId);
                return returnSuccessResponse(addBookMasterResponse);
            } else {
                return returnFailedResponse(INSERT_FAILED_ERROR_MESSAGE);
            }
        } catch (PSQLException psqlException) {
            /* 外部キー制約に引っかかった時のException */
            bookDataService.close();
            return returnFailedResponse(FOREIGN_KEY_ERROR_MESSAGE);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            bookDataService.close();
            return returnFailedResponse(DATABASE_CONNECTION_ERROR_MESSAGE);
        }
    }
}
