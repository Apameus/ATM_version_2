package gr.apameus.atm;

import gr.apameus.atm.account.CreditCard;
import gr.apameus.atm.account.CreditCardManager;
import gr.apameus.atm.forms.*;

import javax.swing.*;
import java.awt.*;

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

    public PanelManager(){
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

    public void addPanel(JPanel panel, String KEY){
        mainPanel.add(panel, KEY);
    }

    public void showPanel(String KEY){
        layout.show(mainPanel, KEY);
    }

    public void showFrame(){
        frame.setSize(700,700);
        frame.setVisible(true);
    }

}
