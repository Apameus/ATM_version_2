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

    public PanelManager(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        frame.add(mainPanel);

        loginPage = new LoginPage(this);
        accountPage = new AccountPage(this);
        depositPage = new DepositPage(this);
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
