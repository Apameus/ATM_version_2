package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;

import javax.swing.*;

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
    private JLabel creditcardNumberText;
    private JLabel balanceText;



    public AccountPage(PanelManager manager){
        manager.addPanel(mainPanel, KEY);

        // show the creditCardNumber
        creditcardNumberText.setText("");
        // show the creditCardBalance
        balanceText.setText("");
    }
}
