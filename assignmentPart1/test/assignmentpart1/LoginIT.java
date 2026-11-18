package assignmentpart1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * JUnit 4 tests for the Login class.
 *
 * @author Student
 */
public class LoginIT {

    // Registered user used across the tests
    public Login instance;

    private static final String FIRST_NAME = "Kyle";
    private static final String SURNAME = "Smith";

    private static final String VALID_USERNAME = "kyl_1";
    private static final String INVALID_USERNAME = "kyle!!!!!!!";

    private static final String VALID_PASSWORD = "Ch&&sec@ke99!";
    private static final String INVALID_PASSWORD = "password";

    private static final String VALID_CELLPHONE = "+27838968976";
    private static final String INVALID_CELLPHONE = "08966553";

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new Login(FIRST_NAME, SURNAME, VALID_USERNAME, VALID_PASSWORD, VALID_CELLPHONE);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    // ==============================
    // checkUsername
    // ==============================

    @Test
    public void testCheckUsername() {
        System.out.println("checkUsername");
        assertEquals(true, instance.checkUsername(VALID_USERNAME));
    }

    @Test
    public void testCheckUsernameIncorrect() {
        System.out.println("checkUsername - incorrectly formatted");
        assertEquals(false, instance.checkUsername(INVALID_USERNAME));
    }

    @Test
    public void testCheckUsernameTrue() {
        System.out.println("checkUsername - assertTrue");
        assertTrue(instance.checkUsername(VALID_USERNAME));
    }

    @Test
    public void testCheckUsernameFalse() {
        System.out.println("checkUsername - assertFalse");
        assertFalse(instance.checkUsername(INVALID_USERNAME));
    }

    // ==============================
    // checkPasswordComplexity
    // ==============================

    @Test
    public void testCheckPasswordComplexity() {
        System.out.println("checkPasswordComplexity");
        assertEquals(true, instance.checkPasswordComplexity(VALID_PASSWORD));
    }

    @Test
    public void testCheckPasswordComplexityIncorrect() {
        System.out.println("checkPasswordComplexity - does not meet requirements");
        assertEquals(false, instance.checkPasswordComplexity(INVALID_PASSWORD));
    }

    @Test
    public void testCheckPasswordComplexityTrue() {
        System.out.println("checkPasswordComplexity - assertTrue");
        assertTrue(instance.checkPasswordComplexity(VALID_PASSWORD));
    }

    @Test
    public void testCheckPasswordComplexityFalse() {
        System.out.println("checkPasswordComplexity - assertFalse");
        assertFalse(instance.checkPasswordComplexity(INVALID_PASSWORD));
    }

    // ==============================
    // checkCellphoneNumber
    // ==============================

    @Test
    public void testCheckCellphoneNumber() {
        System.out.println("checkCellphoneNumber");
        assertEquals(true, instance.checkCellphoneNumber(VALID_CELLPHONE));
    }

    @Test
    public void testCheckCellphoneNumberIncorrect() {
        System.out.println("checkCellphoneNumber - incorrectly formatted");
        assertEquals(false, instance.checkCellphoneNumber(INVALID_CELLPHONE));
    }

    @Test
    public void testCheckCellphoneNumberTrue() {
        System.out.println("checkCellphoneNumber - assertTrue");
        assertTrue(instance.checkCellphoneNumber(VALID_CELLPHONE));
    }

    @Test
    public void testCheckCellphoneNumberFalse() {
        System.out.println("checkCellphoneNumber - assertFalse");
        assertFalse(instance.checkCellphoneNumber(INVALID_CELLPHONE));
    }

    // ==============================
    // loginUser
    // ==============================

    @Test
    public void testLoginUser() {
        System.out.println("loginUser - successful");
        assertEquals(true, instance.loginUser(VALID_USERNAME, VALID_PASSWORD));
    }

    @Test
    public void testLoginUserFailed() {
        System.out.println("loginUser - failed");
        assertEquals(false, instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD));
    }

    @Test
    public void testLoginUserTrue() {
        System.out.println("loginUser - assertTrue");
        assertTrue(instance.loginUser(VALID_USERNAME, VALID_PASSWORD));
    }

    @Test
    public void testLoginUserFalse() {
        System.out.println("loginUser - assertFalse");
        assertFalse(instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD));
    }

    // ==============================
    // isLoginSuccessful
    // ==============================

    @Test
    public void testIsLoginSuccessful() {
        System.out.println("isLoginSuccessful - successful");
        instance.loginUser(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(instance.isLoginSuccessful(VALID_USERNAME));
    }

    @Test
    public void testIsLoginSuccessfulFailed() {
        System.out.println("isLoginSuccessful - failed");
        instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD);
        assertFalse(instance.isLoginSuccessful(INVALID_USERNAME));
    }

    // ==============================
    // GETTERS AND STATUS
    // ==============================

    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        assertEquals(FIRST_NAME, instance.getFirstName());
    }

    @Test
    public void testGetSurname() {
        System.out.println("getSurname");
        assertEquals(SURNAME, instance.getSurname());
    }

    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        assertEquals(VALID_USERNAME, instance.getUsername());
    }

    @Test
    public void testGetCellphoneNumber() {
        System.out.println("getCellphoneNumber");
        assertEquals(VALID_CELLPHONE, instance.getCellphoneNumber());
    }

    @Test
    public void testReturnLoginStatus() {
        System.out.println("returnLoginStatus");

        instance.loginUser(VALID_USERNAME, VALID_PASSWORD);
        String successStatus = instance.returnLoginStatus();

        instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD);
        String failedStatus = instance.returnLoginStatus();

        assertNotNull(successStatus);
        assertNotNull(failedStatus);
        assertNotEquals(successStatus, failedStatus);
    }

    @Test
    public void testDisplayLoginMessage() {
        System.out.println("displayLoginMessage");
        instance.loginUser(VALID_USERNAME, VALID_PASSWORD);
        instance.displayLoginMessage(); // passes if no exception is thrown
    }
}