package model.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import model.event.Event;
import model.event.EventLog;
import model.session.Session;
import model.session.SessionTemplate;
import model.session.WeeklySchedule;
import persistence.JsonSerializable;

// Represents a squad of athletes with a uuid, name, list of athletes and coach(s)
public class Squad implements JsonSerializable {
    private UUID uuid;
    private String name;
    private ArrayList<Athlete> athletes;
    private ArrayList<Coach> coaches;
    private WeeklySchedule weeklySchedule;
    private TreeMap<LocalDate, ArrayList<Session>> recordedSessions; // treemap to sort the dates in order

    /*
     * EFFECTS: sets squad uuid to uuid; name to name; athletes to athletes; coaches to coaches;
     */
    public Squad(UUID uuid, String name, ArrayList<Athlete> athletes, ArrayList<Coach> coaches,
                    WeeklySchedule weeklySchedule, TreeMap<LocalDate, ArrayList<Session>> recordedSessions) {
        this.uuid = uuid;
        this.name = name;
        this.athletes = athletes;
        this.coaches = coaches;
        this.weeklySchedule = weeklySchedule;
        this.recordedSessions = recordedSessions;
    }

    /*
     * EFFECTS: sets squad uuid to uuid; name to name; athletes to athletes; coaches to coaches;
     */
    public Squad(UUID uuid, String name, ArrayList<Athlete> athletes, ArrayList<Coach> coaches,
                    WeeklySchedule weeklySchedule) {
        this(uuid, name, athletes, coaches, weeklySchedule, new TreeMap<>());
    }
    
    /*
     * EFFECTS: sets squad uuid to uuid; name to name; athletes to athletes; coaches to coaches;
     */
    public Squad(UUID uuid, String name, ArrayList<Athlete> athletes, ArrayList<Coach> coaches) {
        this(uuid, name, athletes, coaches, new WeeklySchedule());
    }

    /*
     * EFFECTS: sets squad uuid to random new uuid; name to name; athletes to empty arraylist;
     *          coaches to empty arraylist;
     */
    public Squad(String name) {
        this(UUID.randomUUID(), name, new ArrayList<>(), new ArrayList<>());
    }

    /*
     * EFFECTS: sets squad uuid to random new uuid; name to name; athletes to empty arraylist;
     *          coaches to empty arraylist;
     */
    public Squad(String name, ArrayList<Athlete> athletes, ArrayList<Coach> coaches) {
        this(UUID.randomUUID(), name, athletes, coaches);
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds session template to weeklySchedule
     */
    public void addSessionTemplateToWeeklySchedule(DayOfWeek day, SessionTemplate sessionTemplate) {
        weeklySchedule.addSessionTemplate(day, sessionTemplate);
    }

    /*
     * MODIFIES: this
     * EFFECTS: if recorded sessions contains the template on the date:
     *              adds attending to the list of attending in that session
     * 
     *          else:
     *              creates new list of sessions on that date in recoededSessions
     *              adds attending to the list of attending in that session
     */
    public void recordAttendance(LocalDate date, SessionTemplate template, ArrayList<Athlete> attending) {
        EventLog.getInstance().logEvent(new Event("Recorded attendance for " + template + " with " 
                + attending + " attending on " + date + " in squad " + this));

        if (!weeklySchedule.getSessionTemplatesOnDay(date.getDayOfWeek()).contains(template)) {
            return;
        }

        if (recordedSessions.containsKey(date)) {
            ArrayList<Session> sessions = recordedSessions.get(date);

            if (sessions.contains(template)) {
                sessions.get(sessions.indexOf(template)).getAttending().addAll(attending);
            } else {
                sessions.add(
                    new Session(date, attending, template.getStartTime(),
                        template.getEndTime(), template.getDescription()));
            }
        } else {
            recordedSessions.put(
                    date,
                    new ArrayList<>(Arrays.asList(
                        new Session(date, attending, template.getStartTime(),
                            template.getEndTime(), template.getDescription()))));
        }
    }

    /*
     * REQUIRES: from < to
     * EFFECTS: calculates attendance % for the squad between the two dates
     *          only looks at sessions where attendance was recorded
     */
    public float calculateAttendance(LocalDate from, LocalDate to) {
        ArrayList<Session> sessions = new ArrayList<>(
                    recordedSessions.subMap(from, true, to, true).values()
                    .stream()
                    .flatMap(l -> l.stream())
                    .collect(Collectors.toList()));

        if (sessions.isEmpty()) {
            return 0;
        }

        int total = 0;
        
        for (Session session : sessions) {
            total += session.getAttending().size();
        }

        return 100f * (float) total / (float) (sessions.size() * athletes.size());
    }

    /*
     * REQUIRES: from < to
     * EFFECTS: calculates attendance % for the squad between the two dates
     *          only looks at sessions where attendance was recorded
     */
    public float calculateAttendanceForAthlete(Athlete athlete, LocalDate from, LocalDate to) {
        ArrayList<Session> sessions = new ArrayList<>(
                    recordedSessions.subMap(from, true, to, true).values()
                    .stream()
                    .flatMap(l -> l.stream())
                    .collect(Collectors.toList()));

        if (sessions.isEmpty()) {
            return 0;
        }

        int total = 0;
        
        for (Session session : sessions) {
            if (session.getAttending().contains(athlete)) {
                total += 1;
            }
        }

        return 100f * (float) total / (float) (sessions.size());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(ArrayList<Athlete> athletes) {
        this.athletes = athletes;
    }

    public ArrayList<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(ArrayList<Coach> coaches) {
        this.coaches = coaches;
    }

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    public TreeMap<LocalDate, ArrayList<Session>> getRecordedSessions() {
        return recordedSessions;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /*
     * EFFECTS: returns json object of this serialized
     */
    @Override
    public JSONObject serialize() {
        return createJsonObject().put("uuid", getUuid().toString())
                    .put("name", getName())
                    .put("athletes", serializeList(athletes))
                    .put("coaches", serializeList(coaches))
                    .put("schedule", weeklySchedule.serialize());
    }

    /*
     * EFFECTS: returns new squad deserialized from json
     */
    public static Squad deserialize(JSONObject json) {
        UUID uuid = UUID.fromString(json.getString("uuid"));

        String name = json.getString("name");

        ArrayList<Athlete> athletes = new ArrayList<>();
        JSONArray athleteArray = json.getJSONArray("athletes");

        for (int i = 0; i < athleteArray.length(); i++) {
            athletes.add(Athlete.deserialize(athleteArray.getJSONObject(i)));
        }

        ArrayList<Coach> coaches = new ArrayList<>();
        JSONArray coachArray = json.getJSONArray("coaches");

        for (int i = 0; i < coachArray.length(); i++) {
            coaches.add(Coach.deserialize(coachArray.getJSONObject(i)));
        }


        WeeklySchedule weeklySchedule = WeeklySchedule.deserialize(json.getJSONObject("schedule"));

        return new Squad(uuid, name, athletes, coaches, weeklySchedule);
    }
}
