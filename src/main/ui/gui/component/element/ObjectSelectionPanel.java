package ui.gui.component.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import ui.gui.util.Theme;

// Represents a panel where the user can select something
public class ObjectSelectionPanel<T> extends JPanel {
    private Map<T, JCheckBox> checked;

    /*
     * EFFECTS: constructs new list of items to be selected with getText function as the text on the JCheckBox
     */
    public ObjectSelectionPanel(Theme theme, ArrayList<T> objects, Function<T, String> getText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(theme.getBackground());

        checked = new HashMap<>();

        for (T t : objects) {
            JCheckBox check = new JCheckBox(getText.apply(t));
            check.setForeground(theme.getText());
            checked.put(t, check);
            add(check);
        }
    }

    /*
     * EFFECTS: constructs new list of items to be selected with getText function as the text on the JCheckBox
     */
    public ObjectSelectionPanel(Theme theme, ArrayList<T> objects, ArrayList<T> selected, Function<T, String> getText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(theme.getBackground());

        checked = new HashMap<>();

        for (T t : objects) {
            JCheckBox check = new JCheckBox(getText.apply(t));

            check.setSelected(selected.contains(t));

            check.setForeground(theme.getText());
            checked.put(t, check);
            add(check);
        }
    }

    /*
     * EFFECTS: returns list of checked items
     */
    public ArrayList<T> getSelected() {
        ArrayList<T> selected = new ArrayList<>();
                
        for (Map.Entry<T, JCheckBox> entry : checked.entrySet()) { 
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }

        return selected;
    }
}
