package gr.apameus.atm.account;

public class CreditCard {
    public String creditCardNumber;
    public String pin;
    public Double balance;

    public CreditCard(String creditCardNumber, String pin, Double balance){
        this.creditCardNumber = creditCardNumber;
        this.pin = pin;
        this.balance = balance;
    }
}
