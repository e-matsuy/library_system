package LibraryManager.service;

import LibraryManager.datamodel.*;
import LibraryManager.util.DataBaseController;
import LibraryManager.util.GsonSingleton;
import org.postgresql.util.PSQLException;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class BookDataService extends AbstractService {
    private static final int MAX_LENDING_BOOKS_NUMBER = 3;
    private static final String GET_BOOK_DATA_QUERY_BASE = "select books.id, books.book as book_master_id, book_master.title, book_master.author as author, book_master.publisher as publisher_id, publishers.name as publisher_name, book_master.category as category_id, categories.name as category_name, lending_records.return_schedule as return_schedule, book_master.isbn, books.last_use_log as last_use_log, lending_records.user_id as last_user_id, users.name as last_user_name, lending_records.updated_at as last_use_date, lending_records.returned_date as returned_date from books LEFT OUTER JOIN book_master ON books.book = book_master.id LEFT OUTER JOIN publishers ON book_master.publisher = publishers.id LEFT OUTER JOIN categories ON book_master.category = categories.id LEFT OUTER JOIN lending_records ON books.last_use_log = lending_records.id LEFT OUTER JOIN users ON lending_records.user_id = users.id ";
    private static final String GET_BOOK_DATA_COUNT_QUERY_BASE = "select count(*) from books LEFT OUTER JOIN book_master ON books.book = book_master.id LEFT OUTER JOIN publishers ON book_master.publisher = publishers.id LEFT OUTER JOIN categories ON book_master.category = categories.id LEFT OUTER JOIN lending_records ON books.last_use_log = lending_records.id LEFT OUTER JOIN users ON lending_records.user_id = users.id ";
    private static final String LIMIT_QUERY = "LIMIT ? OFFSET ? ";
    private static final String STOCK_CHECK_QUERY = "WHERE books.id = ? AND (lending_records.checkout_date IS NULL OR lending_records.returned_date IS NOT NULL);";
    private static final String LENDING_BOOKS_CHECK_QUERY = "WHERE lending_records.user_id = ? AND lending_records.returned_date IS NULL ";
    private static final String INSERT_BOOK_QUERY = "insert into books (book, bought_date, purchaser) values (?, ?, ?);";
    private static final String INSERT_BOOK_MASTER_QUERY = "insert into book_master (title, author, publisher, category, isbn) values (?, ?, ?, ?, ?) RETURNING id;";
    private static final String LENDING_BOOKS_WHERE_QUERY = "WHERE lending_records.user_id = ? AND lending_records.returned_date IS NULL ";
    private static final String OVERDUE_WHERE_QUERY = "WHERE lending_records.return_schedule < CURRENT_DATE AND lending_records.returned_date IS NULL ";
    private static final String INSERT_NEW_LEND_RECORD_QUERY = "insert into lending_records (book, user_id, checkout_date, return_schedule) values (?, ?, now(), ?) RETURNING id;";
    private static final String UPDATE_LASTUSE_ID_QUERY = "UPDATE books set last_use_log = ? where id = ?;";
    private static final String LEND_CHECK_QUERY = "WHERE books.id = ? AND lending_records.user_id = ? AND lending_records.returned_date IS NULL;";
    private static final String UPDATE_RETURN_DATE_QUERY = "UPDATE lending_records set returned_date = now() where id = ?;";
    private List<Book> tergets = null;
    private ResultSet resultSet = null;
    private String queryForPreparedStatement = "";

    private void initParameters() throws SQLException {
        this.tergets = null;
        this.resultSet = null;
        this.queryForPreparedStatement = "";
        if (nonNull(dataBaseController)) {
            this.dataBaseController.close();
        }
        this.dataBaseController = new DataBaseController(); // -> ここで事故ったらSQLExceptionが飛ぶ
    }

    /**
     * GETリクエストの内容からDTOへパースする
     *
     * @param request APIServletが受け取ったリクエスト
     * @return パラメータから作成したBookListRequestオブジェクト
     */
    public BookListRequest parseRequest(HttpServletRequest request) {
        BookListRequest bookListRequest = new BookListRequest();

        if (nonNull(request.getParameter(BookListRequest.SIZE_PARAMETER_NAME))) {
            bookListRequest.setSize(Integer.parseInt(request.getParameter(BookListRequest.SIZE_PARAMETER_NAME)));
        }

        if (nonNull(request.getParameter(BookListRequest.OFFSET_PARAMETER_NAME))) {
            bookListRequest.setOffset(Integer.parseInt(request.getParameter(BookListRequest.OFFSET_PARAMETER_NAME)));
        }

        if (nonNull(request.getParameter(BookListRequest.YOU_PARAMETER_NAME))) {
            bookListRequest.setYou(Boolean.parseBoolean(request.getParameter(BookListRequest.YOU_PARAMETER_NAME)));
        }

        if (nonNull(request.getParameter(BookListRequest.OVERDUE_PARAMETER_NAME))) {
            bookListRequest.setOverdue(Boolean.parseBoolean(request.getParameter(BookListRequest.OVERDUE_PARAMETER_NAME)));
        }

        bookListRequest.setId(Integer.getInteger(request.getParameter(BookListRequest.ID_PARAMETER_NAME)));
        bookListRequest.setAuthor(request.getParameter(BookListRequest.AUTHOR_PARAMETER_NAME));
        bookListRequest.setIsbn(request.getParameter(BookListRequest.ISBN_PARAMETER_NAME));

        System.err.println(GsonSingleton.toJson(bookListRequest));

        return bookListRequest;
    }

    /**
     * 検索条件からsqlのwhere句を作る
     *
     * @param request 検索条件を含んだリクエスト情報のDTO
     * @return 検索条件を反映したWhere句
     */
    public String whereQueryBuilder(BookListRequest request) {
        StringBuilder builder = new StringBuilder();
        /* 検索条件が1つでも存在すればwhere句を用意する */
        if (nonNull(request.id()) || nonNull(request.name()) || nonNull(request.author()) || nonNull(request.isbn())) {
            builder.append("WHERE ");

            if (nonNull(request.id())) {
                builder.append("id = ? ");
                return builder.toString();
            }

            if (nonNull(request.name())) {
                builder.append("title LIKE ? ");
            }

            if (nonNull(request.author())) {
                if (nonNull(request.name())) {
                    builder.append("AND ");
                }
                builder.append("author LIKE ? ");
            }

            if (nonNull(request.isbn())) {
                if (nonNull(request.name()) || nonNull(request.author())) {
                    builder.append("AND ");
                }
                builder.append("isbn LIKE ? ");
            }

            return builder.toString();

        } else {
            return "";
        }
    }

    private void setSearchRulesToStatement(PreparedStatement statement, BookListRequest request, boolean hasLimit) throws SQLException {
        int parameterIndex = 1;
        /* クエリパラメータの設定 */
        if (nonNull(request.id())) {
            statement.setInt(parameterIndex, request.id());
            parameterIndex++;
        }
        if (nonNull(request.name())) {
            statement.setString(parameterIndex, request.name() + "%");
            parameterIndex++;
        }
        if (nonNull(request.author())) {
            statement.setString(parameterIndex, request.author() + "%");
            parameterIndex++;
        }
        if (nonNull(request.isbn())) {
            statement.setString(parameterIndex, request.isbn());
            parameterIndex++;
        }
        if (hasLimit) {
            statement.setInt(parameterIndex, request.size());
            parameterIndex++;
            statement.setInt(parameterIndex, request.offset());
        }

    }

    /**
     * sqlの結果1行分をBookオブジェクトにパースする
     *
     * @param resultSet   dbから返されたデータ
     * @param requestUser リクエストしたユーザー
     * @return 引数のResultsetが今指している行のデータをパースしたBookオブジェクト
     */
    private Book parseDbdata(ResultSet resultSet, User requestUser) {
        try {
            Integer lastUserId = resultSet.getInt("last_user_id");
            if (isNull(lastUserId)) {
                lastUserId = 0;
            }
            Book newBook = new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getInt("publisher_id"),
                    resultSet.getString("publisher_name"),
                    resultSet.getInt("category_id"),
                    resultSet.getString("category_name"),
                    resultSet.getString("isbn"),
                    resultSet.getDate("return_schedule"),
                    requestUser.id() == lastUserId
            );

            if (requestUser.role() == Role.admin) {
                newBook.setLastUserId(lastUserId == 0 ? null : lastUserId);
                newBook.setLastUserName(resultSet.getString("last_user_name"));
                newBook.setLastModDate(resultSet.getDate("last_use_date"));
                if (resultSet.getInt("last_use_log") == 0) {
                    newBook.setLastAction(null);
                } else if (isNull(resultSet.getDate("returned_date"))) {
                    newBook.setLastAction(Action.checkout);
                } else {
                    newBook.setLastAction(Action.returned);
                }
            }
            return newBook;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    /**
     * dbの応答から書籍情報の一覧を取り出す
     *
     * @param resultSet   dbから返されたデータ
     * @param requestUser リクエストしたユーザー
     * @return DTOにパースした書籍情報
     * @throws SQLException データベース接続に問題が起こった時
     */
    private List<Book> makeBookList(ResultSet resultSet, User requestUser) throws SQLException {
        List<Book> list = new ArrayList<Book>();
        while (resultSet.next()) {
            list.add(parseDbdata(resultSet, requestUser));
        }
        return list;
    }

    private String makeQueryForSearchBooks(BookListRequest request, boolean forCount) {
        /* クエリのためのパラメータを用意 */
        StringBuilder queryBuilder = new StringBuilder();
        if (forCount) {
            queryBuilder.append(GET_BOOK_DATA_COUNT_QUERY_BASE)
                    .append(whereQueryBuilder(request));
        } else {
            queryBuilder.append(GET_BOOK_DATA_QUERY_BASE)
                    .append(whereQueryBuilder(request))
                    .append(LIMIT_QUERY);
        }
        return queryBuilder.toString();
    }

    private String makeQueryForLendingList(boolean forCount) {
        StringBuilder queryBuilder = new StringBuilder();
        if (forCount) {
            queryBuilder.append(GET_BOOK_DATA_COUNT_QUERY_BASE);
        } else {
            queryBuilder.append(GET_BOOK_DATA_QUERY_BASE);
        }
        queryBuilder.append(LENDING_BOOKS_WHERE_QUERY);
        return queryBuilder.toString();
    }

    private String makeQueryForOverdueList(boolean forCount) {
        /* クエリのためのパラメータを用意 */
        StringBuilder queryBuilder = new StringBuilder();
        if (forCount) {
            queryBuilder.append(GET_BOOK_DATA_COUNT_QUERY_BASE)
                    .append(OVERDUE_WHERE_QUERY);
        } else {
            queryBuilder.append(GET_BOOK_DATA_QUERY_BASE)
                    .append(OVERDUE_WHERE_QUERY)
                    .append(LIMIT_QUERY);
        }
        return queryBuilder.toString();
    }

    private PreparedStatement createStatement(DataBaseController controller, BookListRequest request, User requestUser) throws SQLException {
        PreparedStatement preparedStatement = null;
        String queryForPreparedStatement;
        /* 特殊なリストの要求かを確認 */
        if (request.you()) {
            /* クエリのためのパラメータを用意 */
            queryForPreparedStatement = makeQueryForLendingList(false);
            preparedStatement = controller.preparedStatement(queryForPreparedStatement);
            // ユーザーの貸出中リストを取得するときはパラメータは1つだけ
            preparedStatement.setInt(1, requestUser.id());
        } else if (request.overdue()) {
            /* クエリのためのパラメータを用意 */
            queryForPreparedStatement = makeQueryForOverdueList(false);
            preparedStatement = controller.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, request.size());
            preparedStatement.setInt(2, request.offset());

        } else {
            /* クエリのためのパラメータを用意 */
            queryForPreparedStatement = makeQueryForSearchBooks(request, false);
            preparedStatement = controller.preparedStatement(queryForPreparedStatement);
            setSearchRulesToStatement(preparedStatement, request, true);
        }
        return preparedStatement;
    }

    /**
     * 指定された内容に沿う書籍情報のリストを返す
     *
     * @param request     リスクエストの内容
     * @param requestUser リクエストを送ってきたユーザー情報
     * @return リクエストに沿った書籍情報のリスト
     * @throws SQLException データベース接続に問題が起こった時
     */
    public List<Book> getBookList(BookListRequest request, User requestUser) throws SQLException {
        initParameters();

        try {
            preparedStatement = createStatement(dataBaseController, request, requestUser);
            resultSet = preparedStatement.executeQuery();
            tergets = makeBookList(resultSet, requestUser);
        } catch (SQLException sqlException) {
            /*
             SQL 接続エラー以外のSQLExceptionなのでひとまず閉じる。
             このときこのメソッドの戻り値はnullになる
             */
            sqlException.printStackTrace();
            dataBaseController.close();
        }
        return tergets;
    }

    public int getNumberOfResultThatQuery(BookListRequest request, User requestUser) throws SQLException {
        initParameters();

        /* クエリのためのパラメータを用意 */
        if (request.you()) {
            queryForPreparedStatement = makeQueryForLendingList(true);
        } else if (request.overdue()) {
            queryForPreparedStatement = makeQueryForOverdueList(true);
        } else {
            queryForPreparedStatement = makeQueryForSearchBooks(request, true);
        }

        try {
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            /* クエリのためのパラメータを用意 */
            if (request.you()) {
                // ユーザーの貸出中リストを取得するときはパラメータは1つだけ
                preparedStatement.setInt(1, requestUser.id());
            } else if (request.overdue()) {
                /* do nothing */
            } else {
                setSearchRulesToStatement(preparedStatement, request, false);
            }

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException sqlException) {
            /*
             SQL 接続エラー以外のSQLExceptionなのでひとまず閉じる。
             このときこのメソッドの戻り値はnullになる
             */
            sqlException.printStackTrace();
            dataBaseController.close();
        }
        return 0;
    }

    public boolean addNewBook(AddBookRequest request) throws SQLException {
        //int result = false;
        int result = 0;
        Date sqlDate = new Date(request.boughtDate().getTime());

        initParameters();

        try {
            preparedStatement = dataBaseController.preparedStatement(INSERT_BOOK_QUERY);
            /* 値のセット */
            preparedStatement.setInt(1, request.bookId());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setInt(3, request.purchaserId());
            result = preparedStatement.executeUpdate();
            if (result == 1) {
                dataBaseController.commit();
                return true;
            } else {
                dataBaseController.rollback();
                return false;
            }
        } catch (PSQLException psqlException) {
            if (nonNull(dataBaseController)) {
                dataBaseController.rollback();
                dataBaseController.close();
            }
            throw psqlException;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            if (nonNull(dataBaseController)) {
                dataBaseController.rollback();
                dataBaseController.close();
            }
        }
        return false;
    }

    public Integer addNewMasterData(AddBookMasterRequest request) throws SQLException {
        Integer result = null;
        initParameters();

        try {
            preparedStatement = dataBaseController.preparedStatement(INSERT_BOOK_MASTER_QUERY);
            /* 値のセット */
            preparedStatement.setString(1, request.name());
            preparedStatement.setString(2, request.author());
            preparedStatement.setInt(3, request.publisherId());
            preparedStatement.setInt(4, request.categoryId());
            preparedStatement.setString(5, request.isbn());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("id");
                dataBaseController.commit();
                close();
            } else {
                dataBaseController.rollback();
                close();
            }
        } catch (PSQLException psqlException) {
            if (nonNull(dataBaseController)) {
                dataBaseController.rollback();
                dataBaseController.close();
            }
            throw psqlException;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            if (nonNull(dataBaseController)) {
                dataBaseController.rollback();
                dataBaseController.close();
            }
        }
        return result;
    }

    public boolean checkout(CheckoutRequest request, User user) throws SQLException {
        int newRecordId = 0;
        int result = 0;
        initParameters();

        try {
            /* 個人の貸出冊数を確認 */
            queryForPreparedStatement = GET_BOOK_DATA_COUNT_QUERY_BASE + LENDING_BOOKS_CHECK_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, user.id());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt("count") >= MAX_LENDING_BOOKS_NUMBER) {
                /* 3冊が上限である */
                close();
                return false;
            }
            resultSet.close();

            /* 対象が貸出中であるかを確認 */
            queryForPreparedStatement = GET_BOOK_DATA_COUNT_QUERY_BASE + STOCK_CHECK_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, request.id());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt("count") != 1) {
                /* 1でなければ貸出中 */
                close();
                return false;
            }
            resultSet.close();

            /* 貸出記録の登録 */
            queryForPreparedStatement = INSERT_NEW_LEND_RECORD_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, request.id());
            preparedStatement.setInt(2, user.id());
            preparedStatement.setDate(3, new Date(request.returnSchedule().getTime()));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newRecordId = resultSet.getInt("id");
            } else {
                dataBaseController.rollback();
                close();
                return false;
            }
            resultSet.close();

            /* booksの最新利用記録の更新 */
            queryForPreparedStatement = UPDATE_LASTUSE_ID_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, newRecordId);
            preparedStatement.setInt(2, request.id());
            result = preparedStatement.executeUpdate();
            System.out.println(result);
            if (result == 1) {
                dataBaseController.commit();
                close();
                return true;
            } else {
                dataBaseController.rollback();
                close();
                return false;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            dataBaseController.rollback();
            close();
            return false;
        }
    }

    public boolean returnBook(ReturnRequest request, User user) throws SQLException{
        int targetRecordId = 0;
        int result = 0;
        initParameters();

        try {
            /* 対象の本が貸出中であり、このユーザーがほんとに借りているかをチェックする */
            queryForPreparedStatement = GET_BOOK_DATA_QUERY_BASE + LEND_CHECK_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, request.id());
            preparedStatement.setInt(2, user.id());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                targetRecordId = resultSet.getInt("last_use_log");
            }else{
                close();
                return false;
            }

            queryForPreparedStatement = UPDATE_RETURN_DATE_QUERY;
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            preparedStatement.setInt(1, targetRecordId);
            result = preparedStatement.executeUpdate();
            if(result == 1){
                dataBaseController.commit();
                close();
                return true;
            }else{
                dataBaseController.rollback();
                close();
                return false;
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            dataBaseController.rollback();
            close();
            return false;
        }
    }
}