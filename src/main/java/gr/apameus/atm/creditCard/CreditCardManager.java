package gr.apameus.atm.creditCard;

import gr.apameus.atm.server.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class CreditCardManager {

    double current_balance = 0.0;

    private Socket socket;
    private Connection connection;
    private final List<CreditCard> creditCards;

    // constructor
    public CreditCardManager() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(9999));
        connection = new Connection(socket);
        creditCards = new ArrayList<>();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * <b>Saves a new credit-card</b> with the specified credit-card number and pin <b>in the server</b>
     *
     * @param creditCardNumber the credit-card number
     * @param creditCardPin the credit-card pin
     * @return true if the registration was successful or
     * false if another credit-card is already registered
     * with the specified credit-card number
     */
    public boolean register(String creditCardNumber, String creditCardPin){
        connection.send(createTheLine(creditCardNumber,creditCardPin,"register"));
        return Boolean.parseBoolean(connection.receive());
    }

    /**
     *  Locates a credit with the specified cardNumber and pin and returns it
     *
     * @param cardNumber the credit-card number
     * @param cardPin the credit-card pin
     * @return the credit card <b>or null if the credit-card was not found</b>
     */
    public CreditCard login(String cardNumber, String cardPin){
        connection.send(createTheLine(cardNumber,cardPin,"login"));
        if (Boolean.parseBoolean(connection.receive())){
            current_balance = Double.valueOf(connection.receive());
            CreditCard creditCard = new CreditCard(this,cardNumber,cardPin,current_balance);
            return creditCard;
        }
        return null;
    }

    /**
     * Updates the balance of the current credit-card
     * @param amount the amount that the user want to deposit
     * @return true if the amount was added to the credit-card <b>or false if the amount was 0 or less</b>
     */
    public boolean deposit(Double amount){
        connection.send(createTheLine("deposit", amount));
        current_balance = Double.parseDouble(connection.receive());
        return Boolean.parseBoolean(connection.receive());
    }

    /**
     * Updates the balance of the current credit-card
     * @param amount the amount that the user want to withdraw
     * @return true if the amount was added to the credit-card <b>or false if the amount was 0 or less || if the amount is greater than the card balance</b>
     */
    public boolean withdraw(Double amount){
        connection.send(createTheLine("withdraw", amount));
        current_balance = Double.parseDouble(connection.receive());
        return Boolean.parseBoolean(connection.receive());
    }

    /**
     * Transfer the amount from one credit-card to another, and returns true
     * @param transferTo the credit-card (number) that we want to receive the amount
     * @param amount the amount we want to transfer
     * @return true if the transfer is completed <b>or false if the amount was 0 or less || the credit-card-number wasn't found in the list </b>
     */
    public boolean transfer(String transferTo, Double amount){
        connection.send(createTheLine("transfer", amount));
        connection.send(transferTo);
        current_balance = Double.parseDouble(connection.receive());
        return Boolean.parseBoolean(connection.receive());
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    String createTheLine(String cardNumber, String cardPin, String method){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(cardNumber);
        joiner.add(cardPin);
        joiner.add(method);
        return joiner.toString();
    }
    String createTheLine(String method, double amount){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(method);
        joiner.add(String.valueOf(amount));
        return joiner.toString();
    }
}
