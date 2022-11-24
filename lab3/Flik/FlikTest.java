import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    public void isSameNumberTest() {
        assertTrue(Flik.isSameNumber(127,127));
        boolean actual = Flik.isSameNumber(128, 128);
        int x = 128;
        int y = 128;
        Integer i = 128;
        Integer j = 128;
        assertTrue(x == y);
        assertTrue(i == j);
        assertTrue(actual);
    }
}