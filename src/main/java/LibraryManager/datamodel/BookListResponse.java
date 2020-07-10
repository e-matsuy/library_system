package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookListResponse implements DataTrasferObject {
    private final List<Book> books;
    private final int page;
    @SerializedName("result_count")
    private final int resultCount;

    public BookListResponse(List<Book> books, int page, int resultCount){
        this.books = books;
        this.page = page;
        this.resultCount = resultCount;
    }
}
