/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package assignmentpart1;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreMessagesIT {

    private static final String LONG_MSG =
            "Where are you? You are late! I have asked you to be on time.";

    // ---------- helpers (replace the missing StoreMessages methods) ----------

    private static void setInput(Scanner scanner) {
        try {
            Field f = StoreMessages.class.getDeclaredField("input");
            f.setAccessible(true);
            f.set(null, scanner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void clearAll() {
        StoreMessages.sentMessages.clear();
        StoreMessages.disregardedMessages.clear();
        StoreMessages.storedMessages.clear();
        StoreMessages.messageHashes.clear();
        StoreMessages.messageIDs.clear();
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream testOut = new PrintStream(output);
        try {
            System.setOut(testOut);
            action.run();
            testOut.flush();
        } finally {
            System.setOut(originalOut);
            testOut.close();
        }
        return output.toString();
    }

    private String captureOutputWithInput(String userInput, Runnable action) {
        setInput(new Scanner(userInput));
        return captureOutput(action);
    }

    // ---------- setup / teardown ----------

    @Before
    public void setUp() {
        clearAll();

        StoreMessages.addMessage("0000000001", "01:1:DIDCAKE", "Developer",
                "+27834557896", "Did you get the cake?", "Stored");
        StoreMessages.addMessage("0000000002", "02:2:WHEREONTIME", "Developer",
                "+27838884567", LONG_MSG, "Stored");
        StoreMessages.addMessage("0000000003", "03:3:YOHATGATE", "Developer",
                "+27834484567", "Yohhooo, I am at your gate.", "Stored");
        StoreMessages.addMessage("0838884567", "08:4:ITTIME", "Developer",
                "0838884567", "It is dinner time !", "Stored");
        StoreMessages.addMessage("0000000005", "05:5:OKWITHOUTYOU", "Developer",
                "+27838884567", "Ok, I am leaving without you.", "Stored");
    }

    @After
    public void tearDown() {
        clearAll();
        setInput(new Scanner(System.in));
    }

    // ---------- tests ----------

    @Test
    public void testMessagesArrayCorrectlyPopulated() {
        assertEquals(5, StoreMessages.storedMessages.size());
        assertEquals(5, StoreMessages.messageIDs.size());
        assertEquals(5, StoreMessages.messageHashes.size());

        String output = captureOutput(StoreMessages::displaySendersAndRecipients);

        assertTrue(output.contains("Sender: Developer"));
        assertTrue(output.contains("Recipient: +27834557896"));
        assertTrue(output.contains("Recipient: +27838884567"));
        assertTrue(output.contains("Recipient: 0838884567"));
    }

    @Test
    public void testDisplayLongestMessage() {
        String output = captureOutput(StoreMessages::displayLongestMessage);

        assertTrue(output.contains("Longest Stored Message:"));
        assertTrue(output.contains(LONG_MSG));
    }

    @Test
    public void testSearchByMessageID() {
        String output = captureOutputWithInput("0838884567",
                StoreMessages::searchByMessageID);

        assertTrue(output.contains("Recipient: 0838884567"));
        assertTrue(output.contains("Message: It is dinner time !"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        String output = captureOutputWithInput("9999999999",
                StoreMessages::searchByMessageID);

        assertTrue(output.contains("Message ID not found."));
    }

    @Test
    public void testSearchAllMessagesByRecipient() {
        String output = captureOutputWithInput("+27838884567",
                StoreMessages::searchByRecipient);

        assertTrue(output.contains(LONG_MSG));
        assertTrue(output.contains("Ok, I am leaving without you."));
        assertTrue(output.contains("0000000002"));
        assertTrue(output.contains("0000000005"));
        assertFalse(output.contains("Did you get the cake?"));
    }

    @Test
    public void testSearchByRecipientNotFound() {
        String output = captureOutputWithInput("+27000000000",
                StoreMessages::searchByRecipient);

        assertTrue(output.contains("No messages found for this recipient."));
    }

    @Test
    public void testDeleteMessageUsingMessageHash() {
        String output = captureOutputWithInput("02:2:WHEREONTIME",
                StoreMessages::deleteMessageByHash);

        assertTrue(output.contains("Message found!"));
        assertTrue(output.contains(LONG_MSG));
        assertTrue(output.contains("Message successfully deleted!"));

        assertEquals(4, StoreMessages.storedMessages.size());
        assertFalse(StoreMessages.messageHashes.contains("02:2:WHEREONTIME"));
        assertFalse(StoreMessages.messageIDs.contains("0000000002"));

        String report = captureOutput(StoreMessages::displayFullMessageReport);
        assertFalse(report.contains(LONG_MSG));
        assertTrue(report.contains("Total Stored Messages: 4"));
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        String output = captureOutputWithInput("NOPE",
                StoreMessages::deleteMessageByHash);

        assertTrue(output.contains("No message found with that hash."));
        assertEquals(5, StoreMessages.storedMessages.size());
    }

    @Test
    public void testDisplayReport() {
        String output = captureOutput(StoreMessages::displayFullMessageReport);

        assertTrue(output.contains("FULL STORED MESSAGE REPORT"));

        assertTrue(output.contains("01:1:DIDCAKE"));
        assertTrue(output.contains("02:2:WHEREONTIME"));
        assertTrue(output.contains("03:3:YOHATGATE"));

        assertTrue(output.contains("+27834557896"));
        assertTrue(output.contains("+27838884567"));
        assertTrue(output.contains("+27834484567"));

        assertTrue(output.contains("Did you get the cake?"));
        assertTrue(output.contains(LONG_MSG));
        assertTrue(output.contains("Yohhooo, I am at your gate."));
        assertTrue(output.contains("It is dinner time !"));
        assertTrue(output.contains("Ok, I am leaving without you."));

        assertTrue(output.contains("Message Status: Stored"));
        assertTrue(output.contains("Total Stored Messages: 5"));
    }

    @Test
    public void testAddMessageRoutesByStatus() {
        StoreMessages.addMessage("A1", "H1", "Developer", "+27111111111", "Sent msg", "Sent");
        StoreMessages.addMessage("A2", "H2", "Developer", "+27111111111", "Dropped msg", "Disregard");

        assertEquals(1, StoreMessages.sentMessages.size());
        assertEquals(1, StoreMessages.disregardedMessages.size());
        assertEquals(5, StoreMessages.storedMessages.size());
    }

    @Test
    public void testEmptyStoreMessages() {
        clearAll();

        assertTrue(captureOutput(StoreMessages::displayLongestMessage)
                .contains("No stored messages available."));
        assertTrue(captureOutput(StoreMessages::displayFullMessageReport)
                .contains("There are no stored messages available."));
    }
}