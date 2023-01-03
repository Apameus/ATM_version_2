package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.creditCard.CreditCardManager;

import javax.swing.*;
import java.awt.*;

public class WithdrawPage {
    public static final String KEY = "WithdrawPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JLabel balanceText;
    private JTextField amountField;
    private JButton withdrawButton;
    private JLabel infoText;
    private JButton backButton;
    //
    private CreditCardManager creditCardManager;

    // constructor
    public WithdrawPage(PanelManager manager){
        creditCardManager = manager.getCreditCardManager();
        manager.addPanel(mainPanel, KEY);

        // buttons //
        // back
        backButton.addActionListener(e -> {
            clear();
            manager.showPanel(AccountPage.KEY);
        });
        // withdraw
        withdrawButton.addActionListener(e -> {
            // check
            if (amountField.getText().isBlank()){
                showError("Amount must be specified!");
                return;
            }
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0 || amount > creditCardManager.getCurrent_balance()){
                showError("Invalid amount!");
                amountField.setText("");
                return;
            }

            // check
            if (creditCardManager.withdraw(amount)){
                clear();
                refresh();
                manager.getAccountPage().refresh();
                // successful msg
                infoText.setForeground(Color.green);
                infoText.setText("Withdraw successful");
            }
            else {
                // show error
                showError("Invalid amount!");
                amountField.setText("");
            }
        });
    }

    /**
     * Sets the credit-card balance label according to the current credit-card info.
     */
    public void refresh(){
        balanceText.setText(String.valueOf(creditCardManager.getCurrent_balance()));
    }

    /**
     * Clear the text-field & the label.
     */
    private void clear() {
        amountField.setText("");
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
