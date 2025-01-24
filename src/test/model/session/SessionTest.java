package model.session;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.user.Athlete;

public class SessionTest {
    private Session testSession;
    private Athlete athlete1;
    private Athlete athlete2;
    private Athlete athlete3;
    private ArrayList<Athlete> attending;

    @BeforeEach
    void runBefore() {
        athlete1 = new Athlete("A1", LocalDate.of(2005, 10, 8));
        athlete2 = new Athlete("A2", LocalDate.of(2001, 7, 5));
        athlete3 = new Athlete("A3", LocalDate.of(2000, 4, 18));

        attending = new ArrayList<>(Arrays.asList(athlete1, athlete2, athlete3));

        testSession = new Session(LocalDate.of(2024, 6, 20), attending,
                        LocalTime.of(12, 30), LocalTime.of(14, 00), "A Session");
    }

    @Test
    void testConstructor1() {
        assertEquals(LocalDate.of(2024, 6, 20), testSession.getDate());
        assertEquals(attending, testSession.getAttending());
        assertEquals(LocalTime.of(12, 30), testSession.getStartTime());
        assertEquals(LocalTime.of(14, 00), testSession.getEndTime());
        assertEquals("A Session", testSession.getDescription());
    }

    @Test
    void testConstructor2() {
        testSession = new Session(LocalDate.of(2024, 6, 20),
                        LocalTime.of(12, 30), LocalTime.of(14, 00), "A Session");

        assertEquals(LocalDate.of(2024, 6, 20), testSession.getDate());
        assertTrue(testSession.getAttending().isEmpty());
        assertEquals(LocalTime.of(12, 30), testSession.getStartTime());
        assertEquals(LocalTime.of(14, 00), testSession.getEndTime());
        assertEquals("A Session", testSession.getDescription());
    }

    @Test
    void testRegister() {
        assertEquals(attending, testSession.getAttending());

        Athlete newAthlete1 = new Athlete("New Athlete 1", LocalDate.of(2005, 10, 8));
        Athlete newAthlete2 = new Athlete("New Athlete 2", LocalDate.of(2005, 10, 8));

        testSession.register(newAthlete1);
        testSession.register(newAthlete2);

        assertEquals(5, testSession.getAttending().size());
        assertEquals(newAthlete1, testSession.getAttending().get(3));
        assertEquals(newAthlete2, testSession.getAttending().get(4));
    }

    @Test
    void testIsPresent() {
        assertEquals(attending, testSession.getAttending());

        Athlete newAthlete1 = new Athlete("New Athlete 1", LocalDate.of(2005, 10, 8));

        assertTrue(testSession.isPresent(athlete1));
        assertTrue(testSession.isPresent(athlete2));
        assertFalse(testSession.isPresent(newAthlete1));
    }
}
