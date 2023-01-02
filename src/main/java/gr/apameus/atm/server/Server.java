package gr.apameus.atm.server;

import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static gr.apameus.atm.stream.Packet.*;

public class Server {

    ServerSocket serverSocket;
    List<CreditCard> creditCards;

    Server() throws IOException {
        serverSocket = new ServerSocket(9999);
        creditCards = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.start();

    }

    
    void start() {
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

    private Packet withdraw(String cardNumber, Double amount) {
        var index = findCreditCardIndex(cardNumber);
        if (index != null){
            creditCards.get(index).balance -= amount;
            return new BalancePacket(creditCards.get(index).balance);
        }
        return new ErrorPacket("Deposit unsuccessful");
    }

    private Packet deposit(String cardNumber, Double amount) {
        var index = findCreditCardIndex(cardNumber);
        if (index != null){
            creditCards.get(index).balance += amount;
            return new BalancePacket(creditCards.get(index).balance);
        }
        return new ErrorPacket("Deposit unsuccessful");
    }

    private Packet register(String cardNumber, String cardPin) {
        if (!alreadyExistCheck(cardNumber)){
            creditCards.add(new CreditCard(cardNumber, cardPin, 0));
            return new SuccessPacket("Account registered!");
        }
        return new ErrorPacket("CardNumber already exist!");
    }


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

    private static Boolean amountCheck(double amount) {
        if (amount >= 0){
            return false;
        }
        return true;
    }

    private static boolean alreadyExistCheck(List<CreditCard> creditCards, Connection connection, String cardNumber) {
        if (!creditCards.isEmpty()) {
            for (CreditCard card : creditCards) {
                if (card.creditCardNumber.equals(cardNumber)) {
                    connection.send("false");
                    return true;
                }
            }
        return false;
    }


}
