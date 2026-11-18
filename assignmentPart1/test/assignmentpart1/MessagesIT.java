package assignmentpart1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessagesIT {

    private static final String VALID_NUMBER = "+27838968976";
    private static final String VALID_PASSWORD = "Ch&&sec@ke99!";

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private Scanner originalInput;

    @Before
    public void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        originalInput = (Scanner) getStatic("input");
        clearList("sentMessages");
        clearList("disregardedMessages");
        clearList("storedMessages");
        setStatic("totalMessages", 0);
        setStatic("messageCounter", 0);
        new File("messages.json").delete();
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        setStatic("input", originalInput);
        new File("messages.json").delete();
    }

    // ---------- reflection helpers ----------
    private static Field field(String name) throws Exception {
        Field f = Messages.class.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    private static Object getStatic(String name) throws Exception {
        return field(name).get(null);
    }

    private static void setStatic(String name, Object value) throws Exception {
        field(name).set(null, value);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Messages> list(String name) throws Exception {
        return (ArrayList<Messages>) getStatic(name);
    }

    private static void clearList(String name) throws Exception {
        list(name).clear();
    }

    private String output() {
        return outContent.toString();
    }

    // ---------- main ----------
    @Test(timeout = 5000)
    public void testMain_fullFlow() throws Exception {
        String script = String.join("\n",
                "Kyle", "Smith",
                "kyl_1", VALID_NUMBER, VALID_PASSWORD,
                "kyl_1", VALID_PASSWORD,
                "1",                    // number of messages
                "1",                    // menu: send
                VALID_NUMBER, "Hi Mike how are you", "1",
                "3"                     // quit
        ) + "\n";
        setStatic("input", new Scanner(script));

        Messages.main(new String[0]);

        String out = output();
        assertTrue(out.contains("Registration successful!"));
        assertTrue(out.contains("Welcome Kyle Smith"));
        assertTrue(out.contains("it is great to see you again."));
        assertTrue(out.contains("Message successfully sent."));
        assertTrue(out.contains("Total messages sent: 1"));
        assertTrue(out.contains("Thank you for using QuickChat."));
        assertEquals(1, list("sentMessages").size());
    }

    @Test(timeout = 5000)
    public void testMain_invalidInputsAreRetried() throws Exception {
        String script = String.join("\n",
                "Kyle", "Smith",
                "toolongname", "kyl_1",
                "0838968976", VALID_NUMBER,
                "weak", VALID_PASSWORD,
                "kyl_1", "wrongPass1!",         // failed login
                "kyl_1", VALID_PASSWORD,        // successful login
                "1",
                "9",                            // invalid option
                "abc",                          // non-numeric option
                "3"
        ) + "\n";
        setStatic("input", new Scanner(script));

        Messages.main(new String[0]);

        String out = output();
        assertTrue(out.contains("Username is not correctly formatted."));
        assertTrue(out.contains("Cellphone number is not correctly formatted."));
        assertTrue(out.contains("Password is not correctly formatted."));
        assertTrue(out.contains("Username or password incorrect, please try again."));
        assertTrue(out.contains("Please try logging in again."));
        assertTrue(out.contains("Invalid option. Please try again."));
        assertTrue(out.contains("Please enter a valid number."));
        assertTrue(out.contains("Total messages sent: 0"));
    }

    @Test(timeout = 5000)
    public void testMain_comingSoonAndMultipleMessages() throws Exception {
        String script = String.join("\n",
                "Kyle", "Smith",
                "kyl_1", VALID_NUMBER, VALID_PASSWORD,
                "kyl_1", VALID_PASSWORD,
                "2",                                        // two messages
                "2",                                        // option 2: coming soon
                "1",                                        // option 1: send
                VALID_NUMBER, "First message here", "1",
                VALID_NUMBER, "Second message here", "1",
                "3"
        ) + "\n";
        setStatic("input", new Scanner(script));

        Messages.main(new String[0]);

        String out = output();
        assertTrue(out.contains("Coming Soon."));
        assertTrue(out.contains("Total messages sent: 2"));
        assertEquals(2, list("sentMessages").size());
    }

    // ---------- readPositiveNumber ----------
    @Test(timeout = 5000)
    public void testReadPositiveNumber_validNumber() throws Exception {
        setStatic("input", new Scanner("5\n"));
        assertEquals(5, Messages.readPositiveNumber());
    }

    @Test(timeout = 5000)
    public void testReadPositiveNumber_rejectsZeroNegativeAndText() throws Exception {
        setStatic("input", new Scanner("abc\n-5\n0\n7\n"));
        assertEquals(7, Messages.readPositiveNumber());
        assertTrue(output().contains("Enter a valid whole number"));
        assertTrue(output().contains("Enter a number greater than zero"));
    }

    // ---------- generateMessageID ----------
    @Test
    public void testGenerateMessageID_isTenDigits() {
        for (int i = 0; i < 100; i++) {
            String id = Messages.generateMessageID();
            assertTrue("ID should be 10 digits: " + id, id.matches("[1-9][0-9]{9}"));
        }
    }

    // ---------- isValidRecipient ----------
    @Test
    public void testIsValidRecipient_validNumbers() {
        assertTrue(Messages.isValidRecipient(VALID_NUMBER));
        assertTrue(Messages.isValidRecipient("+14155552671"));
        assertTrue(Messages.isValidRecipient("+4420712345"));
    }

    @Test
    public void testIsValidRecipient_invalidNumbers() {
        assertFalse(Messages.isValidRecipient(""));
        assertFalse(Messages.isValidRecipient("0838968976"));
        assertFalse(Messages.isValidRecipient("27838968976"));
        assertFalse(Messages.isValidRecipient("+0838968976"));
        assertFalse(Messages.isValidRecipient("+2783"));
        assertFalse(Messages.isValidRecipient("+2783896897612345"));
        assertFalse(Messages.isValidRecipient("+27abc8968976"));
        assertFalse(Messages.isValidRecipient("+27 83 896 8976"));
    }

    // ---------- generateMessageHash ----------
    @Test
    public void testGenerateMessageHash_multiWordMessage() {
        assertEquals("00:0:HITONIGHT",
                Messages.generateMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight"));
        assertEquals("12:1:HELLOWORLD",
                Messages.generateMessageHash("1234567890", 1, "Hello world"));
    }

    @Test
    public void testGenerateMessageHash_singleWord() {
        assertEquals("12:1:HELLOHELLO",
                Messages.generateMessageHash("1234567890", 1, "Hello"));
    }

    @Test
    public void testGenerateMessageHash_extraWhitespaceIsTrimmed() {
        assertEquals("99:3:HITHERE",
                Messages.generateMessageHash("9987654321", 3, "   Hi    there   "));
    }

    // ---------- escapeJSON ----------
    @Test
    public void testEscapeJSON() {
        assertEquals("plain text", Messages.escapeJSON("plain text"));
        assertEquals("say \\\"hi\\\"", Messages.escapeJSON("say \"hi\""));
        assertEquals("C:\\\\temp", Messages.escapeJSON("C:\\temp"));
        assertEquals("line1\\nline2", Messages.escapeJSON("line1\nline2"));
        assertEquals("a\\tb\\rc", Messages.escapeJSON("a\tb\rc"));
        assertEquals("", Messages.escapeJSON(""));
    }

    // ---------- sendMessage ----------
    @Test
    public void testSendMessage_sendAction() throws Exception {
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\nHi Mike how are you\n1\n"));

        assertEquals(1, list("sentMessages").size());
        assertTrue(list("disregardedMessages").isEmpty());
        assertTrue(list("storedMessages").isEmpty());
        assertTrue(output().contains("Message successfully sent."));
        assertTrue(output().contains("Recipient: " + VALID_NUMBER));
        assertTrue(output().contains("HIYOU"));
    }

    @Test
    public void testSendMessage_disregardAction() throws Exception {
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\nForget this message\n0\n"));

        assertEquals(1, list("disregardedMessages").size());
        assertTrue(list("sentMessages").isEmpty());
        assertTrue(output().contains("Message disregarded successfully."));
    }

    @Test
    public void testSendMessage_storeAction() throws Exception {
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\nSend this later please\n2\n"));

        assertEquals(1, list("storedMessages").size());
        assertTrue(output().contains("Message successfully stored."));
        assertTrue(new File("messages.json").exists());
    }

    @Test
    public void testSendMessage_invalidRecipient() throws Exception {
        Messages.sendMessage(new Scanner("0838968976\n"));

        assertTrue(output().contains("Invalid recipient cellphone number."));
        assertTrue(list("sentMessages").isEmpty());
    }

    @Test
    public void testSendMessage_emptyMessage() throws Exception {
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\n   \n"));

        assertTrue(output().contains("Please enter a message of less than 250 characters."));
        assertTrue(list("sentMessages").isEmpty());
    }

    @Test
    public void testSendMessage_messageTooLong() throws Exception {
        String longMessage = new String(new char[250]).replace('\0', 'a');
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\n" + longMessage + "\n"));

        assertTrue(output().contains("Please enter a message of less than 250 characters."));
        assertTrue(list("sentMessages").isEmpty());
    }

    @Test
    public void testSendMessage_invalidAction() throws Exception {
        Messages.sendMessage(new Scanner(VALID_NUMBER + "\nHello there\n9\n"));

        assertTrue(output().contains("Invalid action. Message not sent."));
        assertTrue(list("sentMessages").isEmpty());
        assertTrue(list("disregardedMessages").isEmpty());
        assertTrue(list("storedMessages").isEmpty());
    }

    // ---------- displayMessage ----------
    @Test
    public void testDisplayMessage() {
        Messages msg = new Messages("1234567890", 1, VALID_NUMBER,
                "Hello world", "12:1:HELLOWORLD", "Sent");
        Messages.displayMessage(msg);

        String out = output();
        assertTrue(out.contains("Message ID: 1234567890"));
        assertTrue(out.contains("Message Hash: 12:1:HELLOWORLD"));
        assertTrue(out.contains("Recipient: " + VALID_NUMBER));
        assertTrue(out.contains("Message: Hello world"));
    }

    // ---------- storeMessage ----------
    @Test
    public void testStoreMessage_writesJson() throws Exception {
        Messages msg = new Messages("1234567890", 1, VALID_NUMBER,
                "He said \"hi\"", "12:1:HEHI\"", "Stored");
        Messages.storeMessage(msg);

        String json = new String(Files.readAllBytes(new File("messages.json").toPath()));
        assertTrue(json.contains("\"messageID\": \"1234567890\""));
        assertTrue(json.contains("\"messageNumber\": 1"));
        assertTrue(json.contains("\"recipient\": \"" + VALID_NUMBER + "\""));
        assertTrue(json.contains("\"message\": \"He said \\\"hi\\\"\""));
        assertTrue(json.contains("\"status\": \"Stored\""));
        assertTrue(output().contains("Message successfully stored."));
    }

    @Test
    public void testStoreMessage_appendsMultipleMessages() throws Exception {
        Messages.storeMessage(new Messages("1111111111", 1, VALID_NUMBER, "First", "11:1:FIRSTFIRST", "Stored"));
        Messages.storeMessage(new Messages("2222222222", 2, VALID_NUMBER, "Second", "22:2:SECONDSECOND", "Stored"));

        String json = new String(Files.readAllBytes(new File("messages.json").toPath()));
        assertTrue(json.contains("1111111111"));
        assertTrue(json.contains("2222222222"));
    }

    @Test
    public void testStoreMessage_fileErrorIsHandled() {
        // A folder named messages.json makes FileWriter throw an IOException
        File blocker = new File("messages.json");
        blocker.mkdir();

        Messages.storeMessage(new Messages("1234567890", 1, VALID_NUMBER,
                "Hello", "12:1:HELLOHELLO", "Stored"));

        assertTrue(output().contains("Error storing message:"));
        blocker.delete();
    }
}