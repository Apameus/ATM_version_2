package gr.apameus.atm.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditCardManager {
    //ToDo make ll the checks inside here..
    private final List<CreditCard> creditCards;

    public CreditCardManager(){
        creditCards = new ArrayList<>();
    }

    /**
     * Registers a new credit-card with the specified credit-card number and pin
     *
     * @param creditCardNumber the credit-card number
     * @param creditCardPin the credit-card pin
     * @return true if the registration was successful or
     * false if another credit-card is already registered
     * with the specified credit-card number
     */
    public boolean register(String creditCardNumber, String creditCardPin){
        // check if the cardNumber is already assigned in the map
        for (CreditCard card : creditCards)
        {
            if (card.creditCardNumber.equals(creditCardNumber)){
                return false;
            }
        }
        // create the card and add it to the map
        CreditCard card = new CreditCard(creditCardNumber, creditCardPin, 0.0);
        creditCards.add(card);
        return true;
    }

    /**
     *  Locates a credit with the specified cardNumber and pin and returns it
     *
     * @param cardNumber the credit-card number
     * @param cardPin the credit-card pin
     * @return the credit card <b>or null if the credit-card was not found</b>
     */
    public CreditCard login(String cardNumber, String cardPin){
        for (CreditCard card : creditCards) {
            if (card.creditCardNumber.equals(cardNumber) && card.pin.equals(cardPin)){
                return card;}}
        return null;
    }


}
