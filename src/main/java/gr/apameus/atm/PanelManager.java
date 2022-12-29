package gr.apameus.atm;

import gr.apameus.atm.creditCard.CreditCardManager;
import gr.apameus.atm.forms.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelManager {
    JFrame frame;
    JPanel mainPanel;
    CardLayout layout;

    LoginPage loginPage ;
    AccountPage accountPage ;
    DepositPage depositPage;
    WithdrawPage withdrawPage;
    TransferPage transferPage;
    CreditCardManager creditCardManager;

    // constructor
    public PanelManager() throws IOException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        frame.add(mainPanel);

        creditCardManager = new CreditCardManager();
        loginPage = new LoginPage(this);
        accountPage = new AccountPage(this);
        depositPage = new DepositPage(this);
        withdrawPage = new WithdrawPage(this);
        transferPage = new TransferPage(this);

    }

    // getters
    public CreditCardManager getCreditCardManager(){
        return creditCardManager;
    }
    public TransferPage getTransferPage() {
        return transferPage;
    }

    public WithdrawPage getWithdrawPage() {
        return withdrawPage;
    }

    public DepositPage getDepositPage() {
        return depositPage;
    }

    public AccountPage getAccountPage() {
        return accountPage;
    }

    // panel & frame methods
    /**
     * Adding the new panel to the main panel
     * @param panel the new panel
     * @param key the key of the new panel
     */
    public void addPanel(JPanel panel, String key){
        mainPanel.add(panel, key);
    }

    /**
     * Showing the specific panel of the main panel
     * @param key the specific panel key
     */
    public void showPanel(String key){
        layout.show(mainPanel, key);
    }

    /**
     * Showing the main frame
     */
    public void showFrame(){
        frame.setSize(700,700);
        frame.setVisible(true);
    }

}
