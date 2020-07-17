package LibraryManager.datamodel;

public class ReturnRequest implements DataTrasferObject {
    private final int id;

    public int id() {
        return id;
    }

    public ReturnRequest(int id) {
        this.id = id;
    }
}
