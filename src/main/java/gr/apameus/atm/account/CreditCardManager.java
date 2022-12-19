package gr.apameus.atm.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditCardManager {
    private static Map<String, String> creditCardsInfo;

    public CreditCardManager(List<CreditCard> creditCards){
        creditCardsInfo = new HashMap<>();
        for (CreditCard card : creditCards) {
            String cardNumber = card.creditCardNumber;
            String pin = card.pin;
            creditCardsInfo.put(cardNumber, pin);
        }
    }

    public void login(){}

    public static Map<String, String> getCreditCardsInfo() {
        return creditCardsInfo;
    }
}
