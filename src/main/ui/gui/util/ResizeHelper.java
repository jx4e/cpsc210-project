package ui.gui.util;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Supplier;

import javax.swing.JComponent;

public interface ResizeHelper {
    /*
     * EFFECTS: return width of frame
     */
    int getFrameWidth();

    /*
     * EFFECTS: return height of frame
     */
    int getFrameHeight();

    /*
     * EFFECTS: returns int supplier with the frame width / frac
     */
    default Supplier<Integer> width(int frac) {
        return () -> getFrameWidth() / frac;
    }

    /*
     * EFFECTS: returns int supplier with the component width / frac
     */
    default Supplier<Integer> width(JComponent component, int frac) {
        return () -> (int) component.getPreferredSize().getWidth() / frac;
    }

    /*
     * EFFECTS: returns int supplier with the frame height / frac
     */
    default Supplier<Integer> height(int frac) {
        return () -> getFrameHeight() / frac;
    }

    /*
     * EFFECTS: returns int supplier with the component height / frac
     */
    default Supplier<Integer> height(JComponent component, int frac) {
        return () -> (int) component.getPreferredSize().getHeight() / frac;
    }

    /*
     * MODIFIES: component
     * EFFECTS: returns runnable modifiying size of component to the supplied dimensions
     */
    default Runnable createBasicResizeAction(JComponent component, 
            Supplier<Integer> widthSupplier, Supplier<Integer> heightSupplier) {
        return () -> component.setPreferredSize(new Dimension(widthSupplier.get(), heightSupplier.get()));
    }

    /*
     * MODIFIES: component
     * EFFECTS: adds action to the component resized listener for the component
     */
    default void onResize(JComponent component, Runnable action) {
        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                action.run();
                component.revalidate();
            }
        });
    }
}
