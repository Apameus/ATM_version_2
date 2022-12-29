package gr.apameus.atm.server;

import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        List<CreditCard> creditCards = new ArrayList<>();
        CreditCardManager manager;

        // create the server
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9999));

        // accept the connection
        Socket socket = serverSocket.accept();
        Connection connection = new Connection(socket);

        while (socket.isConnected()) {
            // create the panel manager
            manager = new CreditCardManager();

            // read the client message
            /// ex. "Ioannis,Ioannis123,Register"
            String receive = connection.receive();

            System.out.println("receive = " + receive);

            // empty check
            if (receive.isEmpty()) {
                break;
            }

            var attributes = receive.split(",");
            String cardNumber = attributes[0];
            String cardPin = attributes[1];
            String action = attributes[2];

            // respond
            switch (action) {
                case "register" -> {
                    if (alreadyExistCheck(creditCards, connection, cardNumber)) continue;
                    CreditCard card = new CreditCard(manager, cardNumber, cardPin, 0.0);
                    creditCards.add(card);
                    connection.send("true");
                }
                case "login" -> {
                    // empty creditCardList check
                    if (creditCards.isEmpty()) {
                        connection.send("false");
                        continue;
                    }
                    for (CreditCard currentCreditCard : creditCards) {
                        if (currentCreditCard.creditCardNumber.equals(cardNumber) && currentCreditCard.pin.equals(cardPin)) {
                            connection.send("true");
                            connection.send(String.valueOf(currentCreditCard.balance));
                            //
                            label:
                            while (true) {
                                // read the secondary client message
                                /// ex. "deposit,180"
                                String receive2 = connection.receive();
                                System.out.println("receive2 = " + receive2);

                                String[] attributes2 = receive2.split(",");
                                String method = attributes2[0];
                                double amount = Double.parseDouble(attributes2[1]);

                                if (amountCheck(amount)) break;

                                switch (method) {
                                    case "logout":
                                        //currentCreditCard = null;
                                        break label;
                                    case "deposit":
                                        deposit(connection, currentCreditCard, amount);
                                        break;
                                    case "withdraw":
                                        withdraw(connection, currentCreditCard, amount);
                                        break;
                                    case "transfer":
                                        transfer(creditCards, connection, currentCreditCard, amount);
                                        break;
                                    default:
                                        connection.send("false");
                                        break;
                                }
                            }
                        } else {
                            connection.send("false");
                        }

                    }
                }
            }
        }
        connection.close();
        serverSocket.close();
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
        }
        return false;
    }

    private static void transfer(List<CreditCard> creditCards, Connection connection, CreditCard currentCreditCard, double amount) {
        String transferTo = connection.receive();
        // amount is greater than card balance
        if (amount > currentCreditCard.balance){
            connection.send(String.valueOf(currentCreditCard.balance));
            connection.send("false");
        }
        currentCreditCard.balance -= amount;
        for (CreditCard card : creditCards) {
            if (card.creditCardNumber.equals(transferTo)) {
                card.balance += amount;
            }
        }
        connection.send(String.valueOf(currentCreditCard.balance));
        connection.send("true");
    }

    private static void withdraw(Connection connection, CreditCard currentCreditCard, double amount) {
        // amount is greater than the card's balance
        if (amount > currentCreditCard.balance){
            connection.send(String.valueOf(currentCreditCard.balance));
            connection.send("false");
        }
        currentCreditCard.balance -= amount;
        connection.send(String.valueOf(currentCreditCard.balance));
        connection.send("true");
    }

    private static void deposit(Connection connection, CreditCard currentCreditCard, double amount) {
        currentCreditCard.balance += amount;
        connection.send(String.valueOf(currentCreditCard.balance));
        connection.send("true");
    }
}
