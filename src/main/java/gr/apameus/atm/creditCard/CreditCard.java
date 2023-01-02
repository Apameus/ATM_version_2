package gr.apameus.atm.creditCard;

public final class CreditCard {

    public final String creditCardNumber;
    public final String pin;
    public double balance;

    // constructor
    public CreditCard(String creditCardNumber, String pin, double balance){
        this.creditCardNumber = creditCardNumber;
        this.pin = pin;
        this.balance = balance;
    }

}
