package gr.apameus.atm.account;

public class CreditCard {
    String creditCardNumber;
    String pin;
    Double balance;

    public CreditCard(String creditCardNumber, String pin, Double balance){
        this.creditCardNumber = creditCardNumber;
        this.pin = pin;
        this.balance = balance;
    }
}
