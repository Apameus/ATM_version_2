package gr.apameus.atm.creditCard;

public final class CreditCard {

    private final CreditCardManager manager;
    public final String creditCardNumber;
    public final String pin;
    public double balance;

    // constructor
    public CreditCard(CreditCardManager manager, String creditCardNumber, String pin, double balance){
        this.manager = manager;
        this.creditCardNumber = creditCardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
