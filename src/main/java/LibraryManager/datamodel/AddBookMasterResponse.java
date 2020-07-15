package LibraryManager.datamodel;

public class AddBookMasterResponse implements DataTrasferObject {
    private final int id;

    public AddBookMasterResponse(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
