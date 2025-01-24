package persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.session.WeeklySchedule;
import model.user.Squad;

public class TestFileManager {
    private FileManager fm;

    @BeforeEach
    void runBefore() {
        fm = new FileManager();
    }

    @Test
    void testConstructor() {
        assertTrue(new File("./data/").isDirectory());
        assertTrue(new File("./data/squads/").isDirectory());
        assertTrue(new File("./data/schedules/").isDirectory());
    }

    @Test
    void testGetSquadNames() {
        try {
            new JsonWriter(fm).write(new Squad("testsquad"), "testsquad");
            assertTrue(fm.getSquadNames().contains("testsquad.json"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testGetScheduleNames() {
        try {
            new JsonWriter(fm).write(new WeeklySchedule(), "testschedule");
            assertTrue(fm.getScheduleNames().contains("testschedule.json"));
        } catch (Exception e) {
            fail();
        }    
    }

    @Test
    void testGetSquad() {
        assertEquals(new File("./data/squads/testsquad.json"), fm.getSquad("testsquad"));
    }

    @Test
    void testGetSchedule() {
        assertEquals(new File("./data/squads/testschedule.json"), fm.getSquad("testschedule"));
    }
}
