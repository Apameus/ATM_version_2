package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;

import javax.swing.*;

public class AccountPage {
    public static final String KEY = "AccountPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton logoutButton;
    private JButton depositButton;
    private JPanel buttonPanel;
    private JLabel infoText;
    private JLabel creditCardNumberText;
    private JLabel balanceText;
    //
    private CreditCardManager creditCardManager;
    private CreditCard creditCard;
    //

    public AccountPage(PanelManager manager){
        creditCardManager = new CreditCardManager();
        manager.addPanel(mainPanel, KEY);
        // buttons
        logoutButton.addActionListener(e -> {
            manager.showPanel(LoginPage.KEY);
        });

        depositButton.addActionListener(e -> {
            DepositPage depositPage = manager.getDepositPage();
            depositPage.refresh(creditCard);
            manager.showPanel(DepositPage.KEY);
        });

        withdrawButton.addActionListener(e -> {
            manager.showPanel(WithdrawPage.KEY);
        });

        transferButton.addActionListener(e -> {
            manager.showPanel(TransferPage.KEY);
        });

    }

    /**
     * Sets the current credit-card
     * @param creditCard
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard(){
        return creditCard;
    }

    /**
     * <b>Sets the</b> credit-card number & balance <b>labels</b> according to the current credit-card info
     */
    public void refresh() {
        // show the creditCardNumber
        creditCardNumberText.setText(creditCard.creditCardNumber);
        // show the creditCardBalance
        balanceText.setText(String.valueOf(creditCard.balance));
    }
}
