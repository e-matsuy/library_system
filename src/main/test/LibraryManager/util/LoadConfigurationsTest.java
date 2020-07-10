package LibraryManager.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoadConfigurationsTest {
    @Test
    public void canReadProperties(){
        System.out.println(LoadConfigurations.get("secret"));
    }
}