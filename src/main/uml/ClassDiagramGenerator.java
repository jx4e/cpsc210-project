package uml;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

import model.DataManager;
import model.event.*;
import model.session.Session;
import model.session.SessionTemplate;
import model.session.WeeklySchedule;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;
import model.user.User;
import persistence.FileManager;
import persistence.JsonReader;
import persistence.JsonSerializable;
import persistence.JsonWriter;
import ui.Main;
import ui.UIMode;
import ui.cli.SwimTrackerCLI;
import ui.gui.SwimTrackerGUI;
import ui.gui.component.StackableComponent;
import ui.gui.component.element.DateTextField;
import ui.gui.component.element.ObjectSelectionPanel;
import ui.gui.component.element.PromptTextField;
import ui.gui.component.element.TimeTextField;
import ui.gui.component.element.UserPanel;
import ui.gui.component.element.WeeklySchedulePanel;
import ui.gui.component.manager.AttendanceManagerComponent;
import ui.gui.component.manager.SessionManagerComponent;
import ui.gui.component.manager.SquadManagerComponent;
import ui.gui.component.manager.UserManagerComponent;
import ui.gui.util.ResizeHelper;
import ui.gui.util.SetStack;
import ui.gui.util.Theme;


// Generates UML boxes for draw.io cus i am too lazy to do it manually.
public class ClassDiagramGenerator {
    @SuppressWarnings("methodlength")
    public static void main(String[] args) {
        ClassDiagram table = new ClassDiagram(Arrays.asList(
                // model
                DataManager.class,

                // event
                Event.class,
                EventLog.class,

                // session
                Session.class,
                SessionTemplate.class,
                WeeklySchedule.class,

                // user
                Athlete.class,
                Coach.class,
                Squad.class,
                User.class,

                // persistence
                FileManager.class,
                JsonReader.class,
                JsonSerializable.class,
                JsonWriter.class,

                // ui
                Main.class,
                UIMode.class,

                // cli
                SwimTrackerCLI.class,

                // gui
                SwimTrackerGUI.class,

                // component 
                StackableComponent.class,

                // element
                DateTextField.class,
                ObjectSelectionPanel.class,
                PromptTextField.class,
                TimeTextField.class,
                UserPanel.class,
                WeeklySchedulePanel.class,

                // manager
                AttendanceManagerComponent.class,
                SessionManagerComponent.class,
                SquadManagerComponent.class,
                UserManagerComponent.class,

                // util
                ResizeHelper.class,
                SetStack.class,
                Theme.class
        ), false);

        StringSelection stringSelection = new StringSelection(table.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}

