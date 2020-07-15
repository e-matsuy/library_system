package LibraryManager.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Book {
    private final int id;
    private final String title;
    private final String author;
    private transient int publisherId;
    @SerializedName("publisher")
    private final String publisherName;
    private transient final int categoryId;
    @SerializedName("category")
    private final String categoryName;
    private final String isbn;
    @SerializedName("return_schedule")
    private final Date returnSchedule;
    @SerializedName("you")
    private final boolean isYou;
    @SerializedName("last_user")
    private Integer lastUserId = null;
    @SerializedName("last_user_name")
    private String lastUserName = null;
    @SerializedName("last_mod_date")
    private Date lastModDate = null;
    @SerializedName("last_action")
    private Action lastAction = null;

    public Book(int id, String title, String author, int publisherId, String publisherName, int categoryId, String categoryName, String isbn, Date returnSchedule, boolean isYou) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isbn = isbn;
        this.returnSchedule = returnSchedule;
        this.isYou = isYou;
        this.lastUserId = lastUserId;
    }

    public void setLastUserId(Integer lastUserId) {
        this.lastUserId = lastUserId;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }
}
