package ui.gui.component.element;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import ui.Main;

// Represents a date entry field
public class DateTextField extends PromptTextField {
    public DateTextField(String prompt) {
        super(prompt + " (" + Main.DATE_STRING.toLowerCase() + ")");
    }

    /*
     * EFFECTS: return true if entered date is valid
     */
    public boolean isValidDate() {
        try {
            getDate();
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /*
     * EFFECTS: return entered date parsed to LocalDate
     */
    public LocalDate getDate() {
        return LocalDate.parse(
                getText(), Main.DATE_FORMAT);
    }
}
