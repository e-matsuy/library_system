package LibraryManager.datamodel;

public class AddCategoryRequest implements DataTrasferObject{
    private final String name;

    public AddCategoryRequest(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
