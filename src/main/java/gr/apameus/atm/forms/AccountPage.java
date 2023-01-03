package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
    private CreditCard creditCard;
    private double current_balance;
    private final CreditCardManager creditCardManager;

    // constructor
    public AccountPage(PanelManager manager) {

        creditCardManager = manager.getCreditCardManager();
        //connection = creditCardManager.getConnection();

        manager.addPanel(mainPanel, KEY);
        // buttons //
        // logout
        logoutButton.addActionListener(e -> {
            //connection.send("logout,0");
            creditCardManager.logout();
            manager.showPanel(LoginPage.KEY);
        });
        // deposit
        depositButton.addActionListener(e -> {
            DepositPage depositPage = manager.getDepositPage();
            depositPage.refresh();
            manager.showPanel(DepositPage.KEY);
        });
        // withdraw
        withdrawButton.addActionListener(e -> {
            // checks if the card balance is 0
            if (zeroBalanceCheck()){
                showError("Your balance is 0!");
                return;
            }
            WithdrawPage withdrawPage = manager.getWithdrawPage();
            withdrawPage.refresh();
            manager.showPanel(WithdrawPage.KEY);
        });
        // transfer
        transferButton.addActionListener(e -> {
            // checks if the card balance is 0
            if (zeroBalanceCheck()){
                showError("Your balance is 0!");
                return;
            }
            TransferPage transferPage = manager.getTransferPage();
            transferPage.refresh();
            manager.showPanel(TransferPage.KEY);
        });

    }

    /**
     * Checks if the credit-card balance is 0, so there is no reason to show the WithdrawPage.
     * @return true if the credit-card balance is 0, or false if it's not.
     */
    private Boolean zeroBalanceCheck() {
        if (creditCardManager.getCurrent_balance() == 0){
            return true;
        }
        return false;
    }

    /**
     * Sets the current credit-card.
     * @param creditCard
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }


    /**
     * <b>Sets the</b> credit-card number & balance <b>labels</b> according to the current credit-card info.
     */
    public void refresh() {
        // show the creditCardNumber
        creditCardNumberText.setText(creditCard.creditCardNumber);
        // show the creditCardBalance
        balanceText.setText(String.valueOf(creditCardManager.getCurrent_balance()));
        // clear the info field
        infoText.setText("");
    }
    /**
     * Setting the message you passed in the info text with red color.
     * @param msg the message you want to pass.
     */
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }
}
