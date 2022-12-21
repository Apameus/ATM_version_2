package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;
import gr.apameus.atm.account.CreditCardManager;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    private static final String KEY = "LoginPage";
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
        cardManager = new CreditCardManager();
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
        if (cardManager.login(creditNumber,creditPin) == null){
            showError("Invalid username or password!");
            return;
        }
        manager.showPanel(AccountPage.KEY);
    }

    // checks if both fields are specified
    private Boolean blankCheck(String creditNumber, String creditPin) {
        if (creditNumber.isBlank() || creditPin.isBlank()){
            showError("Both fields must be specified!");
            return false;
        }
        return true;
    }

    // passing the error msg to the infoLabel
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }


}
