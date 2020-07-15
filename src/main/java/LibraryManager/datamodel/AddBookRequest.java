package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AddBookRequest implements DataTrasferObject{
    @SerializedName("book_id")
    private final int bookId;
    @SerializedName("bought_date")
    private final Date boughtDate;
    @SerializedName("purchaser_id")
    private final int purchaserId;

    public AddBookRequest(int bookId, Date boughtDate, int purchaserId) {
        this.bookId = bookId;
        this.boughtDate = boughtDate;
        this.purchaserId = purchaserId;
    }

    public int bookId() {
        return bookId;
    }

    public Date boughtDate() {
        return boughtDate;
    }

    public int purchaserId() {
        return purchaserId;
    }
}
