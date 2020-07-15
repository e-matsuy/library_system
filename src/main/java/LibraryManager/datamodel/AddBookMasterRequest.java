package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

public class AddBookMasterRequest {

    private final String name;
    private final String author;
    @SerializedName("publisher_id")
    private final int publisherId;
    @SerializedName("category_id")
    private final int categoryId;
    private final String isbn;

    public AddBookMasterRequest(String name, String author, int publisherId, int categoryId, String isbn) {
        this.name = name;
        this.author = author;
        this.publisherId = publisherId;
        this.categoryId = categoryId;
        this.isbn = isbn;
    }

    public String name() {
        return name;
    }

    public String author() {
        return author;
    }

    public int publisherId() {
        return publisherId;
    }

    public int categoryId() {
        return categoryId;
    }

    public String isbn() {
        return isbn;
    }
}
