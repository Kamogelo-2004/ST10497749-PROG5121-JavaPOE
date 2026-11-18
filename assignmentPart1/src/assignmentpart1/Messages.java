/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 
package assignmentpart1;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Messages {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    private static Scanner input = new Scanner(System.in);
    private static Random random = new Random();

    // Collections for message tracking
    private static ArrayList<Messages> sentMessages = new ArrayList<>();
    private static ArrayList<Messages> disregardedMessages = new ArrayList<>();
    private static ArrayList<Messages> storedMessages = new ArrayList<>();

    private static int totalMessages = 0;
    private static int messageCounter = 0;

    // Constructor
    public Messages(String id, int number, String recipient,
                    String message, String hash, String status) {
        this.messageID = id;
        this.messageNumber = number;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = hash;
        this.messageStatus = status;
    }

    public static void main(String[] args) {
        System.out.println("=== QuickChat Registration ===");

        String firstName, surname, username, cellphoneNumber, password;

        System.out.print("Enter your first name: ");
        firstName = input.nextLine();

        System.out.print("Enter your surname: ");
        surname = input.nextLine();

        // Temporary validator instance
        Login validator = new Login(firstName, surname, "", "", "");

        // Username Validation Loop
        while (true) {
            System.out.print("Enter your username: ");
            username = input.nextLine();

            if (validator.checkUsername(username)) {
                System.out.println("Username successfully captured.\n");
                break;
            } else {
                System.out.println("Username is not correctly formatted.");
                System.out.println("Username must contain an underscore and be no more than 5 characters long.\n");
            }
        }

        // Cellphone Number Validation Loop
        while (true) {
            System.out.print("Enter your cellphone number: ");
            cellphoneNumber = input.nextLine();

            if (validator.checkCellphoneNumber(cellphoneNumber)) {
                System.out.println("Cellphone number successfully captured.\n");
                break;
            } else {
                System.out.println("Cellphone number is not correctly formatted.");
                System.out.println("It must start with an international country code followed by nine digits, e.g. +27838968976\n");
            }
        }

        // Password Validation Loop
        while (true) {
            System.out.print("Enter your password: ");
            password = input.nextLine();

            if (validator.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.\n");
                break;
            } else {
                System.out.println("Password is not correctly formatted.");
                System.out.println("Password must be at least 8 characters, contain a capital letter, a number, and a special character.\n");
            }
        }

        // Create the registered user account
        Login registeredUser = new Login(firstName, surname, username, password, cellphoneNumber);

        System.out.println("================================");
        System.out.println("Registration successful!");
        System.out.println("Welcome " + registeredUser.getFirstName() + " " + registeredUser.getSurname());
        System.out.println("================================\n");

        // ==============================
        // LOGIN SECTION
        // ==============================
        System.out.println("========== LOGIN ==========");
        boolean loginSuccess = false;

        while (!loginSuccess) {
            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            loginSuccess = registeredUser.loginUser(loginUsername, loginPassword);
            System.out.println();
            registeredUser.displayLoginMessage();
            System.out.println();

            if (!loginSuccess) {
                System.out.println("Please try logging in again.\n");
            }
        }

        // ==============================
        // QUICKCHAT MESSAGING MENU
        // ==============================
        System.out.print("How many messages would you like to enter? ");
        int numberOfMessages = readPositiveNumber();

        int option = 0;

        while (option != 3) {
            System.out.println("\n1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Enter your option: ");

            try {
                option = Integer.parseInt(input.nextLine());

                switch (option) {
                    case 1:
                        for (int i = 0; i < numberOfMessages; i++) {
                            sendMessage(input);
                        }
                        numberOfMessages = 0;
                        break;

                    case 2:
                        System.out.println("Coming Soon.");
                        break;

                    case 3:
                        System.out.println("Total messages sent: " + totalMessages);
                        System.out.println("Thank you for using QuickChat.");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        input.close();
    }

    public static int readPositiveNumber() {
        while (true) {
            try {
                int number = Integer.parseInt(input.nextLine());
                if (number > 0) {
                    return number;
                }
                System.out.print("Enter a number greater than zero: ");
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    public static void sendMessage(Scanner inputScanner) {
        String id = generateMessageID();

        System.out.print("\nEnter recipient cellphone number: ");
        String recipient = inputScanner.nextLine();

        if (!isValidRecipient(recipient)) {
            System.out.println("Invalid recipient cellphone number.");
            return;
        }

        System.out.print("Enter your message: ");
        String message = inputScanner.nextLine();

        if (message.trim().isEmpty() || message.length() >= 250) {
            System.out.println("Please enter a message of less than 250 characters.");
            return;
        }

        messageCounter++;
        String hash = generateMessageHash(id, messageCounter, message);

        Messages newMessage = new Messages(id, messageCounter, recipient, message, hash, "Pending");

        System.out.println("\nMessage ID: " + id);
        System.out.println("Message Hash: " + hash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);

        System.out.println("\nChoose an action:");
        System.out.println("1. Send Message");
        System.out.println("0. Disregard Message");
        System.out.println("2. Store Message to send later");
        System.out.print("Your choice: ");

        String action = inputScanner.nextLine();

        switch (action) {
            case "1":
                newMessage.messageStatus = "Sent";
                sentMessages.add(newMessage);
                totalMessages++;
                System.out.println("Message successfully sent.");
                displayMessage(newMessage);
                break;

            case "0":
                newMessage.messageStatus = "Disregarded";
                disregardedMessages.add(newMessage);
                System.out.println("Message disregarded successfully.");
                break;

            case "2":
                newMessage.messageStatus = "Stored";
                storedMessages.add(newMessage);
                storeMessage(newMessage);
                break;

            default:
                System.out.println("Invalid action. Message not sent.");
        }
    }

    public static String generateMessageID() {
        long id = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public static boolean isValidRecipient(String recipient) {
        return recipient.matches("\\+[1-9][0-9]{7,14}");
    }

    public static String generateMessageHash(String id, int number, String message) {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (id.substring(0, 2) + ":" + number + ":" + firstWord + lastWord).toUpperCase();
    }

    public static void displayMessage(Messages msg) {
        System.out.println("\n--- Message Details ---");
        System.out.println("Message ID: " + msg.messageID);
        System.out.println("Message Hash: " + msg.messageHash);
        System.out.println("Recipient: " + msg.recipient);
        System.out.println("Message: " + msg.message);
    }

    public static void storeMessage(Messages msg) {
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            String json = "{\n"
                    + "  \"messageID\": \"" + escapeJSON(msg.messageID) + "\",\n"
                    + "  \"messageNumber\": " + msg.messageNumber + ",\n"
                    + "  \"messageHash\": \"" + escapeJSON(msg.messageHash) + "\",\n"
                    + "  \"recipient\": \"" + escapeJSON(msg.recipient) + "\",\n"
                    + "  \"message\": \"" + escapeJSON(msg.message) + "\",\n"
                    + "  \"status\": \"" + escapeJSON(msg.messageStatus) + "\"\n"
                    + "}\n";

            writer.write(json);
            System.out.println("Message successfully stored.");

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public static String escapeJSON(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}