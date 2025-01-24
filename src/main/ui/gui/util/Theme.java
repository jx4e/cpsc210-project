package ui.gui.util;

import java.awt.Color;
import java.awt.Font;

// Represents a colour theme for the GUI
public class Theme {
    /*
     * The main accent colour
     */
    private Color accent;

    /*
     * The colour used on raised elements
     */
    private Color surface;

    /*
     * The colour used for backgrounds
     */
    private Color background;

    /*
     * The colour used for fonts
     */
    private Color text;

    /*
     * The font used
     */
    private Font font;  

    /*
     * EFFECTS: constructs a new theme with colours provided
     */
    public Theme(Color accent, Color surface, Color background, Color text, Font font) {
        this.accent = accent;
        this.surface = surface;
        this.background = background;
        this.text = text;
        this.font = font;
    }

    public Color getAccent() {
        return accent;
    }

    public Color getSurface() {
        return surface;
    }

    public Color getBackground() {
        return background;
    }

    public Color getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public static final Theme TBIRD = new Theme(
                Color.WHITE,
                Color.decode("#0458b3"), 
                Color.decode("#002144"), 
                Color.WHITE,
                new Font("Arial", 0, 14));
}
