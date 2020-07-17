package LibraryManager.datamodel;

import LibraryManager.datamodel.DataTrasferObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CheckoutRequest implements DataTrasferObject {
    private final int id;
    @SerializedName("return_schedule")
    private final Date return_schedule;

    public CheckoutRequest(int id, Date return_schedule) {
        this.id = id;
        this.return_schedule = return_schedule;
    }

    public int id() {
        return id;
    }

    public Date returnSchedule() {
        return return_schedule;
    }
}
