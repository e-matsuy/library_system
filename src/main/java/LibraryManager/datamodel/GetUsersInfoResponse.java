package LibraryManager.datamodel;

import java.util.List;

public class GetUsersInfoResponse implements DataTrasferObject{
    private final List<User> users;

    public GetUsersInfoResponse(List<User> users) {
        this.users = users;
    }
}
