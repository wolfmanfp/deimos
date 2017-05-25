package hu.wolfman.deimos.tools;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A játék történéseit naplózó osztály.
 * @author Farkas Péter
 */
public final class Logger {
    private static Logger INSTANCE;
    private final String filename;
    private final Date date;
    private SimpleDateFormat dateFormat;

    private Logger() {
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        filename = String.format("deimos_%s.log", dateFormat.format(date));
    }
    
    /**
     * A naplózó osztály statikus példányát visszaadó metódus.
     * Amennyiben a példány nem létezik, ebben a metódusban jön létre.
     * @return A naplózó osztály példánya.
     */
    public static Logger getInstance() {
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
        dateFormat = new SimpleDateFormat("yyyy. MM. dd. hh:mm:ss");
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(String.format("%s %s", 
                    dateFormat.format(date),
                    message)
            );
        } catch (Exception e) {
            //TODO
        }
        
    }
}
