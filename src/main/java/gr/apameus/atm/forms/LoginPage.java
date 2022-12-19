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

    // constructor
    public LoginPage(PanelManager manager){
        manager.addPanel(mainPanel, KEY);
        String creditNumber = creditNumberField.getText();
        String creditPin = new String(creditPinField.getPassword());
        // loginButton
        loginButton.addActionListener(e -> {
            blankCheck(creditNumber, creditPin);
            correctInfoCheck(creditNumber, creditPin, manager);

        });
        // registerButton
        registerButton.addActionListener(e -> {
            blankCheck(creditNumber, creditPin);
            alreadyExistCheck(creditNumber);
        });
    }

    private void correctInfoCheck(String creditNumber, String creditPin, PanelManager manager) {
        if (CreditCardManager.getCreditCardsInfo().containsKey(creditNumber)){
            if (CreditCardManager.getCreditCardsInfo().get(creditNumber).equals(creditPin)){
                manager.showPanel("AccountPage");
            }
            else {
                showError("Wrong Password !");
            }
        }
    }

    private void alreadyExistCheck(String creditNumber) {
        if (CreditCardManager.getCreditCardsInfo().isEmpty()){
            return;
        }
        if (CreditCardManager.getCreditCardsInfo().containsKey(creditNumber)){
            showError("Credit card number already exist!");
        }

    }

    // checks if both fields are specified
    private void blankCheck(String creditNumber, String creditPin) {
        if (creditNumber.isBlank() || creditPin.isBlank()){
            showError("Both fields must be specified!");

        }
    }

    // passing to the infoLabel the error msg
    private void showError(String msg) {
        infoText.setForeground(Color.red);
        infoText.setText(msg);
    }


}
