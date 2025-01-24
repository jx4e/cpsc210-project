package ui.gui.component.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.DataManager;
import model.session.SessionTemplate;
import model.user.Athlete;
import model.user.Squad;
import ui.gui.component.StackableComponent;
import ui.gui.component.element.DateTextField;
import ui.gui.component.element.ObjectSelectionPanel;
import ui.gui.util.ResizeHelper;
import ui.gui.util.SetStack;
import ui.gui.util.Theme;

// Represents a jcomponent to do session management functions in the GUI
public class AttendanceManagerComponent extends JPanel implements ResizeHelper, StackableComponent {
    private DataManager dataManager;

    private Theme theme;
    private JFrame frame;
    private JPanel menu;
    private JPanel recordAttendancePanel;
    private JPanel viewSquadAttendancePanel;
    private JPanel viewAthleteAttendancePanel;

    private Stack<JComponent> componentStack;

    /*
     * EFFECTS: create new UserManagerComponent
     */
    public AttendanceManagerComponent(JFrame frame, DataManager dataManager, Theme theme) {
        this.frame = frame;
        this.dataManager = dataManager;
        this.theme = theme;

        componentStack = new SetStack<>();

        init();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialise the panels
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(theme.getBackground());
        
        initMenu();

        pushComponent(menu);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the menu
     */
    @SuppressWarnings("methodlength")
    private void initMenu() {
        menu = new JPanel(new BorderLayout());
        menu.setBorder(new EmptyBorder(10, 10, 10, 10));
        menu.setBackground(theme.getBackground());
        JPanel inner = new JPanel(new GridLayout(3, 1));
        inner.setBackground(theme.getBackground());

        JButton recordAttendance = new JButton("Record Attendance");
        recordAttendance.setBackground(Color.BLUE);
        recordAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initRecordAttendancePanel();
                pushComponent(recordAttendancePanel);
            }
        });
        inner.add(recordAttendance);

        JButton squadAttendance = new JButton("Calculate Squad Attendance");
        squadAttendance.setBackground(Color.BLUE);
        squadAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initViewSquadAttendancePanel();
                pushComponent(viewSquadAttendancePanel);
            }
        });
        inner.add(squadAttendance);

        JButton athleteAttendance = new JButton("Calculate Athlete Attendance");
        athleteAttendance.setBackground(Color.BLUE);
        athleteAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initViewAthleteAttendancePanel();
                pushComponent(viewAthleteAttendancePanel);
            }
        });
        inner.add(athleteAttendance);

        menu.add(inner);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise record attendance panel
     */
    private void initRecordAttendancePanel() {
        recordAttendancePanel = new JPanel(new GridLayout(3, 1));
        recordAttendancePanel.setBackground(theme.getBackground());

        JScrollPane createSquadPane = new JScrollPane();
        createSquadPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        DateTextField date = new DateTextField("Date");
        recordAttendancePanel.add(date);

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        squadSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createRecordAttendancePanel((Squad) squadSelect.getSelectedItem(), date.getDate());
                createSquadPane.setViewportView(panel);
                createSquadPane.repaint();
            }
        });
        recordAttendancePanel.add(squadSelect);


        recordAttendancePanel.add(createSquadPane);
    }

    /*
     * EFFECTS: creates jpanel to view selected squad schedule
     */
    @SuppressWarnings("methodlength")
    private JPanel createRecordAttendancePanel(Squad squad, LocalDate date) {
        JPanel recordAttendancePanel = new JPanel(new GridLayout(3, 1));
        recordAttendancePanel.setBackground(theme.getBackground());

        Object[] sessions = squad.getWeeklySchedule().getSessionTemplatesOnDay(date.getDayOfWeek()).toArray();
        JComboBox<Object> sessionSelect = new JComboBox<>(sessions);
        recordAttendancePanel.add(sessionSelect);

        JScrollPane createSquadPane = new JScrollPane();

        ObjectSelectionPanel<Athlete> athletes =
                new ObjectSelectionPanel<>(theme, squad.getAthletes(), a -> a.getName());
        createSquadPane.setViewportView(athletes);

        recordAttendancePanel.add(createSquadPane);

        JButton submit = new JButton("Submit Attendance");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sessionSelect.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "No session selected", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION 
                        == JOptionPane.showConfirmDialog(frame, "Confirm?",
                                 "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                SessionTemplate template = ((SessionTemplate) sessionSelect.getSelectedItem());

                squad.recordAttendance(date, template, athletes.getSelected());


                JOptionPane.showMessageDialog(frame, "Recorded attendance for: " + template.getDescription());

                popComponent();
            }
        });
        recordAttendancePanel.add(submit);



        return recordAttendancePanel;
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise record attendance panel
     */
    private void initViewSquadAttendancePanel() {
        viewSquadAttendancePanel = new JPanel(new GridLayout(2, 1));
        viewSquadAttendancePanel.setBackground(theme.getBackground());

        JScrollPane createSquadPane = new JScrollPane();
        createSquadPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        squadSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createViewSquadAttendancePanel((Squad) squadSelect.getSelectedItem());
                createSquadPane.setViewportView(panel);
                createSquadPane.repaint();
            }
        });
        viewSquadAttendancePanel.add(squadSelect);


        viewSquadAttendancePanel.add(createSquadPane);
    }

    /*
     * EFFECTS: creates jpanel to view selected squad schedule
     */
    @SuppressWarnings("methodlength")
    private JPanel createViewSquadAttendancePanel(Squad squad) {
        JPanel recordAttendancePanel = new JPanel(new GridLayout(4, 1));
        recordAttendancePanel.setBackground(theme.getBackground());

        DateTextField from = new DateTextField("From");
        recordAttendancePanel.add(from);

        DateTextField to = new DateTextField("To");
        recordAttendancePanel.add(to);

        JButton submit = new JButton("Calculate Attendance");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!from.isValidDate()) {
                    JOptionPane.showMessageDialog(frame, "Invalid from date", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!from.isValidDate()) {
                    JOptionPane.showMessageDialog(frame, "Invalid to date", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION 
                        == JOptionPane.showConfirmDialog(frame, "Confirm?",
                                 "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                float attendance = squad.calculateAttendance(from.getDate(), to.getDate());

                JOptionPane.showMessageDialog(frame, "Attendance is: " + attendance);

                popComponent();
            }
        });
        recordAttendancePanel.add(submit);



        return recordAttendancePanel;
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise record attendance panel
     */
    private void initViewAthleteAttendancePanel() {
        viewAthleteAttendancePanel = new JPanel(new GridLayout(2, 1));
        viewAthleteAttendancePanel.setBackground(theme.getBackground());

        JScrollPane createSquadPane = new JScrollPane();
        createSquadPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        squadSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createViewAthleteAttendancePanel((Squad) squadSelect.getSelectedItem());
                createSquadPane.setViewportView(panel);
                createSquadPane.repaint();
            }
        });
        viewAthleteAttendancePanel.add(squadSelect);


        viewAthleteAttendancePanel.add(createSquadPane);
    }

    /*
     * EFFECTS: creates jpanel to view selected squad schedule
     */
    @SuppressWarnings("methodlength")
    private JPanel createViewAthleteAttendancePanel(Squad squad) {
        JPanel recordAttendancePanel = new JPanel(new GridLayout(5, 1));
        recordAttendancePanel.setBackground(theme.getBackground());

        Object[] athletes = squad.getAthletes().toArray();
        JComboBox<Object> athleteSelect = new JComboBox<>(athletes);
        recordAttendancePanel.add(athleteSelect);

        DateTextField from = new DateTextField("From");
        recordAttendancePanel.add(from);

        DateTextField to = new DateTextField("To");
        recordAttendancePanel.add(to);

        JButton submit = new JButton("Calculate Attendance");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (athleteSelect.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Invalid user selection",
                             "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!from.isValidDate()) {
                    JOptionPane.showMessageDialog(frame, "Invalid from date", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!from.isValidDate()) {
                    JOptionPane.showMessageDialog(frame, "Invalid to date", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "Confirm?", "Confirm",
                        JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                Athlete athlete = (Athlete) athleteSelect.getSelectedItem();

                float attendance = squad.calculateAttendanceForAthlete(athlete, from.getDate(), to.getDate());

                JOptionPane.showMessageDialog(frame, "Attendance is: " + attendance);

                popComponent();
            }
        });
        recordAttendancePanel.add(submit);

        return recordAttendancePanel;
    }

    @Override
    public int getFrameWidth() {
        return frame.getWidth();
    }

    @Override
    public int getFrameHeight() {
        return frame.getHeight();
    }

    @Override
    public Stack<JComponent> componentStack() {
        return componentStack;
    }

    @Override
    public void removeFromParent(JComponent component) {
        remove(component);
    }

    @Override
    public void addToParent(JComponent component) {
        add(component);
    }

    @Override
    public void repaintParent() {
        repaint();
    }

    @Override
    public void revalidateParent() {
        revalidate();
    }
}
