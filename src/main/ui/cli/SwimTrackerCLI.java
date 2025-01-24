package ui.cli;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.DataManager;
import model.session.Session;
import model.session.SessionTemplate;
import model.session.WeeklySchedule;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;
import model.user.User;
import persistence.FileManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import static ui.Main.*;

public class SwimTrackerCLI {
    private Scanner input;
    private DataManager dataManager;
    private Coach currentUser;
    private FileManager fileManager;
    private JsonReader reader;
    private JsonWriter writer;

    /*
     * EFFECTS: runs the app
     */
    public SwimTrackerCLI() {
        run();
    }

    /*
     * EFFECTS: runs the app
     */
    public void run() {
        if (TEST_ENV) {
            initTest();
        } else {
            init();
        }

        while (selectTask()) {
            // just keep running
        }
    }
    
    /*
     * MODIFIES: this
     * EFFECTS: initialises input and sets currentUser based on user's input
     */
    public void init() {
        input = new Scanner(System.in);
        dataManager = new DataManager();
        fileManager = new FileManager();
        reader = new JsonReader(fileManager);
        writer = new JsonWriter(fileManager);

        initCurrentUser();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the environment for testing purposes
     */
    @SuppressWarnings("methodlength")
    public void initTest() {
        input = new Scanner(System.in);
        dataManager = new DataManager();
        fileManager = new FileManager();
        reader = new JsonReader(fileManager);
        writer = new JsonWriter(fileManager);
        
        currentUser = new Coach("Jon Wills", LocalDate.of(1990, 8, 24));
        dataManager.loadUser(currentUser);

        Coach ds = new Coach("Derrick Schoof", LocalDate.of(1975, 10, 7));
        dataManager.loadUser(ds);

        Athlete jg = new Athlete("Jake Gaunt", LocalDate.of(2005, 8, 24));
        dataManager.loadUser(jg);

        Athlete fh = new Athlete("Frank Ho", LocalDate.of(2004, 6, 24));
        dataManager.loadUser(fh);

        Athlete jb = new Athlete("Joel Blanco", LocalDate.of(2004, 2, 12));
        dataManager.loadUser(jb);

        ArrayList<Coach> coaches = new ArrayList<>(Arrays.asList(currentUser, ds));
        ArrayList<Athlete> athletes = new ArrayList<>(Arrays.asList(jg, fh, jb));

        Squad ubct = new Squad("UBCT", athletes, coaches);

        // session templates
        SessionTemplate amSwim = new SessionTemplate(LocalTime.of(6, 30), LocalTime.of(8, 30), "AM Swim");
        SessionTemplate pmSwim = new SessionTemplate(LocalTime.of(15, 0), LocalTime.of(17, 00), "PM Swim");
        SessionTemplate gym = new SessionTemplate(LocalTime.of(8, 15), LocalTime.of(9, 15), "Gym");

        // populate schedule
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, amSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, pmSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.TUESDAY, gym);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.TUESDAY, pmSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.WEDNESDAY, amSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.WEDNESDAY, pmSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.THURSDAY, gym);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.THURSDAY, pmSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.FRIDAY, amSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.FRIDAY, pmSwim);
        ubct.addSessionTemplateToWeeklySchedule(DayOfWeek.SATURDAY, amSwim);

        dataManager.loadSquad(ubct);
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets currentUser to new Coach based on user's input
     */
    public void initCurrentUser() {
        printBanner("Login");

        String name = getUserInput("Welcome, Please enter your name:");
        LocalDate dateOfBirth = LocalDate.parse(
                getUserInput("Please enter your date of birth (DD/MM/YYYY):"), DATE_FORMAT);

        currentUser = new Coach(name, dateOfBirth);
        dataManager.loadUser(currentUser);

        System.out.println("Logged in as: " + "(" + formatUser(currentUser) + ")");
    }

    /*
     * EFFECTS: prints the menu and selects what user wants to do
     */
    @SuppressWarnings("methodlength")
    public boolean selectTask() {
        printBanner("Task Menu");

        System.out.println("(1) Create New User");
        System.out.println("(2) Create New Squad");
        System.out.println("(3) Add Athlete to Squad");
        System.out.println("(4) Remove Athlete to Squad");
        System.out.println("(5) List Athletes in Squad");
        System.out.println("(6) View Schedule for Squad");
        System.out.println("(7) Create Session and Add To Schedule");
        System.out.println("(8) View my scheduled sessions for a day");
        System.out.println("(9) Record Attendance for Session");
        System.out.println("(10) See Attendance for Squad");
        System.out.println("(11) See Attendance for Athlete");
        System.out.println("(12) Save Squad");
        System.out.println("(13) Load Squad");
        System.out.println("(14) Quit");
        System.out.println();
        System.out.println("Select an option (1-12):");

        int selected = Integer.valueOf(input.nextLine());

        if (selected == 14) {
            return false;
        }

        switch (selected) {
            case 1 -> createNewUser();
            case 2 -> createNewSquad();
            case 3 -> addAthleteToSquad();
            case 4 -> removeAthleteFromSquad();
            case 5 -> listAthletesInSquad();
            case 6 -> viewScheduleForSquad();
            case 7 -> createSessionAndAddToSchedule();
            case 8 -> viewScheduledSessionsForDate();
            case 9 -> recordAttendanceForSession();
            case 10 -> seeAttendanceForSquadBetweenDates();
            case 11 -> seeAttendanceForAthleteBetweenDates();
            case 12 -> saveSquad();
            case 13 -> loadSquad();
        }

        System.out.println();
        System.out.println("Press enter to continue.");
        input.nextLine();

        return true;
    }

    /*
     * MODIFIES: this
     * EFFECTS: creates a new user from user input
     */
    public void createNewUser() {
        printBanner("Create New User");

        boolean valid = false;
        String role = null;

        while (!valid) {
            role = getUserInput("Please enter your user's role (c or a):");
            valid = role.equals("c") || role.equals("a");

            if (!valid) {
                System.out.println("Invalid input! Enter c for coach or a for athlete.");
            }
        }

        String name = getUserInput("Please enter your user's name:");
        LocalDate dateOfBirth = LocalDate.parse(
                getUserInput("Please enter your user's date of birth (DD/MM/YYYY):"), DATE_FORMAT);

        User newUser;

        if (role.equals("c")) {
            newUser = new Coach(name, dateOfBirth);
        } else {
            newUser = new Athlete(name, dateOfBirth);
        }

        dataManager.loadUser(newUser);

        System.out.println("Created user: " + "(" + formatUser(newUser) + ")");
    } 

    /*
     * MODIFIES: this
     * EFFECTS: creates a new squad from user input
     */
    public void createNewSquad() {
        printBanner("Create Squad");

        String name = getUserInput("Please enter your squad's name:");
        boolean addAthletes = getUserInput("To add athletes to squad type y:").equals("y");

        ArrayList<Athlete> athletes = new ArrayList<>();

        if (addAthletes) {
            athletes.addAll(getUsersFromList(dataManager.getAthletes()));
        }

        boolean addCoaches = getUserInput("To add other coaches to squad type y:").equals("y");

        ArrayList<Coach> coaches = new ArrayList<>();
        coaches.add(currentUser); // add the current coach to their squad

        if (addCoaches) {
            coaches.addAll(getUsersFromList(dataManager.getCoaches()));
        }

        Squad squad = new Squad(name, athletes, coaches);
        dataManager.loadSquad(squad);

        System.out.println("Created squad: ");
        System.out.println("Name: " + squad.getName() + " UUID: " + squad.getUuid());
        System.out.println("Coaches:");
        printUserList(squad.getCoaches());
        System.out.println("Athletes:");
        printUserList(squad.getAthletes());
    }

    /*
     * EFFECTS: adds athlete to a sqaud specified by user input
     */
    public void addAthleteToSquad() {
        printBanner("Add Athlete To Squad");

        Squad squad = getSquadFromList(dataManager.getSquads());

        System.out.println();
        System.out.println("Current Squad: " + "(" + formatSquad(squad) + ")");

        squad.getAthletes().addAll(getUsersFromList(dataManager.getAthletes()));

        System.out.println();
        System.out.println("Updated Squad: " + "(" + formatSquad(squad) + ")");
    }

    /*
     * EFFECTS: removes athlete to a sqaud specified by user input
     */
    public void removeAthleteFromSquad() {
        printBanner("Remove Athlete From Squad");

        Squad squad = getSquadFromList(dataManager.getSquads());

        System.out.println();
        System.out.println("Current Squad: " + "(" + formatSquad(squad) + ")");

        squad.getAthletes().removeAll(getUsersFromList(dataManager.getAthletes()));

        System.out.println();
        System.out.println("Updated Squad: " + "(" + formatSquad(squad) + ")");
    }

    /*
     * EFFECTS: prints athletes in selected squad
     */
    public void listAthletesInSquad() {
        printBanner("List Athletes In Squad");

        Squad squad = getSquadFromList(dataManager.getSquads());

        printBanner("Athletes");

        printUserList(squad.getAthletes());
    }

    /*
     * EFFECTS: prints schedule for specified squad
     */
    public void viewScheduleForSquad() {
        printBanner("View Schedule For Squad");

        Squad squad = getSquadFromList(dataManager.getSquads());
        printWeeklySchedule(squad.getWeeklySchedule());
    }

    /*
     * EFFECTS: creates session and adds it to a specified squad's schedule
     */
    public void createSessionAndAddToSchedule() {
        printBanner("Create a session");

        Squad squad = getSquadFromList(dataManager.getSquads());
        printWeeklySchedule(squad.getWeeklySchedule());

        DayOfWeek day = DayOfWeek.of(Integer.parseInt(getUserInput("Select day of week (1-7):")));
        LocalTime startTime = LocalTime.parse(getUserInput("Enter a start time (hh:mm):"), TIME_FORMAT);
        LocalTime endTime = LocalTime.parse(getUserInput("Enter an end time (hh:mm):"), TIME_FORMAT);
        String description = getUserInput("Enter a description:");

        SessionTemplate template = new SessionTemplate(startTime, endTime, description);

        squad.addSessionTemplateToWeeklySchedule(day, template);

        System.out.println("Updated Schedule:");
        printWeeklySchedule(squad.getWeeklySchedule());
    }

    /*
     * EFFECTS: prints scheduled sessions for date from all of a coaches squads
     */
    public void viewScheduledSessionsForDate() {
        printBanner("View today's schedule");

        ArrayList<Squad> squads = dataManager.getSquadsByCoach(currentUser);

        LocalDate date = LocalDate.parse(
                getUserInput("Please enter the date of session to get schedule for (DD/MM/YYYY):"), DATE_FORMAT);

        ArrayList<SessionTemplate> sessions = new ArrayList<>(squads.stream()
                .map(Squad::getWeeklySchedule)
                .map(schedule -> schedule.getSessionTemplatesOnDay(date.getDayOfWeek()))
                .flatMap(s -> s.stream())
                .collect(Collectors.toList()));

        System.out.println("Todays sessions:");
        sessions.forEach(this::printSessionTemplate);
    }

    /*
     * EFFECTS: records attendance for a session specified by user inpuit
     */
    public void recordAttendanceForSession() {
        printBanner("Record Attendance For Session");

        System.out.println("Please select a squad to mark attendence for");
        Squad squad = getSquadFromList(dataManager.getSquads());

        LocalDate date = LocalDate.parse(
                getUserInput("Please enter the date of session to record for (DD/MM/YYYY):"), DATE_FORMAT);
        
        System.out.println("Please select a session to mark attendence for");
        SessionTemplate template = selectSessionTemplateFromDate(squad.getWeeklySchedule(), date);

        System.out.println("Please select athletes to mark attendence for");
        ArrayList<Athlete> attending = getUsersFromList(squad.getAthletes());

        squad.recordAttendance(date, template, attending);

        System.out.println("Marked " 
                + attending.stream().map(this::formatUser).collect(Collectors.toList()) + " as attending.");
    }

    /*
     * EFFECTS: printes attendance % for specified squad between 2 dates
     */
    public void seeAttendanceForSquadBetweenDates() {
        printBanner("See attendance for squad");

        System.out.println("Please select a squad to view attendence for");
        Squad squad = getSquadFromList(dataManager.getSquads());
        LocalDate from = LocalDate.parse(
                getUserInput("Please enter the date of session to view attendance from (DD/MM/YYYY):"), DATE_FORMAT);
        LocalDate to = LocalDate.parse(
                getUserInput("Please enter the date of session to view attendance to (DD/MM/YYYY):"), DATE_FORMAT);

        System.out.println("The attendance for " 
                    + squad.getName() + " is: " + squad.calculateAttendance(from, to) + " %");
    }

    /*
     * EFFECTS: prints attendance % for specified athlete between 2 dates
     */
    public void seeAttendanceForAthleteBetweenDates() {
        printBanner("See attendance for athlete");

        System.out.println("Please select a squad the athlete is in");
        Squad squad = getSquadFromList(dataManager.getSquads());
        System.out.println("Please select a user to view attendence for");
        Athlete athlete = getUserFromList(squad.getAthletes());
        LocalDate from = LocalDate.parse(
                getUserInput("Please enter the date of session to view attendance from (DD/MM/YYYY):"), DATE_FORMAT);
        LocalDate to = LocalDate.parse(
                getUserInput("Please enter the date of session to view attendance to (DD/MM/YYYY):"), DATE_FORMAT);

        System.out.println("The attendance for " 
                + squad.getName() + " is: " + squad.calculateAttendanceForAthlete(athlete, from, to) + " %");
    }

    /*
     * REQUIRES: 1+ squad is in data manager
     * EFFECTS: saves squad to file
     */
    public void saveSquad() {
        printBanner("Save Squad");

        System.out.println("Please select a squad to save");
        Squad squad = getSquadFromList(dataManager.getSquads());

        try {
            writer.write(squad, squad.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * REQUIRES: 1+ squad is in the data squads folder
     * EFFECTS: saves squad to file
     */
    public void loadSquad() {
        printBanner("Load Squad");

        System.out.println("Select a squad to load from file");

        fileManager.getSquadNames().forEach(System.out::println);

        String squadName = getUserInput("Type the name of a squad: ");

        try {
            dataManager.loadSquad(reader.deserializeSquad(squadName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * EFFECTS: returns the users input to a prompt
     */
    public String getUserInput(String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    /*
     * EFFECTS: prints nice banner with content
     */
    public void printBanner(String content) {
        System.out.println();
        System.out.println("=".repeat(content.length()));
        System.out.println(content);
        System.out.println("=".repeat(content.length()));
        System.out.println();
    }

    /*
     * EFFECTS: returns the User the user picks from the list
     */
    public <U extends User> U getUserFromList(ArrayList<U> userList) {
        printUserList(userList);

        System.out.println("Type user number:");

        int selected = Integer.parseInt(input.nextLine());
        U user = userList.get(selected - 1); 

        return user;
    }

    /*
     * EFFECTS: returns the Users the user picks from the list
     */
    public <U extends User> ArrayList<U> getUsersFromList(ArrayList<U> userList) {
        ArrayList<U> selectedUsers = new ArrayList<>();

        printUserList(userList);

        System.out.println("Type users numbers separated by a space:");

        selectedUsers.addAll(Arrays.stream(input.nextLine().split(" "))
                    .map(Integer::parseInt) // map to integers
                    .map(i -> userList.get(i - 1)) // map to Users in list
                    .collect(Collectors.toList())); 

        return selectedUsers;
    }

    /*
     * EFFECTS: prints list of users
     */
    public <U extends User> void printUserList(ArrayList<U> userList) {
        System.out.println();
        System.out.println("Users:");

        for (int i = 0; i < userList.size(); i++) {
            System.out.println("(" + String.valueOf(i + 1) + ")" + "(" + formatUser(userList.get(i)) + ")");
        }

        System.out.println();
    } 

    /*
     * EFFECTS: returns the data in user nicely formatted as a string
     */
    public String formatUser(User user) {
        String role = "";

        if (user instanceof Coach) {
            role = "Coach";
        } else if (user instanceof Athlete) {
            role = "Athlete";
        }

        return "Name: " + user.getName() 
                + " DOB: " + user.getDateOfBirth() + " Role: " + role + " UUID: " + user.getUuid();
    } 

    /*
     * EFFECTS: returns the User the user picks from the list
     */
    public Squad getSquadFromList(ArrayList<Squad> squadList) {
        printSquadList(squadList);

        System.out.println("Type squad number:");

        int selected = Integer.parseInt(input.nextLine());
        Squad squad = squadList.get(selected - 1); 

        return squad;
    }

    /*
     * EFFECTS: prints list of squads
     */
    public void printSquadList(ArrayList<Squad> squadList) {
        System.out.println();
        System.out.println("Squad:");

        for (int i = 0; i < squadList.size(); i++) {
            System.out.println("(" + String.valueOf(i + 1) + ")" + "(" + formatSquad(squadList.get(i)) + ")");
        }

        System.out.println();
    } 

    /*
     * EFFECTS: returns the data in user nicely formatted as a string
     */
    public String formatSquad(Squad squad) {
        List<String> athleteNames = squad.getAthletes().stream().map(User::getName).collect(Collectors.toList());
        List<String> coachNames = squad.getCoaches().stream().map(User::getName).collect(Collectors.toList());


        return "Name: " 
                    + squad.getName() + " Athletes: " 
                    + athleteNames + " Coaches: " + coachNames + " UUID: " + squad.getUuid();
    } 

    /*
     * EFFECTS: prints weekly schedule
     */
    public void printWeeklySchedule(WeeklySchedule schedule) {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day + ":");
            schedule.getSessionTemplatesOnDay(day).forEach(this::printSessionTemplate);
        }

    }

    /*
     * EFFECTS: prints a session
     */
    public void printSession(Session session) {
        System.out.println(session.getStartTime() 
                + "-" + session.getEndTime() + " Description: " + session.getDescription());
    }

    /*
     * EFFECTS: prints a session template
     */
    public void printSessionTemplate(SessionTemplate template) {
        System.out.println(template.getStartTime() 
                + "-" + template.getEndTime() + " Description: " + template.getDescription());
    }

    /*
     * EFFECTS: returns session templae from user selection
     */
    public SessionTemplate selectSessionTemplateFromDate(WeeklySchedule schedule, LocalDate date) {
        ArrayList<SessionTemplate> sessions = schedule.getSessionTemplatesOnDay(date.getDayOfWeek());

        for (int i = 0; i < sessions.size(); i++) {
            System.out.print("(" + String.valueOf(i + 1) + ")");
            printSessionTemplate(sessions.get(i));
        }

        System.out.println("Type session number:");

        int selected = Integer.parseInt(input.nextLine());
        SessionTemplate template = sessions.get(selected - 1); 

        return template;
    }
}
