package hu.wolfman.deimos.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A naplózó osztály tesztje.
 * @author Farkas Péter
 */
public class LoggerTest {
    
    public LoggerTest() {
    }

    /**
     * A get metódus tesztje.
     */
    @Test
    public void testGet() {
        assertFalse(Logger.get() == null);
    }
    
    /**
     * A log metódus tesztje.
     */
    @Test
    public void testLog() {
        Logger.get().log("Teszt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("deimos.log"))) {
            String logfile = reader.readLine();
            assertFalse(logfile == null);
            assertTrue(logfile.contains("Teszt"));
        } catch (Exception e) {
        }
    }
    
}
