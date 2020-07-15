package LibraryManager.datamodel;

public class Publisher implements DataTrasferObject{
    private final int id;
    private final String name;

    public Publisher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }
}
