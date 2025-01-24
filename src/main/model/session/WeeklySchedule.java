package model.session;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import model.event.Event;
import model.event.EventLog;
import persistence.JsonSerializable;

// Represents a weekly schedule of session templates
public class WeeklySchedule implements JsonSerializable {
    private HashMap<DayOfWeek, ArrayList<SessionTemplate>> weeklyTemplate;

    /*
     * EFFECTS: creates a weekly schedule with the weekly template being set to a blank hashmap
     */
    public WeeklySchedule() {
        weeklyTemplate = new HashMap<>();
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds sessionTemplate to weeklyTemplate under the day
     */
    public void addSessionTemplate(DayOfWeek day, SessionTemplate sessionTemplate) {
        EventLog.getInstance().logEvent(new Event("Added " + sessionTemplate + " on " + day + " to " + this));

        if (weeklyTemplate.containsKey(day)) {
            weeklyTemplate.get(day).add(sessionTemplate);
        } else {
            ArrayList<SessionTemplate> templates = new ArrayList<>();
            templates.add(sessionTemplate);
            weeklyTemplate.put(day, templates);
        }
    }

    /*
     * EFFECTS: returns list of session templates on day
     */
    public ArrayList<SessionTemplate> getSessionTemplatesOnDay(DayOfWeek day) {
        if (weeklyTemplate.containsKey(day)) {
            return weeklyTemplate.get(day);
        }

        return new ArrayList<>();
    }

    /*
     * EFFECTS: returns list of sessions for the date based on the weekly template
     */
    public ArrayList<Session> getSessionsOnDate(LocalDate date) {
        ArrayList<Session> sessions = new ArrayList<>();
        ArrayList<SessionTemplate> templatesForDate = getSessionTemplatesOnDay(date.getDayOfWeek());

        for (SessionTemplate template : templatesForDate) {
            sessions.add(new Session(date, template.getStartTime(), template.getEndTime(), template.getDescription()));
        }

        return sessions;
    }

    public HashMap<DayOfWeek, ArrayList<SessionTemplate>> getWeeklyTemplate() {
        return weeklyTemplate;
    }

    /*
     * EFFECTS: returns this serialized to json object
     */
    @Override
    public JSONObject serialize() {
        JSONObject json = createJsonObject();

        weeklyTemplate.forEach((k, v) -> json.put(k.toString(), serializeList(v)));

        return json;
    }

    /*
     * EFFECTS: returns a new weekly schedule created from a json object
     */
    public static WeeklySchedule deserialize(JSONObject json) {
        WeeklySchedule schedule = new WeeklySchedule();

        json.keySet().forEach(
                k -> {
                    JSONArray a = json.getJSONArray(k);

                    for (int i = 0; i < a.length(); i++) {
                        schedule.addSessionTemplate(
                                DayOfWeek.valueOf(k),
                                SessionTemplate.deserialize(a.getJSONObject(i))
                        );
                    }
            }
        );

        return schedule;
    }
}
