package model.session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import model.event.Event;
import model.event.EventLog;
import model.user.Athlete;

// Represents a session on a specific day with a list of attending athletes
public class Session extends SessionTemplate {
    private LocalDate date;
    private ArrayList<Athlete> attending;

    /*
     * REQUIRES: startTime is before endTime
     * EFFECTS: sets date to date; attending to attending; startTime to startTime;
     *          endTime to endTime; description to description;
     */
    public Session(LocalDate date, ArrayList<Athlete> attending, LocalTime startTime,
                     LocalTime endTime, String description) {
        super(startTime, endTime, description);
        this.date = date;
        this.attending = attending;
    }

    /*
     * REQUIRES: startTime is before endTime
     * EFFECTS: sets date to date; attending to empty arraylist; startTime to startTime; 
     *          endTime to endTime; description to description;
     */
    public Session(LocalDate date, LocalTime startTime, LocalTime endTime, String description) {
        this(date, new ArrayList<>(), startTime, endTime, description);
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds athlete to attending
     */
    public void register(Athlete athlete) {
        attending.add(athlete);

        EventLog.getInstance().logEvent(new Event("Registered " + athlete + " to " + this));
    }

    /*
     * EFFECTS: returns true if athlete is in attending
     */
    public boolean isPresent(Athlete athlete) {
        return attending.contains(athlete);
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Athlete> getAttending() {
        return attending;
    }
}
