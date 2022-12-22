package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;

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
    //

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
            Double amount = Double.valueOf(amountField.getText());

            if (creditCardManager.deposit(manager.getAccountPage().getCreditCard(), amount)){
                clear();
                refresh(manager.getAccountPage().getCreditCard());
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

    private void clear() {
        amountField.setText("");
        infoText.setText("");
    }

    public void refresh(CreditCard card){
        balanceText.setText(String.valueOf(card.balance));
    }
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }
}
