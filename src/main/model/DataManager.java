package model;

import java.util.ArrayList;

import model.event.Event;
import model.event.EventLog;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;
import model.user.User;

// A user manager keeping track of loaded data in the application
// Has a list of loaded users
// Has a list of loaded squads
public class DataManager {
    private ArrayList<User> users;
    private ArrayList<Squad> squads;

    /*
     * EFFECTS: creates DataManager with empty users and squads
     */
    public DataManager() {
        this.users = new ArrayList<>();
        this.squads = new ArrayList<>();
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds user to user list
     */
    public void loadUser(User user) {
        EventLog.getInstance().logEvent(new Event("Loaded user " + user + " in " + this));

        if (users.contains(user)) {
            return;
        }

        this.users.add(user);
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds squad to squads
     */
    public void loadSquad(Squad squad) {
        EventLog.getInstance().logEvent(new Event("Loaded squad " + squad + " in " + this));

        squad.getAthletes().forEach(this::loadUser);
        squad.getCoaches().forEach(this::loadUser);

        this.squads.add(squad);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /*
     * EFFECTS: returns list of users from users who are coaches
     */
    public ArrayList<Coach> getCoaches() {
        // could use users.stream() but not sure if its better for marks to do it this way??
        
        ArrayList<Coach> coaches = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Coach) {
                coaches.add((Coach) user);
            }
        }

        return coaches;
    }
    
    /*
     * EFFECTS: returns list of users from users who are athletes
     */
    public ArrayList<Athlete> getAthletes() {
        ArrayList<Athlete> athletes = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Athlete) {
                athletes.add((Athlete) user);
            }
        }

        return athletes;
    }

    public ArrayList<Squad> getSquads() {
        return squads;
    }

    /*
     * EFFECTS: returns list of squads who are coached by coach
     */
    public ArrayList<Squad> getSquadsByCoach(Coach coach) {
        ArrayList<Squad> coachedSquads = new ArrayList<>();

        for (Squad squad : squads) {
            if (squad.getCoaches().contains(coach)) {
                coachedSquads.add(squad);
            }
        }

        return coachedSquads;
    }
}
