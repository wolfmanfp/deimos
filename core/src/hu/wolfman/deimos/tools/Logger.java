package hu.wolfman.deimos.tools;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Általános célú naplózó osztály.
 * @author Farkas Péter
 */
public final class Logger {
    public final String filename;
    private static Logger INSTANCE;

    private Logger() {
        filename = String.format("deimos.log");
    }
    
    /**
     * A naplózó osztály statikus példányát visszaadó metódus.
     * Amennyiben a példány nem létezik, ebben a metódusban jön létre.
     * @return A naplózó osztály példánya.
     */
    public static Logger get() {
        if (INSTANCE == null) {
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }
    
    /**
     * A naplófájl tartalmához hozzáfűz egy dátummal ellátott üzenetet.
     * @param message A naplóban megjelenő üzenet.
     */
    public void log(String message) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            String timeLog = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")
                    .format(Calendar.getInstance().getTime());
            writer.write(String.format("[%s] %s\r\n", timeLog, message));
        } catch (Exception e) {
            Message.errorMessage("Naplózási hiba", "Hiba");
        }
    }
    
}
