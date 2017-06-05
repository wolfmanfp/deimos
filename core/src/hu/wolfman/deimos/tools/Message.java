package hu.wolfman.deimos.tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Swing-alapú üzeneteket megjelenítő osztály.
 * @author Farkas Péter
 */
public class Message {
    
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Hibaüzenet megjelenítése.
     * @param text A hibaüzenet szövege.
     * @param title Az ablak fejlécén megjelenő szöveg.
     */
    public static void errorMessage(String text, String title) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, text, title, JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }
    
    /**
     * Tájékoztató üzenet megjelenítése.
     * @param text A tájékoztató üzenet szövege.
     * @param title Az ablak fejlécén megjelenő szöveg.
     */
    public static void infoMessage(String text, String title) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, text, title, JOptionPane.PLAIN_MESSAGE);
        frame.dispose();
    }
    
}
