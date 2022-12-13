package src.views;

import src.controllers.AirportController;

import javax.swing.*;
import java.awt.*;

public class Main {
    private final AirportController airportController;
    private final JFrame frame;

    public Main(AirportController airportController) {
        this.airportController = airportController;
        frame = new JFrame("Sistema de aeroporto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        createUIComponents();
    }

    private void createUIComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton button = new JButton("Cadastrar voo");
        button.addActionListener(e -> new FlightRegister(airportController));

        panel.add(button);

        frame.add(panel);
    }

}
