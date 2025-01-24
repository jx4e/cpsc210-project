package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import model.event.Event;
import model.event.EventLog;
import model.session.WeeklySchedule;
import model.user.Squad;

// Represents a JSONWriter used to write JSON to a file
public class JsonWriter {
    private FileManager manager;

    /*
     * EFFECTS: constructs json writer with the file magaer
     */
    public JsonWriter(FileManager manager) {
        this.manager = manager;
    }

    /*
     * MODIFIES: this
     * EFFECTS: write a serializable to the file at the destination
     *          returns true if it was successful
     */
    public boolean write(JsonSerializable serializable, String name) throws IOException, FileNotFoundException {
        if (serializable instanceof Squad) {
            return write(serializable.serialize(), manager.getSquad(name));
        } else if (serializable instanceof WeeklySchedule) {
            return write(serializable.serialize(), manager.getSchedule(name));
        }

        return false;
    }

    /*
     * MODIFIES: this
     * EFFECTS: write a json object to the file at the destination
     *          returns true if it was successful
     */
    private boolean write(JSONObject json, File file) throws IOException, FileNotFoundException {
        file.delete();
        file.createNewFile();

        FileOutputStream out = new FileOutputStream(file);

        out.write(json.toString(4).getBytes());

        out.close();

        EventLog.getInstance().logEvent(new Event("Wrote" + json + " to file " + file));

        return true;
    }
}
