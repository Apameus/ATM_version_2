package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;

import javax.swing.*;
import java.awt.*;

public class TransferPage {
    public static final String KEY = "TransferPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JTextField transferToField;
    private JLabel infoText;
    private JButton backButton;
    private JTextField amountField;
    private JButton transferButton;
    private JLabel balanceText;
    //
    CreditCardManager creditCardManager;


    public TransferPage(PanelManager manager){
        creditCardManager = manager.getCreditCardManager();
        manager.addPanel(mainPanel, KEY);

        // buttons
        backButton.addActionListener(e -> {
            clear();
            manager.getAccountPage().refresh();
            manager.showPanel(AccountPage.KEY);
        });

        transferButton.addActionListener(e -> {
            if (transferToField.getText().isBlank() || amountField.getText().isBlank()){
                showError("Amount must be specified!");
                return;
            }
            String transferTo = transferToField.getText();
            Double amount = Double.valueOf(amountField.getText());
            creditCardManager.transfer(manager.getAccountPage().getCreditCard(), transferTo, amount);
            refresh(manager.getAccountPage().getCreditCard());
            clear();
            // successful msg
            infoText.setForeground(Color.green);
            infoText.setText("Transfer successful");
        });
    }

    private void clear() {
        amountField.setText("");
        transferToField.setText("");
        infoText.setText("");
    }

    public void refresh(CreditCard card){
        balanceText.setText(card.balance.toString());
    }

    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }
}
