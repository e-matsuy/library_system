package LibraryManager.datamodel;

import java.util.List;

public class GetPublishersResponse implements DataTrasferObject{
    private final List<Publisher> publishers;

    public GetPublishersResponse(List<Publisher> publishers) {
        this.publishers = publishers;
    }
}
