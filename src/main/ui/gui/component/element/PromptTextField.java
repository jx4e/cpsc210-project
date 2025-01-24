package ui.gui.component.element;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

// Represents JTextField with a prompt that dissapears when clicked
public class PromptTextField extends JTextField {
    private String prompt;

    /*
     * EFFECTS: constructs new JPromptTextField with prompt
     */
    public PromptTextField(String prompt) {
        super(prompt);

        this.prompt = prompt;

        this.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (getText().equals(prompt)) {
                    setText("");
                } 
            }
        
            public void focusLost(FocusEvent e) {
                if (getText().equals("")) {
                    setText(prompt);
                }
            }
        });
    }

    /*
     * MODIFIES: this
     * EFFECTS: restores text to the prompt
     */
    public void reset() {
        setText(prompt);
    }
}
