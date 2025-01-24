package persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

// Interface that allows objects of class type to be serialized in json
public interface JsonSerializable {
    /*
     * EFFECTS: returns this serialized to JSONObject
     */
    JSONObject serialize();

    /*
     * EFFECTS: returns a new blank json object
     */
    default JSONObject createJsonObject() {
        return new JSONObject();
    }

    /*
     * EFFECTS: returns a new blank json object
     */
    default JSONArray createJsonArray() {
        return new JSONArray();
    }

    default JSONArray serializeList(List<? extends JsonSerializable> items) {
        return createJsonArray().putAll(
            items.stream().map(s -> s.serialize()).collect(Collectors.toList())
        );
    }
}
