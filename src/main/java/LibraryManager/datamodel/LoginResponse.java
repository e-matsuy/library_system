package LibraryManager.datamodel;

public class LoginResponse implements DataTrasferObject {
    private final String token;
    private final int expires_in;

    public LoginResponse(String token, int expires_in) {
        this.token = token;
        this.expires_in = expires_in;
    }

    public String token() {
        return token;
    }

    public int expires_in() {
        return expires_in;
    }
}
