package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;

public class TestDataManager {
    private DataManager testDataManager;
    private Squad testSquad1;
    private Squad testSquad2;
    private Athlete athlete1;
    private Athlete athlete2;
    private Athlete athlete3;
    private Coach coach1;
    private Coach coach2;
    private ArrayList<Athlete> athletes;
    private ArrayList<Coach> coaches;
    
    @BeforeEach
    void runBefore() {
        athlete1 = new Athlete("A1", LocalDate.of(2005, 10, 8));
        athlete2 = new Athlete("A2", LocalDate.of(2001, 7, 5));
        athlete3 = new Athlete("A3", LocalDate.of(2000, 4, 18));

        coach1 = new Coach("C1", LocalDate.of(1980, 12, 18));
        coach2 = new Coach("C2", LocalDate.of(1980, 3, 8));

        athletes = new ArrayList<>(Arrays.asList(athlete1, athlete2, athlete3));
        coaches = new ArrayList<>(Arrays.asList(coach1, coach2));

        testSquad1 = new Squad(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                         "UBC Varsity", athletes, coaches);

        testSquad2 = new Squad(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                         "UBC Varsity", athletes, new ArrayList<>(Arrays.asList(coach2)));

        testDataManager = new DataManager();
    }

    @Test
    void testConstructor() {
        assertTrue(testDataManager.getUsers().isEmpty());
        assertTrue(testDataManager.getSquads().isEmpty());
    }

    @Test
    void testLoadUser() {
        assertTrue(testDataManager.getUsers().isEmpty());

        testDataManager.loadUser(athlete1);
        
        assertEquals(1, testDataManager.getUsers().size());
        assertEquals(athlete1, testDataManager.getUsers().get(0));

        testDataManager.loadUser(coach1);

        assertEquals(2, testDataManager.getUsers().size());
        assertEquals(athlete1, testDataManager.getUsers().get(0));
        assertEquals(coach1, testDataManager.getUsers().get(1));

        testDataManager.loadUser(coach2);
        testDataManager.loadUser(athlete2);

        assertEquals(4, testDataManager.getUsers().size());
        assertEquals(athlete1, testDataManager.getUsers().get(0));
        assertEquals(coach1, testDataManager.getUsers().get(1));
        assertEquals(coach2, testDataManager.getUsers().get(2));
        assertEquals(athlete2, testDataManager.getUsers().get(3));
    }

    @Test
    void testLoadSquad() {
        assertTrue(testDataManager.getSquads().isEmpty());

        testDataManager.loadSquad(testSquad1);
        
        assertEquals(1, testDataManager.getSquads().size());
        assertEquals(testSquad1, testDataManager.getSquads().get(0));
    }

    @Test
    void testGetCoaches() {
        assertTrue(testDataManager.getAthletes().isEmpty());
        assertTrue(testDataManager.getCoaches().isEmpty());

        testDataManager.loadUser(athlete1);
        testDataManager.loadUser(coach1);
        testDataManager.loadUser(coach2);
        testDataManager.loadUser(athlete2);

        assertEquals(4, testDataManager.getUsers().size());
        assertEquals(athlete1, testDataManager.getUsers().get(0));
        assertEquals(coach1, testDataManager.getUsers().get(1));
        assertEquals(coach2, testDataManager.getUsers().get(2));
        assertEquals(athlete2, testDataManager.getUsers().get(3));

        List<Coach> coaches = testDataManager.getCoaches();

        assertEquals(2, coaches.size());
        assertEquals(coach1, coaches.get(0));
        assertEquals(coach2, coaches.get(1));
    }

    @Test
    void testGetAthletes() {
        assertTrue(testDataManager.getAthletes().isEmpty());
        assertTrue(testDataManager.getAthletes().isEmpty());

        testDataManager.loadUser(athlete1);
        testDataManager.loadUser(coach1);
        testDataManager.loadUser(coach2);
        testDataManager.loadUser(athlete2);

        assertEquals(4, testDataManager.getUsers().size());
        assertEquals(athlete1, testDataManager.getUsers().get(0));
        assertEquals(coach1, testDataManager.getUsers().get(1));
        assertEquals(coach2, testDataManager.getUsers().get(2));
        assertEquals(athlete2, testDataManager.getUsers().get(3));

        List<Athlete> athletes = testDataManager.getAthletes();

        assertEquals(2, athletes.size());
        assertEquals(athlete1, athletes.get(0));
        assertEquals(athlete2, athletes.get(1));
    }

    @Test
    void testGetSquadsByCoach() {
        testDataManager.loadSquad(testSquad1);
        testDataManager.loadSquad(testSquad2);
        testDataManager.loadSquad(testSquad2);
        // squads is now ts1, ts2, ts2

        ArrayList<Squad> coachedSquad = testDataManager.getSquadsByCoach(coach1);

        assertEquals(new ArrayList<>(Arrays.asList(testSquad1)), coachedSquad);

        coachedSquad = testDataManager.getSquadsByCoach(coach2);

        assertEquals(testDataManager.getSquads(), coachedSquad);

    }
}