package gr.apameus.atm;

import gr.apameus.atm.account.CreditCardManager;
import gr.apameus.atm.forms.AccountPage;
import gr.apameus.atm.forms.LoginPage;

import javax.swing.*;
import java.awt.*;

public class PanelManager {
    JFrame frame;
    JPanel mainPanel;
    CardLayout layout;

    public PanelManager(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        frame.add(mainPanel);

        LoginPage loginPage = new LoginPage(this);
        AccountPage accountPage = new AccountPage(this);

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
