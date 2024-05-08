import org.junit.Test;

import java.util.TimeZone;

public class TimeZoneTest {

    @Test
    public void timeZone(){
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("DisplayName: %s, ID: %s, Offset: %s%n",
                TimeZone.getDefault().getDisplayName(),
                TimeZone.getDefault().getID(),
                TimeZone.getDefault().getRawOffset());
        System.out.println("------------------------------------------------------------------------------------------");

    }
}
