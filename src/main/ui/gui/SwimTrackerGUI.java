package ui.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.DataManager;
import model.session.SessionTemplate;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;
import ui.Main;
import ui.gui.component.StackableComponent;
import ui.gui.component.manager.AttendanceManagerComponent;
import ui.gui.component.manager.SessionManagerComponent;
import ui.gui.component.manager.SquadManagerComponent;
import ui.gui.component.manager.UserManagerComponent;
import ui.gui.util.ResizeHelper;
import ui.gui.util.Theme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class SwimTrackerGUI implements ResizeHelper, StackableComponent {
    private static final Dimension DEFAULT_DIMENSION = new Dimension(400, 300);
    private static final Theme DEFAULT_THEME = Theme.TBIRD;

    /*
     * Elements
     */
    private JFrame frame;
    private JPanel header;
    private JComponent home;
    private JComponent userManager;
    private JComponent squadManager;
    private JComponent sessionManager;
    private JComponent attendanceManager;

    private Stack<JComponent> componentStack;

    /*
     * Theme
     */
    private Theme theme;

    /*
     * Data
     */
    private DataManager dataManager;
    private Coach currentUser;

    public SwimTrackerGUI() {
        theme = DEFAULT_THEME;

        dataManager = new DataManager();

        if (Main.TEST_ENV) {
            initTestEnv();
        }

        initGUI();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the environment for testing purposes
     */
    @SuppressWarnings("methodlength")
    public void initTestEnv() {
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
     * EFFECTS: initialises the GUI
     */
    private void initGUI() {
        componentStack = new Stack<>();
        initFrame();
        initHeader();
        initHome();
        initUserManager();
        initSquadManager();
        initSessionManager();
        initAttencanceManager();
        frame.setVisible(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the frame
     */
    private void initFrame() {
        frame = new JFrame("Swim Tracker");
        frame.setSize(DEFAULT_DIMENSION);
        frame.setMinimumSize(DEFAULT_DIMENSION);
        // frame.getContentPane().setBackground(theme.getBackground());
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the header
     */
    @SuppressWarnings("methodlength")
    private void initHeader() {
        header = new JPanel();
        header.setBackground(theme.getSurface());
        header.setLayout(new BorderLayout());
        Runnable resizeAction = createBasicResizeAction(header, width(1), height(8));
        resizeAction.run();
        onResize(header, resizeAction);
        
        
        JLabel titleLabel = new JLabel("Swim Tracker");
        titleLabel.setForeground(theme.getText());
        resizeAction = () -> 
            titleLabel.setFont(theme.getFont().deriveFont((float) header.getPreferredSize().getHeight() / 2f));
        resizeAction.run();
        onResize(titleLabel, resizeAction);
        header.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popComponent();
            }
        });
        resizeAction = createBasicResizeAction(backButton, width(header, 5), height(header, 1));
        resizeAction.run();
        onResize(backButton, resizeAction);
        header.add(backButton, BorderLayout.CENTER);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(home);
            }
        });
        resizeAction = createBasicResizeAction(homeButton, width(header, 5), height(header, 1));
        resizeAction.run();
        onResize(homeButton, resizeAction);
        header.add(homeButton, BorderLayout.EAST);
        
        frame.add(header, BorderLayout.NORTH);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the home panel
     */
    @SuppressWarnings("methodlength")
    private void initHome() {
        home = new JPanel(new BorderLayout());
        home.setBorder(new EmptyBorder(10, 10, 10, 10));
        home.setBackground(theme.getBackground());
        JPanel inner = new JPanel(new GridLayout(4, 1));
        inner.setBackground(theme.getBackground());

        JButton userManagerButton = new JButton("User Manager");
        userManagerButton.setBackground(Color.BLUE);
        userManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(userManager);
            }
        });
        inner.add(userManagerButton);

        JButton squadManagerButton = new JButton("Squad Manager");
        squadManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(squadManager);
            }
        });
        inner.add(squadManagerButton);

        JButton sessionManagerButton = new JButton("Session Manager");
        sessionManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(sessionManager);
            }
        });
        inner.add(sessionManagerButton);

        JButton attendanceManagerButton = new JButton("Attendance Manager");
        attendanceManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(attendanceManager);
            }
        });
        inner.add(attendanceManagerButton);

        home.add(inner);

        pushComponent(home);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  initialises user manager panel
     */
    private void initUserManager() {
        userManager = new UserManagerComponent(frame, dataManager, theme);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  initialises squad manager panel
     */
    private void initSquadManager() {
        squadManager = new SquadManagerComponent(frame, dataManager, DEFAULT_THEME);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  initialises squad manager panel
     */
    private void initSessionManager() {
        sessionManager = new SessionManagerComponent(frame, dataManager, DEFAULT_THEME);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  initialises squad manager panel
     */
    private void initAttencanceManager() {
        attendanceManager = new AttendanceManagerComponent(frame, dataManager, DEFAULT_THEME);
    }

    /*
     * EFFECTS: if there is a stackable component at top popComponent on that
     *          if it fails, pop this
     */
    @Override
    public boolean popComponent() {
        if (componentStack.peek() instanceof StackableComponent) {
            if (((StackableComponent) componentStack.peek()).popComponent()) {
                return true;
            }
        } 

        return StackableComponent.super.popComponent();
    }

    @Override
    public Stack<JComponent> componentStack() {
        return componentStack;
    }

    @Override
    public void removeFromParent(JComponent component) {
        frame.remove(component);
    }

    @Override
    public void addToParent(JComponent component) {
        frame.add(component);
    }

    @Override
    public void repaintParent() {
        frame.repaint();
    }

    @Override
    public void revalidateParent() {
        frame.revalidate();
    }

    public int getFrameWidth() {
        return frame.getWidth();
    }

    public int getFrameHeight() {
        return frame.getHeight();
    }
}
