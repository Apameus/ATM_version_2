package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

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

        // buttons //
        // back
        backButton.addActionListener(e -> {
            clear();
            manager.getAccountPage().refresh();
            manager.showPanel(AccountPage.KEY);
        });
        // transfer
        transferButton.addActionListener(e -> {
            // check
            if (transferToField.getText().isBlank() || amountField.getText().isBlank()){
                showError("Amount must be specified!");
                return;
            }
            String transferTo = transferToField.getText();
            Double amount = Double.valueOf(amountField.getText());
            creditCardManager.transfer(transferTo, amount);
            refresh();
            clear();
            // successful msg
            infoText.setForeground(Color.green);
            infoText.setText("Transfer successful");
        });
    }
    /**
     * Clear all the fields & labels.
     */
    private void clear() {
        amountField.setText("");
        transferToField.setText("");
        infoText.setText("");
    }
    /**
     * <b>Sets the</b> credit-card balance <b>label</b> according to the current credit-card info.
     */
    public void refresh(){
        balanceText.setText(String.valueOf(creditCardManager.getCurrent_balance()));
    }

    /*
     * Setting the message you passed in the info text with red color.
     * @param msg the message you want to pass.
     */
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }
}
