package model.session;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SessionTemplateTest {
    private SessionTemplate sessionTemplateTest;

    @BeforeEach
    void runBefore() {
        sessionTemplateTest = new SessionTemplate(LocalTime.of(13, 00), LocalTime.of(15, 00), "A Session!");
    }

    @Test
    void testConstructor() {
        assertEquals(LocalTime.of(13, 00), sessionTemplateTest.getStartTime());
        assertEquals(LocalTime.of(15, 00), sessionTemplateTest.getEndTime());
        assertEquals("A Session!", sessionTemplateTest.getDescription());
    }

    @Test
    void testEquals() {
        SessionTemplate sessionTemplateTestCopy = 
                new SessionTemplate(LocalTime.of(13, 00), LocalTime.of(15, 00), "A Session!");
        SessionTemplate sessionTemplateTestDiffStart = 
                new SessionTemplate(LocalTime.of(14, 00), LocalTime.of(15, 00), "A Session!");
        SessionTemplate sessionTemplateTestDiffEnd = 
                new SessionTemplate(LocalTime.of(13, 00), LocalTime.of(16, 00), "A Session!");
        SessionTemplate sessionTemplateTestDiffDesc = 
                new SessionTemplate(LocalTime.of(13, 00), LocalTime.of(15, 00), "A Se ssion!");
            
        assertTrue(sessionTemplateTest.equals(sessionTemplateTestCopy));
        assertFalse(sessionTemplateTest.equals(sessionTemplateTestDiffStart));
        assertFalse(sessionTemplateTest.equals(sessionTemplateTestDiffEnd));
        assertFalse(sessionTemplateTest.equals(sessionTemplateTestDiffDesc));
        assertFalse(sessionTemplateTest.equals(new Object()));
    }

    @Test
    void testSerialize() {
        JSONObject serialized = 
                new JSONObject(
                    "{\"start\":\"13:00\",\"description\":\"A Session!\",\"end\":\"15:00\"}");

        assertEquals(serialized.toMap(), sessionTemplateTest.serialize().toMap());
    }

    @Test
    void testDeserialize() {
        SessionTemplate deserialized = SessionTemplate.deserialize(sessionTemplateTest.serialize());

        assertEquals(sessionTemplateTest.getStartTime(), deserialized.getStartTime());
        assertEquals(sessionTemplateTest.getEndTime(), deserialized.getEndTime());
        assertEquals(sessionTemplateTest.getDescription(), deserialized.getDescription());
    }
}
