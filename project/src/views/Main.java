package src.views;

import src.controllers.ControladorAeroporto;
import src.controllers.ControladorProgramaFidelidade;
import src.controllers.ControladorVenda;

import javax.swing.*;
import java.awt.*;

public class Main {
    private final ControladorAeroporto controladorAeroporto;
    private final ControladorVenda controladorVenda;
    private final ControladorProgramaFidelidade controladorProgramaFidelidade;
    private final JFrame frame;

    public Main(ControladorAeroporto controladorAeroporto, ControladorVenda controladorVenda, ControladorProgramaFidelidade controladorProgramaFidelidade) {
        this.controladorAeroporto = controladorAeroporto;
        this.controladorVenda = controladorVenda;
        this.controladorProgramaFidelidade = controladorProgramaFidelidade;
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
        panel.setLayout(new GridLayout(2, 2));

        JButton createFlightButton = new JButton("Cadastrar voo");
        createFlightButton.addActionListener(e -> new RegistradorDeVoos(controladorAeroporto));
        createFlightButton.setPreferredSize(new Dimension(200, 30));

        JButton ticketSaleButton = new JButton("Vender passagem");
        ticketSaleButton.addActionListener(e -> new VendaDeTiquetes(controladorVenda));
        ticketSaleButton.setPreferredSize(new Dimension(200, 30));

        JButton mileageProgramButton = new JButton("Programa de Milhas");
        mileageProgramButton.addActionListener(e -> new ProgramaDeMilhas(controladorProgramaFidelidade));
        mileageProgramButton.setPreferredSize(new Dimension(200, 30));

        JButton confirmarVooButton = new JButton("Confirmar decolagem");
        confirmarVooButton.addActionListener(e -> new ConfirmarDecolagem(controladorAeroporto));
        confirmarVooButton.setPreferredSize(new Dimension(200, 30));



        panel.add(createFlightButton);
        panel.add(ticketSaleButton);
        panel.add(mileageProgramButton);
        panel.add(confirmarVooButton);

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
