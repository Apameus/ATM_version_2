package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.creditCard.CreditCard;
import gr.apameus.atm.creditCard.CreditCardManager;

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
            if (!blankCheck(creditNumber, creditPin)) {
                login(creditNumber, creditPin, manager);
            }
        });
        // registerButton
        registerButton.addActionListener(e -> {
            String creditNumber = creditNumberField.getText();
            String creditPin = new String(creditPinField.getPassword());
            // checks
            if (!blankCheck(creditNumber, creditPin)) {
                register(creditNumber, creditPin);
            }
        });
    }

    /**
     * Saves the new credit-card & makes the necessary checks.
     * @param creditNumber the credit-card number passed from the text field.
     * @param creditPin the credit-card pin passed from the text field.
     */
    private void register(String creditNumber, String creditPin) {
        // checks
        if (!cardManager.register(creditNumber, creditPin)){
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

    /**
     * Showing the Account Page after making all the necessary checks.
     * @param creditNumber the credit-card number passed from the text field.
     * @param creditPin the credit-card pin passed from the text field.
     * @param manager the PanelManager we are currently using.
     */
    private void login(String creditNumber, String creditPin, PanelManager manager) {
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
            return true;
        }
        return false;
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
