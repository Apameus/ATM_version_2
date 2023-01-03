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
     * Sends a <b>register request</b> to the server with the specified card number & pin.
     *
     * @param creditCardNumber the credit-card number
     * @param creditCardPin the credit-card pin
     * @return <b>true</b> if the server responde with CreditCard-Packet, <b>or null</b> if the server responde with Error-Packet.
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
     * Sends a <b>login request</b> to the server with the specified card number & pin.
     * @param cardNumber the credit-card number
     * @param cardPin the credit-card pin
     * @return <b>the new current credit-card </b>if the server responde with CreditCard-Packet, <b>or null</b> if the server responde with Error-Packet.
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
     * Send request to the server to update the balance of the current credit-card.
     * @param amount the amount that the user want to deposit.
     * @return <b>true</b> if the server responde with a Balance-Packet <b>or false</b> the server responde with a Error-Packet.
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
     * Send request to the server to update the balance of the current credit-card.
     * @param amount the amount that the user want to withdraw
     * @return <b>true</b> if the amount was added to the credit-card <b>or false</b> if the amount was 0 or less or if the amount is greater than the card balance
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
     * Send request to the server to transfer the amount from one credit-card to another.
     * @param transferTo the credit-card (number) that we want to receive the amount
     * @param amount the amount we want to transfer
     * @return <b>true</b> if the server responde with a Balance-Packet <b>or false</b> if the server responde with a Error-Packet.
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
    }

    /**
     * @return The balance of the current credit-card.
     */
    public double getCurrent_balance() {
        return current_creditCard.balance;
    }

}
