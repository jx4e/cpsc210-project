package ui.gui.component.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
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
import model.user.Squad;
import ui.gui.component.StackableComponent;
import ui.gui.component.element.PromptTextField;
import ui.gui.component.element.TimeTextField;
import ui.gui.component.element.WeeklySchedulePanel;
import ui.gui.util.ResizeHelper;
import ui.gui.util.SetStack;
import ui.gui.util.Theme;

// Represents a jcomponent to do session management functions in the GUI
public class SessionManagerComponent extends JPanel implements ResizeHelper, StackableComponent {
    private DataManager dataManager;

    private Theme theme;
    private JFrame frame;
    private JPanel menu;
    private JPanel viewSchedulePanel;
    private JPanel createSessionPanel;

    private Stack<JComponent> componentStack;

    /*
     * EFFECTS: create new UserManagerComponent
     */
    public SessionManagerComponent(JFrame frame, DataManager dataManager, Theme theme) {
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
        JPanel inner = new JPanel(new GridLayout(2, 1));
        inner.setBackground(theme.getBackground());

        JButton viewSquad = new JButton("View Squad Schedule");
        viewSquad.setBackground(Color.BLUE);
        viewSquad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initViewSchedulePanel();
                pushComponent(viewSchedulePanel);
            }
        });
        inner.add(viewSquad);

        JButton createSession = new JButton("Create Session");
        createSession.setBackground(Color.BLUE);
        createSession.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initCreateSessionPanel();
                pushComponent(createSessionPanel);
            }
        });
        inner.add(createSession);

        menu.add(inner);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise view schedule panel
     */
    private void initViewSchedulePanel() {
        viewSchedulePanel = new JPanel(new GridLayout(2, 1));
        viewSchedulePanel.setBackground(theme.getBackground());

        JScrollPane createSquadPane = new JScrollPane();
        createSquadPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        squadSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createManageSquadPanel((Squad) squadSelect.getSelectedItem());
                createSquadPane.setViewportView(panel);
                createSquadPane.repaint();
            }
        });
        viewSchedulePanel.add(squadSelect);
        viewSchedulePanel.add(createSquadPane);
    }

    /*
     * EFFECTS: creates jpanel to view selected squad schedule
     */
    private WeeklySchedulePanel createManageSquadPanel(Squad squad) {
        return new WeeklySchedulePanel(theme, squad.getWeeklySchedule());
    }

    /*
     * EFFECTS: creates the create session panel
     */
    @SuppressWarnings("methodlength")
    private void initCreateSessionPanel() {
        createSessionPanel = new JPanel(new GridLayout(6, 1));
        createSessionPanel.setBackground(theme.getBackground());

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        createSessionPanel.add(squadSelect);

        Object[] days = DayOfWeek.values();
        JComboBox<Object> daySelect = new JComboBox<>(days);
        createSessionPanel.add(daySelect);

        TimeTextField startTime = new TimeTextField("Start");
        createSessionPanel.add(startTime);

        TimeTextField endTime = new TimeTextField("End");
        createSessionPanel.add(endTime);

        PromptTextField description = new PromptTextField("Description");
        createSessionPanel.add(description);

        JButton create = new JButton("Create Session");
        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (squadSelect.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "No squad selected", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (daySelect.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "No date selected", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!startTime.isValidTime()) {
                    JOptionPane.showMessageDialog(frame, "Invalid start time", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!endTime.isValidTime()) {
                    JOptionPane.showMessageDialog(frame, "Invalid end time", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "Confirm?",
                         "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                Squad squad = ((Squad) squadSelect.getSelectedItem());
                DayOfWeek day = ((DayOfWeek) daySelect.getSelectedItem());

                SessionTemplate template = new SessionTemplate(startTime.getTime(),
                         endTime.getTime(), description.getText());


                squad.addSessionTemplateToWeeklySchedule(day, template);

                JOptionPane.showMessageDialog(frame,
                         "Created new session: " + template.getDescription() + " in squad: " + squad.getName());

                popComponent();
            }
        });
        createSessionPanel.add(create);
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
