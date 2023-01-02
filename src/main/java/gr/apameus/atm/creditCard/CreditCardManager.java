package gr.apameus.atm.creditCard;

import gr.apameus.atm.stream.Packet;
import gr.apameus.atm.stream.PacketStream;

import java.io.IOException;
import java.net.Socket;

public final class CreditCardManager {

    CreditCard current_creditCard;

    private Packet exchange(Packet packet){

        // connect
        try (Socket socket = new Socket("localhost",9999);
            PacketStream stream = new PacketStream(socket.getInputStream(), socket.getOutputStream())) {

            // send request
            stream.send(packet);

            // read response
            return stream.receive();

        } // close the connection
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    @SuppressWarnings("all")
    public boolean register(String creditCardNumber, String creditCardPin){
        Packet packet = new Packet.RegisterPacket(creditCardNumber, creditCardPin);
        Packet response = exchange(packet);
        return switch (response){
            case Packet.SuccessPacket(String msg) -> true;
            case Packet.ErrorPacket(String msg) -> false;
            default -> throw new IllegalStateException("Unexpected packet: " + response);
        };
    }

    /**
     *  Locates a credit with the specified cardNumber and pin and returns it
     *
     * @param cardNumber the credit-card number
     * @param cardPin the credit-card pin
     * @return the credit card <b>or null if the credit-card was not found</b>
     */
    @SuppressWarnings("all")
    public CreditCard login(String cardNumber, String cardPin){
       Packet packet = new Packet.LoginPacket(cardNumber, cardPin);
       Packet response = exchange(packet);
       return switch (response){
           case Packet.CreditCardPacket(String creditCardNumber, String creditCardPin, Double balance) -> current_creditCard = new CreditCard(creditCardNumber, creditCardPin, balance);
           case Packet.ErrorPacket(String msg) -> null;
           default -> throw new IllegalStateException("Unexpected packet: " + response);
       };
    }

    public void logout(){
        current_creditCard = null;
    }

    /**
     * Updates the balance of the current credit-card
     * @param amount the amount that the user want to deposit
     * @return true if the amount was added to the credit-card <b>or false if the amount was 0 or less</b>
     */
    @SuppressWarnings("all")
    public boolean deposit(Double amount){
        Packet packet = new Packet.DepositPacket(current_creditCard.creditCardNumber, amount);
        Packet response = exchange(packet);
        return switch (response){
            case Packet.BalancePacket(Double balance) -> {
                current_creditCard.balance = balance;
                yield true;
            }
            case Packet.ErrorPacket(String msg) -> false;
            default -> throw new IllegalStateException("Unexpected value: " + response);
        };
    }

    /**
     * Updates the balance of the current credit-card
     * @param amount the amount that the user want to withdraw
     * @return true if the amount was added to the credit-card <b>or false if the amount was 0 or less || if the amount is greater than the card balance</b>
     */
    @SuppressWarnings("all")
    public boolean withdraw(Double amount){
        Packet packet = new Packet.WithdrawPacket(current_creditCard.creditCardNumber ,amount);
        Packet response = exchange(packet);
        return switch (response){
            case Packet.BalancePacket(Double balance) -> {
                current_creditCard.balance = balance;
                yield true;
            }
            case Packet.ErrorPacket(String msg) -> false;
            default -> throw new IllegalStateException("Unexpected value: " + response);
        };
    }

    /**
     * Transfer the amount from one credit-card to another, and returns true
     * @param transferTo the credit-card (number) that we want to receive the amount
     * @param amount the amount we want to transfer
     * @return true if the transfer is completed <b>or false if the amount was 0 or less || the credit-card-number wasn't found in the list </b>
     */
    @SuppressWarnings("all")
    public boolean transfer(String transferTo, Double amount){
        Packet packet = new Packet.TransferPacket(current_creditCard.creditCardNumber ,transferTo, amount);
        Packet response = exchange(packet);
        return switch (response){
            case Packet.BalancePacket(Double balance) -> {
                current_creditCard.balance = balance;
                yield true;
            }
            case Packet.ErrorPacket(String msg) -> false;
            default -> throw new IllegalStateException("Unexpected value: " + response);
        };
        //current_balance
    }

    public double getCurrent_balance() {
        return current_creditCard.balance;
    }

}
