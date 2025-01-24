package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import model.event.Event;
import model.event.EventLog;
import model.session.WeeklySchedule;
import model.user.Squad;

// Represets a json reader to deserialize json objects
public class JsonReader {
    private FileManager manager;

    /*
     * EFFECTS: creates new JSON reader at location file
     */
    public JsonReader(FileManager manager) {
        this.manager = manager;
    }

    /*
     * EFFECTS: reads file and returns the contents deserialized to a squad
     */
    public Squad deserializeSquad(String name) throws FileNotFoundException, IOException {
        return deserializeSquad(manager.getSquad(name));
    }

    /*
     * EFFECTS: reads file and returns the contents deserialized to a squad
     */
    public Squad deserializeSquad(File file) throws FileNotFoundException, IOException {
        return Squad.deserialize(read(file));
    }

    /*
     * EFFECTS: reads file and returns the contents deserialized to a weeklyschedule
     */
    public WeeklySchedule deserializeSchedule(String name) throws FileNotFoundException, IOException {
        return WeeklySchedule.deserialize(read(manager.getSchedule(name)));
    }

    /*
     * EFFECTS: reads the file and parses contents into a JSONObject
     */
    private JSONObject read(File file) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(file);

        JSONObject json = new JSONObject(new String(in.readAllBytes()));

        in.close();

        EventLog.getInstance().logEvent(new Event("Read file " + file));

        return json;
    }
}
