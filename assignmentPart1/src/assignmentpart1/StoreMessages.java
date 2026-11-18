/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignmentpart1;

/**
 *
 * @author Student
 */



import java.util.ArrayList;
import java.util.Scanner;

public class StoreMessages {

    // ==========================================
    // ARRAYS FOR STORING MESSAGES
    // ==========================================

    public static ArrayList<MessageRecord> sentMessages = new ArrayList<>();
    public static ArrayList<MessageRecord> disregardedMessages = new ArrayList<>();
    public static ArrayList<MessageRecord> storedMessages = new ArrayList<>();

    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    private static Scanner input = new Scanner(System.in);

    // ==========================================
    // MESSAGE RECORD CLASS
    // ==========================================

    public static class MessageRecord {

        private String messageID;
        private String messageHash;
        private String sender;
        private String recipient;
        private String message;
        private String status;

        public MessageRecord(String messageID, String messageHash,
                             String sender, String recipient,
                             String message, String status) {

            this.messageID = messageID;
            this.messageHash = messageHash;
            this.sender = sender;
            this.recipient = recipient;
            this.message = message;
            this.status = status;
        }

        public String getMessageID() {
            return messageID;
        }

        public String getMessageHash() {
            return messageHash;
        }

        public String getSender() {
            return sender;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {

            return "Message ID: " + messageID
                    + "\nMessage Hash: " + messageHash
                    + "\nSender: " + sender
                    + "\nRecipient: " + recipient
                    + "\nMessage: " + message
                    + "\nStatus: " + status;
        }
    }

    // ==========================================
    // ADD MESSAGE TO ARRAYS
    // ==========================================

    public static void addMessage(String id, String hash,
                                  String sender, String recipient,
                                  String message, String status) {

        MessageRecord newMessage = new MessageRecord(
                id, hash, sender, recipient, message, status);

        messageIDs.add(id);
        messageHashes.add(hash);

        if (status.equalsIgnoreCase("Sent")) {

            sentMessages.add(newMessage);

        } else if (status.equalsIgnoreCase("Disregarded")
                || status.equalsIgnoreCase("Disregard")) {

            disregardedMessages.add(newMessage);

        } else if (status.equalsIgnoreCase("Stored")) {

            storedMessages.add(newMessage);
        }
    }

    // ==========================================
    // STORED MESSAGES MENU
    // ==========================================

    public static void storedMessagesMenu() {

        int choice;

        do {

            System.out.println("\n===== STORED MESSAGES MENU =====");
            System.out.println("1. Display sender and recipient");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search using message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete message using message hash");
            System.out.println("6. Display full message report");
            System.out.println("7. Return to main menu");

            System.out.print("Enter your choice: ");

            while (!input.hasNextInt()) {
                System.out.print("Enter a number from 1 to 7: ");
                input.next();
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:
                    displaySendersAndRecipients();
                    break;

                case 2:
                    displayLongestMessage();
                    break;

                case 3:
                    searchByMessageID();
                    break;

                case 4:
                    searchByRecipient();
                    break;

                case 5:
                    deleteMessageByHash();
                    break;

                case 6:
                    displayFullMessageReport();
                    break;

                case 7:
                    System.out.println("Returning to main menu.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 7);
    }

    // ==========================================
    // OPTION 1: DISPLAY SENDER AND RECIPIENT
    // ==========================================

    public static void displaySendersAndRecipients() {

        if (storedMessages.isEmpty()) {

            System.out.println("There are no stored messages.");
            return;
        }

        for (MessageRecord msg : storedMessages) {

            System.out.println("Sender: " + msg.getSender());
            System.out.println("Recipient: " + msg.getRecipient());

            System.out.println("--------------------------");
        }
    }

    // ==========================================
    // OPTION 2: DISPLAY LONGEST MESSAGE
    // ==========================================

    public static void displayLongestMessage() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages available.");
            return;
        }

        MessageRecord longest = storedMessages.get(0);

        for (MessageRecord msg : storedMessages) {

            if (msg.getMessage().length()
                    > longest.getMessage().length()) {

                longest = msg;
            }
        }

        System.out.println("\nLongest Stored Message:");
        System.out.println(longest.getMessage());
    }

    // ==========================================
    // OPTION 3: SEARCH USING MESSAGE ID
    // ==========================================

    public static void searchByMessageID() {

        System.out.print("Enter message ID: ");

        String searchID = input.nextLine();

        boolean found = false;

        for (MessageRecord msg : storedMessages) {

            if (msg.getMessageID().equals(searchID)) {

                System.out.println("Recipient: "
                        + msg.getRecipient());

                System.out.println("Message: "
                        + msg.getMessage());

                found = true;
                break;
            }
        }

        if (!found) {

            System.out.println("Message ID not found.");
        }
    }

    // ==========================================
    // OPTION 4: SEARCH BY RECIPIENT
    // ==========================================

    public static void searchByRecipient() {

        System.out.print("Enter recipient number: ");

        String searchRecipient = input.nextLine();

        boolean found = false;

        for (MessageRecord msg : storedMessages) {

            if (msg.getRecipient().equalsIgnoreCase(searchRecipient)) {

                System.out.println(msg);

                System.out.println("--------------------------");

                found = true;
            }
        }

        if (!found) {

            System.out.println("No messages found for this recipient.");
        }
    }

    // ==========================================
    // OPTION 5: DELETE MESSAGE USING HASH
    // ==========================================

    public static void deleteMessageByHash() {

        if (storedMessages.isEmpty()) {

            System.out.println("There are no stored messages to delete.");
            return;
        }

        System.out.print("Enter message hash: ");

        String searchHash = input.nextLine();

        boolean found = false;

        for (int i = 0; i < storedMessages.size(); i++) {

            MessageRecord msg = storedMessages.get(i);

            if (msg.getMessageHash().equalsIgnoreCase(searchHash)) {

                System.out.println("\nMessage found!");

                System.out.println("Message ID: " + msg.getMessageID());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessage());

                // Remove message from stored messages
                storedMessages.remove(i);

                // Remove corresponding hash and ID
                messageHashes.remove(msg.getMessageHash());
                messageIDs.remove(msg.getMessageID());

                System.out.println("\nMessage successfully deleted!");

                found = true;
                break;
            }
        }

        if (!found) {

            System.out.println("No message found with that hash.");
        }
    }

    // ==========================================
    // OPTION 6: DISPLAY FULL MESSAGE REPORT
    // ==========================================

    public static void displayFullMessageReport() {

        if (storedMessages.isEmpty()) {

            System.out.println("There are no stored messages available.");
            return;
        }

        System.out.println("\n==================================");
        System.out.println("     FULL STORED MESSAGE REPORT");
        System.out.println("==================================");

        for (int i = 0; i < storedMessages.size(); i++) {

            MessageRecord msg = storedMessages.get(i);

            System.out.println("\nMessage Number: " + (i + 1));

            System.out.println("----------------------------------");

            System.out.println("Message ID: " + msg.getMessageID());
            System.out.println("Sender: " + msg.getSender());
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessage());
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Message Status: " + msg.getStatus());

            System.out.println("----------------------------------");
        }

        System.out.println("\nTotal Stored Messages: "
                + storedMessages.size());

        System.out.println("==================================");
    }

}
