package src.views;

import src.controllers.ControladorAeroporto;
import src.controllers.ControladorVenda;

import javax.swing.*;
import java.awt.*;

public class Main {
    private final ControladorAeroporto controladorAeroporto;

    private final ControladorVenda controladorVenda;
    private final JFrame frame;

    public Main(ControladorAeroporto controladorAeroporto, ControladorVenda controladorVenda) {
        this.controladorAeroporto = controladorAeroporto;
        this.controladorVenda = controladorVenda;
        frame = new JFrame("Sistema de aeroporto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        createUIComponents();
        setWindowsStyle();
    }

    private void createUIComponents() {


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton createFlightButton = new JButton("Cadastrar voo");
        createFlightButton.addActionListener(e -> new RegistradorDeVoos(controladorAeroporto));
        createFlightButton.setPreferredSize(new Dimension(200, 30));

        JButton ticketSaleButton = new JButton("Vender passagem");
        ticketSaleButton.addActionListener(e -> new VendaDeTiquetes(controladorVenda));
        ticketSaleButton.setPreferredSize(new Dimension(200, 30));


        panel.add(createFlightButton);
        panel.add(ticketSaleButton);

        frame.add(panel);
    }

    private void setWindowsStyle() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
