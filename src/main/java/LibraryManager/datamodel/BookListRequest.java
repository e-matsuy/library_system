package LibraryManager.datamodel;

public class BookListRequest implements DataTrasferObject {
    public static final String SIZE_PARAMETER_NAME = "size";
    public static final String OFFSET_PARAMETER_NAME = "offset";
    public static final String YOU_PARAMETER_NAME = "you";
    public static final String OVERDUE_PARAMETER_NAME = "overdue";
    public static final String ID_PARAMETER_NAME = "id";
    public static final String AUTHOR_PARAMETER_NAME = "author";
    public static final String ISBN_PARAMETER_NAME = "isbn";

    private int size = 20;
    private int offset = 0;
    private boolean you = false;
    private boolean overdue = false;
    private Integer id = null;
    private String name = null;
    private String author = null;
    private String isbn = null;

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOffset(int offset) {
            this.offset = offset;
    }

    public void setYou(boolean you) {
        this.you = you;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int size() {
        return size;
    }

    public int offset() {
        return offset;
    }

    public boolean you() {
        return you;
    }

    public boolean overdue() {
        return overdue;
    }

    public Integer id() {
        return id;
    }

    public String author() {
        return author;
    }

    public String isbn() {
        return isbn;
    }
}
