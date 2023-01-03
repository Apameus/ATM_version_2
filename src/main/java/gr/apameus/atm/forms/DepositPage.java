package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

import javax.swing.*;
import java.awt.*;

public class DepositPage {
    public static final String KEY = "DepositPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JLabel balanceText;
    private JTextField amountField;
    private JButton depositButton;
    private JLabel infoText;
    private JButton backButton;
    //
    CreditCardManager creditCardManager;

    // constructor
    public DepositPage(PanelManager manager){
        creditCardManager = manager.getCreditCardManager();
        manager.addPanel(mainPanel, KEY);

        // buttons
        backButton.addActionListener(e -> {
            clear();
            manager.showPanel(AccountPage.KEY);
        });

        depositButton.addActionListener(e -> {
            if (amountField.getText().isBlank()){
                showError("Amount must be specified!");
                return;
            }
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0){
                showError("Amount must be greater than 0!");
                amountField.setText("");
                return;
            }

            if (creditCardManager.deposit(amount)){
                clear();
                refresh();
                manager.getAccountPage().refresh();
                // successful msg
                infoText.setForeground(Color.green);
                infoText.setText("Deposit successful");
            }
            else {
                // show error
                showError("Invalid amount!");
                amountField.setText("");
            }

        });
    }

    /**
     * Clear the text-field & the label.
     */
    private void clear() {
        amountField.setText("");
        infoText.setText("");
    }

    /**
     * Sets the credit-card balance label according to the current credit-card info.
     */
    public void refresh(){
        balanceText.setText(String.valueOf(creditCardManager.getCurrent_balance()));
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
