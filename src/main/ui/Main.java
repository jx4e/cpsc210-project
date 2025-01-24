package ui;

import java.time.format.DateTimeFormatter;

import ui.cli.SwimTrackerCLI;
import ui.gui.SwimTrackerGUI;

public class Main {
    public static final String DATE_STRING = "dd/MM/yyyy";
    public static final String TIME_STRING = "HH:mm";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_STRING);
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(TIME_STRING);
    public static final boolean TEST_ENV = true;
    public static final UIMode MODE = UIMode.GUI;

    public static void main(String[] args) throws Exception {
        switch (MODE) {
            case CLI:
                new SwimTrackerCLI();
                break;
        
            case GUI:
                new SwimTrackerGUI();
                break;
        }
    }
}
