package LibraryManager.datamodel;

import com.google.gson.annotations.SerializedName;

public class Mail implements DataTrasferObject{
    @SerializedName("display_name")
    private final String displayName;
    @SerializedName("mail_to")
    private final String mailTo;
    private final String subject;
    private final String body;
    private String token = null;

    public Mail(String displayName, String mailTo, String subject, String body){
        this.displayName = displayName;
        this.mailTo = mailTo;
        this.subject = subject;
        this.body = body;
    }

    public void setToken(String token){
        this.token = token;
    }
}
