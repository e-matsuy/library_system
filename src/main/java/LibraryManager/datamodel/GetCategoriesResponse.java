package LibraryManager.datamodel;

import java.util.List;

public class GetCategoriesResponse implements DataTrasferObject{
    private final List<Category> categories;

    public GetCategoriesResponse(List<Category> categories) {
        this.categories = categories;
    }
}
