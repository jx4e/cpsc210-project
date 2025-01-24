package ui.gui.component.element;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import ui.Main;

// Represents a date entry field
public class TimeTextField extends PromptTextField {
    public TimeTextField(String prompt) {
        super(prompt + " (" + Main.TIME_STRING.toLowerCase() + ")");
    }

    /*
     * EFFECTS: return true if entered date is valid
     */
    public boolean isValidTime() {
        try {
            getTime();
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /*
     * EFFECTS: return entered date parsed to LocalDate
     */
    public LocalTime getTime() {
        return LocalTime.parse(
                getText(), Main.TIME_FORMAT);
    }
}
