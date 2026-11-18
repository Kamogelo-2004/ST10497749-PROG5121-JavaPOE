package assignmentpart1;

import java.util.Scanner;

public class AssignmentPart1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String firstName;
        String surname;
        String username;
        String cellphoneNumber;
        String password;

        // ==============================
        // REGISTRATION
        // ==============================

        System.out.print("Enter your first name: ");
        firstName = input.nextLine();

        System.out.print("Enter your surname: ");
        surname = input.nextLine();

        /*
         * A temporary object used only to run the validation
         * methods while the details are being captured.
         */
        Login validator = new Login(firstName, surname, "", "", "");

        // ==============================
        // USERNAME
        // ==============================

        while (true) {

            System.out.print("Enter your username: ");
            username = input.nextLine();

            if (validator.checkUsername(username)) {

                System.out.println("Username successfully captured.");
                break;

            } else {

                System.out.println("Username is not correctly formatted.");
                System.out.println("Username must contain an underscore.");
                System.out.println("Username must be no more than 5 characters long.");
                System.out.println();
            }
        }

        // ==============================
        // CELLPHONE NUMBER
        // ==============================

        while (true) {

            System.out.print("Enter your cellphone number: ");
            cellphoneNumber = input.nextLine();

            if (validator.checkCellphoneNumber(cellphoneNumber)) {

                System.out.println("Cellphone number successfully captured.");
                break;

            } else {

                System.out.println("Cellphone number is not correctly formatted.");
                System.out.println("It must start with an international country code");
                System.out.println("followed by nine digits, e.g. +27838968976");
                System.out.println();
            }
        }

        // ==============================
        // PASSWORD
        // ==============================

        while (true) {

            System.out.print("Enter your password: ");
            password = input.nextLine();

            if (validator.checkPasswordComplexity(password)) {

                System.out.println("Password successfully captured.");
                break;

            } else {

                System.out.println("Password is not correctly formatted.");
                System.out.println("Password must:");
                System.out.println("- Be at least 8 characters long");
                System.out.println("- Contain a capital letter");
                System.out.println("- Contain a number");
                System.out.println("- Contain a special character");
                System.out.println();
            }
        }

        // ==============================
        // CREATE THE REGISTERED USER
        // ==============================

        Login user = new Login(
                firstName,
                surname,
                username,
                password,
                cellphoneNumber
        );

        System.out.println();
        System.out.println("================================");
        System.out.println("Registration successful!");
        System.out.println("Welcome " + user.getFirstName() + " " + user.getSurname());
        System.out.println("================================");

        // ==============================
        // LOGIN
        // ==============================

        System.out.println();
        System.out.println("========== LOGIN ==========");

        System.out.print("Enter your username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        user.loginUser(loginUsername, loginPassword);

        System.out.println();
        System.out.println(user.returnLoginStatus());

        input.close();
    }
}