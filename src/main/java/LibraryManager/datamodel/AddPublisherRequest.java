package LibraryManager.datamodel;

public class AddPublisherRequest implements DataTrasferObject{
    private final String name;

    public AddPublisherRequest(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
