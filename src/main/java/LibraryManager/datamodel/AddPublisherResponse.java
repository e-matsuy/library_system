package LibraryManager.datamodel;


public class AddPublisherResponse implements DataTrasferObject {
    private final int id;

    public AddPublisherResponse(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
