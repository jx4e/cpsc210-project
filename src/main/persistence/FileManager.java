package persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

// Manages file locations for project
public class FileManager {
    private final File rootDir;
    private final File squadDir;
    private final File scheduleDir;
    
    /*
     * EFFECTS: makes new file manager object with:
     *          root dir = data
     *          squad dir = data/squads
     *          schedule dir = data/schedules
     */
    public FileManager() {
        rootDir = new File("./data/");
        rootDir.mkdirs();

        squadDir = new File(rootDir, "squads");
        squadDir.mkdirs();

        scheduleDir = new File(rootDir, "schedules");
        scheduleDir.mkdirs();
    }

    public File getRootDir() {
        return rootDir;
    }

    public File getSquadDir() {
        return squadDir;
    }

    public File getScheduleDir() {
        return scheduleDir;
    }

    /*
     * EFFECTS: returns list of all squad file names
     */
    public ArrayList<String> getSquadNames() {
        return new ArrayList<>(Arrays.asList(squadDir.list()));
    }

    /*
     * EFFECTS: returns list of all scheudle names
     */
    public ArrayList<String> getScheduleNames() {
        return new ArrayList<>(Arrays.asList(scheduleDir.list()));
    }

    /*
     * EFFECTS: return file location of squad
     */
    public File getSquad(String name) {
        return new File(squadDir, name + ".json");
    }

    /*
     * EFFECTS: return file location of schedule
     */
    public File getSchedule(String name) {
        return new File(scheduleDir, name + ".json");
    }
}
