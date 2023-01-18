package gr.apameus.atm.server;

import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.stream.Packet;
import gr.apameus.atm.stream.PacketStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gr.apameus.atm.stream.Packet.*;

public class Server {

    ServerSocket serverSocket;
    List<CreditCard> creditCards;

    // contractor
    Server() throws IOException {
        serverSocket = new ServerSocket(9999);
        creditCards = new ArrayList<>();
    }

    // main method (runnable)
    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.startLocal();

    }

    /**
     * Starts a new request - response connection.
     */
    void startSocket() {
        while (serverSocket.isBound()){

            // Accept
            try (Socket socket = serverSocket.accept();
                 PacketStream stream = PacketStream.forSocket(socket)){

                // Read request
                var request = stream.receive();

                // ... process request
                Packet response = processRequest(request);

                // Send response
                stream.send(response);
            } // Close connection
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts a new request - response connection, with local disk operations.
     */
    void startLocal() {
        // Parse the file **
        creditCards = parse();

        while (serverSocket.isBound()){

            // Accept
            try (Socket socket = serverSocket.accept();
                 PacketStream stream = PacketStream.forSocket(socket)){

                // Read request
                var request = stream.receive();

                // ... process request
                Packet response = processRequest(request);

                // Send response
                stream.send(response);

                // Serialize **
                serialize(creditCards);
            } // Close connection
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void serialize(List<CreditCard> creditCards) {
        List<String> lines = new ArrayList<>();
        for (var creditCard : creditCards){
            String line =  "Credit_Card_Number: " + creditCard.creditCardNumber + "\n" +
                    "Pin: " + creditCard.pin + "\n" +
                    "Balance: " + creditCard.balance + "\n" ;
            lines.add(line);

        }
        saveToFile(lines);
    }

    private void saveToFile(List<String> lines) {
        try {
            Files.write(Path.of("C:\\Users\\Ιωάννης Τζωρτζίνης\\IdeaProjects\\ATM_version_2\\src\\main\\java\\gr\\apameus\\atm\\server\\ATM_v.2_FILE.txt"),lines);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private List<CreditCard> parse() {
        List<String> lines = getAllLines();
        lines.removeAll(Collections.singleton(""));
        for (int linesPassed = 0; lines.size() > linesPassed; linesPassed += 3){
            var number = getNumber(lines.get(linesPassed));
            var pin = getPin(lines.get(linesPassed + 1));
            var balance = getBalance(lines.get(linesPassed + 2));
            CreditCard creditCard = new CreditCard(number, pin, balance);
            creditCards.add(creditCard);
        }
        return creditCards;
    }

    private Double getBalance(String line) {
        if (line.startsWith("Balance:")){
            var lineOf = line.split(" ");
            var balance = Double.parseDouble(lineOf[1]);
            return balance;
        }
        throw new IllegalArgumentException("Something went wrong in getBalance method");
    }

    private String getPin(String line) {
        if (line.startsWith("Pin:")){
            var lineOf = line.split(" ");
            var pin = lineOf[1];
            return pin;
        }
        throw new IllegalArgumentException("Something went wrong in getPin method");
    }

    private String getNumber(String line) {
        if (line.startsWith("Credit_Card_Number:")){
            var lineOf = line.split(" ");
            var number = lineOf[1];
            return number;
        }
        throw new IllegalArgumentException("Something went wrong in getNumber method");
    }

    private List<String> getAllLines() {
        try {
            return Files.readAllLines(Path.of("C:\\Users\\Ιωάννης Τζωρτζίνης\\IdeaProjects\\ATM_version_2\\src\\main\\java\\gr\\apameus\\atm\\server\\ATM_v.2_FILE.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Call the needed method according to the passed packet.
     * @param packet the packet.
     * @return the analogous packet in every situation.
     */
    private Packet processRequest(Packet packet){

        return switch (packet){
            case RegisterPacket(String cardNumber, String cardPin) -> {
                yield register(cardNumber, cardPin);
            }
            case LoginPacket(String cardNumber, String cardPin) -> {
                yield login(cardNumber, cardPin);
            }
            case DepositPacket(String cardNumber, Double amount) -> {
                yield deposit(cardNumber, amount);
            }
            case WithdrawPacket(String cardNumber, Double amount) -> {
                yield withdraw(cardNumber, amount);
            }
            case TransferPacket(String cardNumber, String transferTo, Double amount) ->{
                yield transfer(cardNumber, transferTo, amount);
            }
            default -> throw new IllegalStateException("Unexpected value: " + packet);
        };
    }

    /**
     * <b>Deducts</b> the amount from the card's balance specified by card number, and <b>increase</b> the balance of the other card also specified by number (transferTo).
     * @param cardNumber the credit-card number.
     * @param transferTo the credit-card number of the receiver.
     * @param amount the amount to be transferred.
     * @return Balance-Packet if amount was transferred successfully, or Error-Packet if not.
     */
    private Packet transfer(String cardNumber, String transferTo, Double amount) {
        var index = findCreditCardIndex(cardNumber);
        var index2 = findCreditCardIndex(transferTo);
        if (index != null && index2 != null){
            creditCards.get(index).balance -= amount;
            creditCards.get(index2).balance += amount;
            return new BalancePacket(creditCards.get(index).balance);
        }
        return new ErrorPacket("Transfer failed!");
    }

    /**
     * Updates the balance of the credit-card specified by the card number.
     * @param cardNumber the card number.
     * @param amount the amount to be withdrawn.
     * @return Balance-Packet if amount was deducted successfully, else Error-Packet.
     */
    private Packet withdraw(String cardNumber, Double amount) {
        var index = findCreditCardIndex(cardNumber);
        if (index != null){
            creditCards.get(index).balance -= amount;
            return new BalancePacket(creditCards.get(index).balance);
        }
        return new ErrorPacket("Deposit unsuccessful");
    }

    /**
     * Updates the balance of the credit-card specified by the card number.
     * @param cardNumber the card number.
     * @param amount the amount to be deposited.
     * @return Balance-Packet if amount was added successfully, else Error-Packet.
     */
    private Packet deposit(String cardNumber, Double amount) {
        var index = findCreditCardIndex(cardNumber);
        if (index != null){
            creditCards.get(index).balance += amount;
            return new BalancePacket(creditCards.get(index).balance);
        }
        return new ErrorPacket("Deposit unsuccessful");
    }

    /**
     * <b>Saves a new credit-card</b> with the specified credit-card number and pin.
     * @param cardNumber the credit-card number.
     * @param cardPin the credit-card pin.
     * @return Success-Packet if the registration was successful or
     *       Error-Packet if another credit-card is already registered
     *       with the specified credit-card number
     */
    private Packet register(String cardNumber, String cardPin) {
        if (!alreadyExistCheck(cardNumber)){
            creditCards.add(new CreditCard(cardNumber, cardPin, 0));
            //**

            //**
            return new SuccessPacket("Account registered!");
        }
        return new ErrorPacket("CardNumber already exist!");
    }

    /**
     * Locates a credit with the specified card number & pin and returns the analogous packet.
     * @param cardNumber the credit-card number.
     * @param cardPin the credit-card pin
     * @return CreditCard-Packet if a credit-card with the specific number & pin was found, or Error-Packet if else.
     */
    private Packet login(String cardNumber, String cardPin) {
        CreditCard card = findCreditCard(cardNumber, cardPin);
        if (card != null){
            return new CreditCardPacket(cardNumber, cardPin, card.balance);
        }
        return new ErrorPacket("Invalid username or password!");
    }


    /**
     * Searches for a specific credit card with the specified
     * creditCardNumber & pin and returns it, or else it returns null
     * @param creditCardNumber the credit card number
     * @param pin the pin
     * @return the creditCard or null.
     */
    private CreditCard findCreditCard(String creditCardNumber, String pin) {
        for (CreditCard creditCard : creditCards) {
            if (creditCardNumber.equals(creditCard.creditCardNumber) && pin.equals(creditCard.pin)){
                return creditCard;
            }
        }
        return null;
    }

    /**
     * @param creditCardNumber
     * @return the index of the specified card number in the list, <b>or null if none.</b>
     */
    private Integer findCreditCardIndex(String creditCardNumber) {
        var i = -1;
        for (CreditCard creditCard : creditCards) {
            i++;
            if (creditCardNumber.equals(creditCard.creditCardNumber)){
                return i;
            }
        }
        return null;
    }

    /**
     * @param cardNumber
     * @return true if another card exist with the specified card number, else false.
     */
    private boolean alreadyExistCheck(String cardNumber) {
            for (CreditCard card : creditCards) {
                if (card.creditCardNumber.equals(cardNumber)) {
                    return true;
                }
            }
        return false;
    }


}
