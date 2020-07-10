package LibraryManager.util;

import org.junit.Before;
import org.junit.Test;

public class TokenControllerTest {

    TokenCertificator target;
    String testSecret = "qwertyuiop";
    @Before
    public void before(){
        target = new TokenCertificator(testSecret);
    }

    /*
    @Test
    public void トークンが生成できる(){
        //assertNotNull(target.generateToken(new User(123, employeeNumber, name, nameKana, mailAddress, role, datOfHire)));
    }
     */


}