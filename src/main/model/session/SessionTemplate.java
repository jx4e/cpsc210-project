package model.session;

import java.time.LocalTime;

import org.json.JSONObject;

import persistence.JsonSerializable;

// Represents a template for a session with startTime, endTime, description
public class SessionTemplate implements JsonSerializable {
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    
    /*
     * REQUIRES: startTime is before endTime
     * EFFECTS: sets startTime to startTime; endTime to endTime; description to description;
     */
    public SessionTemplate(LocalTime startTime, LocalTime endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /*
     * EFFECTS: return true if startTime, endTime, description all same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SessionTemplate) {
            SessionTemplate objt = (SessionTemplate) obj;
            return objt.startTime.equals(startTime) 
                && objt.endTime.equals(endTime)
                && objt.description.equals(description);
        }

        return false;
    }

    @Override
    public String toString() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    /*
     * EFFECTS: returns this serialized to json object
     */
    @Override
    public JSONObject serialize() {
        return createJsonObject().put("start", getStartTime().toString())
                    .put("end", getEndTime().toString())
                    .put("description", getDescription());
    }

    /*
     * EFFECTS: returns json deserialized to a session template
     */
    public static SessionTemplate deserialize(JSONObject json) {
        LocalTime start = LocalTime.parse(json.getString("start"));
        LocalTime end = LocalTime.parse(json.getString("end"));
        String description = json.getString("description");

        return new SessionTemplate(start, end, description);
    }
}
