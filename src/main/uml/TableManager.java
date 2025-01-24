package uml;

// Represents a singleton tablemanager
public class TableManager {
    private int currentId;
    private int currentX;

    private static TableManager instance;

    private TableManager() {
        currentId = 2;
    }

    /*
     * MODIFIES: this
     * EFFECTS: returns current id and increments
     */
    public int getNext() {
        return currentId++;
    }

    /*
     * MODIFIES: this
     * EFFECTS: returns current id and increments
     */
    public String getNextString() {
        return String.valueOf(getNext());
    }

    /*
     * MODIFIES: this
     * EFFECTS: returns current x and increments
     */
    public int getNextX() {
        return currentX += 180;
    }

    /*
     * MODIFIES: this
     * EFFECTS: returns current x and increments
     */
    public String getNextXString() {
        return String.valueOf(getNextX());
    }

    public static TableManager getTableManager() {
        if (instance == null)  {
            instance = new TableManager();
        }

        return instance;
    }
}
