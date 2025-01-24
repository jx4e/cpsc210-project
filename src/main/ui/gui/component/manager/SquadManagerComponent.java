package ui.gui.component.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.DataManager;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;
import persistence.FileManager;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.gui.component.StackableComponent;
import ui.gui.component.element.ObjectSelectionPanel;
import ui.gui.component.element.PromptTextField;
import ui.gui.util.ResizeHelper;
import ui.gui.util.SetStack;
import ui.gui.util.Theme;

// Represents a jcomponent to do user management functions in the GUI
public class SquadManagerComponent extends JPanel implements ResizeHelper, StackableComponent {
    private DataManager dataManager;
    private FileManager fileManager;
    private JsonReader reader;
    private JsonWriter writer;

    private Theme theme;
    private JFrame frame;
    private JPanel menu;
    private JScrollPane createSquadPane;
    private JPanel manageSquadPanel;
    private JPanel saveSquadPanel;
    private JPanel loadSquadPanel;


    private Stack<JComponent> componentStack;

    /*
     * EFFECTS: create new UserManagerComponent
     */
    public SquadManagerComponent(JFrame frame, DataManager dataManager, Theme theme) {
        this.frame = frame;
        this.dataManager = dataManager;
        this.theme = theme;

        fileManager = new FileManager();
        writer = new JsonWriter(fileManager);
        reader = new JsonReader(fileManager);

        componentStack = new SetStack<>();

        init();
    }

    /*
     * MODIFIES: this
     * EFFECTSL initialise the panels
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(theme.getBackground());
        
        initMenu();
        initManageSquadPanel();

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
        JPanel inner = new JPanel(new GridLayout(4, 1));
        inner.setBackground(theme.getBackground());

        JButton createSquadButton = new JButton("Create Squad");
        createSquadButton.setBackground(Color.BLUE);
        createSquadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initCreateSquadPanel();
                pushComponent(createSquadPane);
            }
        });
        inner.add(createSquadButton);

        JButton manageSquadButton = new JButton("Manage Squad");
        manageSquadButton.setBackground(Color.BLUE);
        manageSquadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initManageSquadPanel();
                pushComponent(manageSquadPanel);
            }
        });
        inner.add(manageSquadButton);

        JButton saveSquadButton = new JButton("Save Squad");
        saveSquadButton.setBackground(Color.BLUE);
        saveSquadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initSaveSquadPanel();
                pushComponent(saveSquadPanel);
            }
        });
        inner.add(saveSquadButton);

        JButton loadSquadButton = new JButton("Load Squad");
        loadSquadButton.setBackground(Color.BLUE);
        loadSquadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initLoadSquadPanel();
                pushComponent(loadSquadPanel);
            }
        });
        inner.add(loadSquadButton);

        menu.add(inner);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise create squad panel
     */
    @SuppressWarnings("methodlength")
    private void initCreateSquadPanel() {
        JPanel createSquadPanel = new JPanel(new GridLayout(7, 1));
        createSquadPanel.setBackground(theme.getBackground());

        JLabel titleLabel = new JLabel("Create Squad");
        titleLabel.setForeground(theme.getText());
        Runnable resizeAction = () -> titleLabel.setFont(theme.getFont().deriveFont(12f));
        resizeAction.run();
        onResize(titleLabel, resizeAction);
        createSquadPanel.add(titleLabel);

        PromptTextField nameField = new PromptTextField("Name");
        createSquadPanel.add(nameField);

        JLabel selectAthleteLabel = new JLabel("Select Athletes");
        selectAthleteLabel.setForeground(theme.getText());
        createSquadPanel.add(selectAthleteLabel);

        ObjectSelectionPanel<Athlete> selectAthletes 
                = new ObjectSelectionPanel<>(theme, dataManager.getAthletes(), u -> u.getName());
        createSquadPanel.add(selectAthletes);

        JLabel selectCoachLabel = new JLabel("Select Coaches");
        selectCoachLabel.setForeground(theme.getText());
        createSquadPanel.add(selectCoachLabel);

        ObjectSelectionPanel<Coach> selectCoaches
                 = new ObjectSelectionPanel<>(theme, dataManager.getCoaches(), u -> u.getName());
        createSquadPanel.add(selectCoaches);
        
        JButton createButton = new JButton("Submit");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Squad squad = new Squad(nameField.getText(), selectAthletes.getSelected(), selectCoaches.getSelected());
                dataManager.loadSquad(squad);
                JOptionPane.showMessageDialog(frame, "Created new squad: " + squad.getName());
                popComponent();
            }
        });
        createSquadPanel.add(createButton);

        createSquadPane = new JScrollPane(createSquadPanel);
        createSquadPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise manage squad panel
     */
    private void initManageSquadPanel() {
        manageSquadPanel = new JPanel(new GridLayout(2, 1));
        manageSquadPanel.setBackground(theme.getBackground());

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
        manageSquadPanel.add(squadSelect);
        manageSquadPanel.add(createSquadPane);
    }

    /*
     * EFFECTS: creates jpanel to manage selected squad
     */
    @SuppressWarnings("methodlength")
    private JPanel createManageSquadPanel(Squad squad) {
        JPanel createSquadPanel = new JPanel(new GridLayout(7, 1));
        createSquadPanel.setBackground(theme.getBackground());

        JLabel titleLabel = new JLabel("Manage Squad");
        titleLabel.setForeground(theme.getText());
        Runnable resizeAction = () -> titleLabel.setFont(theme.getFont().deriveFont(12f));
        resizeAction.run();
        onResize(titleLabel, resizeAction);
        createSquadPanel.add(titleLabel);

        PromptTextField nameField = new PromptTextField(squad.getName());
        createSquadPanel.add(nameField);

        JLabel selectAthleteLabel = new JLabel("Select Athletes");
        selectAthleteLabel.setForeground(theme.getText());
        createSquadPanel.add(selectAthleteLabel);

        ObjectSelectionPanel<Athlete> selectAthletes 
                = new ObjectSelectionPanel<>(theme, dataManager.getAthletes(), squad.getAthletes(), u -> u.getName());
        createSquadPanel.add(selectAthletes);

        JLabel selectCoachLabel = new JLabel("Select Coaches");
        selectCoachLabel.setForeground(theme.getText());
        createSquadPanel.add(selectCoachLabel);

        ObjectSelectionPanel<Coach> selectCoaches
                 = new ObjectSelectionPanel<>(theme, dataManager.getCoaches(), squad.getCoaches(), u -> u.getName());
        createSquadPanel.add(selectCoaches);
        
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                squad.setName(nameField.getText());
                squad.setAthletes(selectAthletes.getSelected());
                squad.setCoaches(selectCoaches.getSelected());
                JOptionPane.showMessageDialog(frame, "Updated squad: " + squad.getName());
                popComponent();
            }
        });
        createSquadPanel.add(updateButton);

        return createSquadPanel;
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise save squad panel
     */
    @SuppressWarnings("methodlength")
    private void initSaveSquadPanel() {
        saveSquadPanel = new JPanel(new GridLayout(2, 1));
        saveSquadPanel.setBackground(theme.getBackground());

        Object[] squads = dataManager.getSquads().toArray();
        JComboBox<Object> squadSelect = new JComboBox<>(squads);
        saveSquadPanel.add(squadSelect);

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (squadSelect.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "No squad selected", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "Confirm?",
                         "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                Squad squad = ((Squad) squadSelect.getSelectedItem());

                try {
                    writer.write(squad, squad.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Unable to save squad!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame,
                         "Saved squad: " + squad.getName());

                popComponent();
            }
        });
        saveSquadPanel.add(save);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise save squad panel
     */
    @SuppressWarnings("methodlength")
    private void initLoadSquadPanel() {
        loadSquadPanel = new JPanel(new GridLayout(2, 1));
        loadSquadPanel.setBackground(theme.getBackground());

        JFileChooser fileChooser = new JFileChooser(fileManager.getSquadDir());
        loadSquadPanel.add(fileChooser);

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(frame, "No file selected", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "Confirm?",
                         "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                try {
                    Squad squad = reader.deserializeSquad(fileChooser.getSelectedFile());
                    dataManager.loadSquad(squad);
                    JOptionPane.showMessageDialog(frame, "Loaded squad: " + squad.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Unable to load squad!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                popComponent();
            }
        });
        loadSquadPanel.add(save);
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
