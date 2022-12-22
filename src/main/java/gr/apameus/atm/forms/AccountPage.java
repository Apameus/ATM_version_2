package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;

import javax.swing.*;
import java.awt.*;

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
        creditCardManager = manager.getCreditCardManager();
        manager.addPanel(mainPanel, KEY);

        // buttons
        logoutButton.addActionListener(e -> {
            manager.showPanel(LoginPage.KEY);
        });
        // deposit
        depositButton.addActionListener(e -> {
            DepositPage depositPage = manager.getDepositPage();
            depositPage.refresh(creditCard);
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
            withdrawPage.refresh(creditCard);
            manager.showPanel(WithdrawPage.KEY);

        });
        // transfer
        transferButton.addActionListener(e -> {
            if (zeroBalanceCheck()){
                showError("Your balance is 0!");
                return;
            }
            TransferPage transferPage = manager.getTransferPage();
            transferPage.refresh(creditCard);
            manager.showPanel(TransferPage.KEY);
        });

    }

    /**
     * Checks if the credit-card balance is 0, so there is no reason to show the WithdrawPage
     * @return true if the credit-card balance is 0, or false if it's not
     */
    private Boolean zeroBalanceCheck() {
        if (creditCard.balance == 0){
            return true;
        }
        return false;
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
        // clear the info field
        infoText.setText("");
    }

    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }
}
