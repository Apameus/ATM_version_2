package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    public static final String KEY = "LoginPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JTextField creditNumberField;
    private JPasswordField creditPinField;
    private JLabel infoText;
    private JButton loginButton;
    private JButton registerButton;

    private CreditCardManager cardManager;

    // constructor
    public LoginPage(PanelManager manager){
        cardManager = manager.getCreditCardManager();
        manager.addPanel(mainPanel, KEY);

        // loginButton
        loginButton.addActionListener(e -> {
            String creditNumber = creditNumberField.getText();
            String creditPin = new String(creditPinField.getPassword());
            // checks
            if (blankCheck(creditNumber, creditPin) == true) {
                correctInfoCheck(creditNumber, creditPin, manager);
            }

        });
        // registerButton
        registerButton.addActionListener(e -> {
            String creditNumber = creditNumberField.getText();
            String creditPin = new String(creditPinField.getPassword());
            // checks
            if (blankCheck(creditNumber, creditPin) == true) {
                register(creditNumber, creditPin);
            }
        });
    }

    private void register(String creditNumber, String creditPin) {
        if (cardManager.register(creditNumber, creditPin) == false){
            showError("CreditCard number already exist!");
            return;
        }
        // successful msg
        infoText.setForeground(Color.green);
        infoText.setText("Register successful");
        // empty the textFields
        creditNumberField.setText("");
        creditPinField.setText("");
    }

    private void correctInfoCheck(String creditNumber, String creditPin, PanelManager manager) {
        CreditCard creditCard = cardManager.login(creditNumber, creditPin);
        if (creditCard == null){
            showError("Invalid username or password!");
            return;
        }
        // clear the fields
        clear();
        // get the specific account page
        AccountPage accountPage = manager.getAccountPage();
        // set the current credit-card
        accountPage.setCreditCard(creditCard);
        // set the labels
        accountPage.refresh();
        // show Account Page
        manager.showPanel(AccountPage.KEY);
    }

    /**
     * Checks if both fields are specified
     */
    private Boolean blankCheck(String creditNumber, String creditPin) {
        if (creditNumber.isBlank() || creditPin.isBlank()){
            showError("Both fields must be specified!");
            return false;
        }
        return true;
    }

    /**
     * Passing the error message to the infoLabel
     * @param msg the error message
     */
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }

    /**
     * Clear all the fields & labels
     */
    private void clear(){
        creditNumberField.setText("");
        creditPinField.setText("");
        infoText.setText("");
    }
}
