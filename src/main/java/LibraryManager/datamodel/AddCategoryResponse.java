package LibraryManager.datamodel;


public class AddCategoryResponse implements DataTrasferObject {
    private final int id;

    public AddCategoryResponse(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
