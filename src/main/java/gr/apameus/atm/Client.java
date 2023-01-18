package gr.apameus.atm;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        PanelManager panelManager = new PanelManager();
        panelManager.showFrame();
    }
}