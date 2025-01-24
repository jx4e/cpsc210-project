package model.user;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONObject;

import persistence.JsonSerializable;

// Represents a coach
public class Coach extends User implements JsonSerializable {
    /*
     * EFFECTS: uuid of user is set to uuid;
     *          name of user is set to name;
     *          dateOfBirth of user set to dateOfBirth;
     */
    public Coach(UUID uuid, String name, LocalDate dateOfBirth) {
        super(uuid, name, dateOfBirth);
    }

    /*
     * EFFECTS: uuid of user is set to a random new UUID;
     *          name of user is set to name;
     *          dateOfBirth of user set to dateOfBirth;
     */
    public Coach(String name, LocalDate dateOfBirth) {
        super(name, dateOfBirth);
    }

    /*
     * EFFECTS: returns this serialized to json object
     */
    @Override
    public JSONObject serialize() {
        return createJsonObject().put("uuid", getUuid().toString())
                    .put("name", getName())
                    .put("dob", getDateOfBirth().toString());
    }

    /*
     * EFFECTS: returns new Coach deserialized from json object
     */
    public static Coach deserialize(JSONObject json) {
        UUID uuid = UUID.fromString(json.getString("uuid"));
        String name = json.getString("name");
        LocalDate dateOfBirth = LocalDate.parse(json.getString("dob"));

        return new Coach(uuid, name, dateOfBirth);
    }
}
