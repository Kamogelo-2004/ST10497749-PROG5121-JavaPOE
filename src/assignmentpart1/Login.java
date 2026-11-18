package assignmentpart1;

public class Login {

    // ==============================
    // VARIABLES
    // ==============================

    private String firstName;
    private String surname;
    private String username;
    private String password;
    private String cellphoneNumber;

    // Tracks whether the last login attempt succeeded
    private boolean loginSuccessful = false;

    // ==============================
    // CONSTRUCTOR
    // ==============================

    public Login(String firstName,
                 String surname,
                 String username,
                 String password,
                 String cellphoneNumber) {

        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellphoneNumber;
    }

    // ==============================
    // GETTERS
    // ==============================

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    // ==============================
    // CHECK USERNAME
    // ==============================

    /*
     * Username must contain an underscore and be
     * no more than five characters long.
     */
    public boolean checkUsername(String enteredUsername) {

        return enteredUsername != null
                && enteredUsername.contains("_")
                && enteredUsername.length() <= 5;
    }

    // ==============================
    // CHECK PASSWORD COMPLEXITY
    // ==============================

    /*
     * Password must be at least eight characters and contain
     * a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String enteredPassword) {

        if (enteredPassword == null || enteredPassword.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < enteredPassword.length(); i++) {

            char character = enteredPassword.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }

    // ==============================
    // CHECK CELLPHONE NUMBER
    // ==============================

    /*
     * Number must start with an international country code
     * and be followed by nine digits, e.g. +27838968976
     */
    public boolean checkCellphoneNumber(String enteredCellphone) {

        return enteredCellphone != null
                && enteredCellphone.matches("^\\+\\d{1,3}\\d{9}$");
    }

    // ==============================
    // LOGIN USER
    // ==============================

    public boolean loginUser(String enteredUsername, String enteredPassword) {

        if (enteredUsername == null || enteredPassword == null) {
            loginSuccessful = false;
            return false;
        }

        loginSuccessful = username.equals(enteredUsername)
                && password.equals(enteredPassword);

        return loginSuccessful;
    }

    // ==============================
    // IS LOGIN SUCCESSFUL
    // ==============================

    public boolean isLoginSuccessful(String enteredUsername) {

        return loginSuccessful
                && username.equals(enteredUsername);
    }

    // ==============================
    // LOGIN STATUS MESSAGE
    // ==============================

    public String returnLoginStatus() {

        if (loginSuccessful) {
            return "Welcome " + firstName + " " + surname
                    + ", it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }

    public void displayLoginMessage() {
        System.out.println(returnLoginStatus());
    }
}