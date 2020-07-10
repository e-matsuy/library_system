package LibraryManager.service;

import LibraryManager.datamodel.*;
import LibraryManager.util.DataBaseController;
import LibraryManager.util.GsonSingleton;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class BookDataService {
    private final String GET_BOOK_DATA_QUERY_BASE = "select books.id, books.book as book_master_id, book_master.title, book_master.author as author, book_master.publisher as publisher_id, publishers.name as publisher_name, book_master.category as category_id, categories.name as category_name, lending_records.return_schedule as return_schedule, book_master.isbn, books.last_use_log as last_use_log, lending_records.user_id as last_user_id, users.name as last_user_name, lending_records.updated_at as last_use_date, lending_records.returned_date as returned_date from books LEFT OUTER JOIN book_master ON books.book = book_master.id LEFT OUTER JOIN publishers ON book_master.publisher = publishers.id LEFT OUTER JOIN categories ON book_master.category = categories.id LEFT OUTER JOIN lending_records ON books.last_use_log = lending_records.id LEFT OUTER JOIN users ON lending_records.user_id = users.id ";
    private final String GET_BOOK_DATA_COUNT_QUERY_BASE = "select count(*) from books LEFT OUTER JOIN book_master ON books.book = book_master.id LEFT OUTER JOIN publishers ON book_master.publisher = publishers.id LEFT OUTER JOIN categories ON book_master.category = categories.id LEFT OUTER JOIN lending_records ON books.last_use_log = lending_records.id LEFT OUTER JOIN users ON lending_records.user_id = users.id ";
    private final String LIMIT_QUERY = "LIMIT ? OFFSET ? ";
    private final String LENDING_BOOKS_WHERE_QUERY = "WHERE lending_records.user_id = ? AND lending_records.returned_date IS NULL ";
    private final String OVERDUE_WHERE_QUERY = "WHERE lending_records.return_schedule < CURRENT_DATE AND lending_records.returned_date IS NULL ";
    private DataBaseController dataBaseController = null;
    private PreparedStatement preparedStatement = null;
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
        }else{
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

    private PreparedStatement createStatement(DataBaseController controller, BookListRequest request, User requestUser) throws SQLException{
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

        }else{
            /* クエリのためのパラメータを用意 */
            queryForPreparedStatement = makeQueryForSearchBooks(request, false);
            preparedStatement = controller.preparedStatement(queryForPreparedStatement);
            setSearchRulesToStatement(preparedStatement, request, true);
        }
        System.out.println(queryForPreparedStatement);
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
        if(request.you()) {
            queryForPreparedStatement = makeQueryForLendingList(true);
        }else if(request.overdue()){
            queryForPreparedStatement = makeQueryForOverdueList(true);
        }else{
            queryForPreparedStatement = makeQueryForSearchBooks(request, true);
        }

        try {
            preparedStatement = dataBaseController.preparedStatement(queryForPreparedStatement);
            /* クエリのためのパラメータを用意 */
            if(request.you()) {
                // ユーザーの貸出中リストを取得するときはパラメータは1つだけ
                preparedStatement.setInt(1, requestUser.id());
            }else if(request.overdue()){
                /* do nothing */
            }else{
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
}
