package gr.apameus.atm.forms;

import gr.apameus.atm.PanelManager;

import javax.swing.*;

public class TransferPage {
    public static final String KEY = "TransferPage";
    private JPanel mainPanel;
    private JPanel panel;
    private JTextField textField1;
    private JLabel infoText;
    private JButton backButton;
    private JTextField amountField;
    private JButton transferButton;


    public TransferPage(PanelManager manager){
        manager.addPanel(mainPanel, KEY);

        // buttons
        backButton.addActionListener(e -> {
            clear();
            manager.showPanel(AccountPage.KEY);
        });

        transferButton.addActionListener(e -> {});
    }

    private void clear() {
        amountField.setText("");
        infoText.setText("");
    }
}
