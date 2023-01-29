package src.views;

import src.controllers.ControladorAeroporto;
import src.models.Voo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConfirmarDecolagem {

    public ConfirmarDecolagem(ControladorAeroporto controladorAeroporto) {
        JFrame frame = new JFrame("Confirmar Decolagem");

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 530, 450);

        panel.add(scrollPane);

        frame.add(panel);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Origem");
        model.addColumn("Destino");
        model.addColumn("Horário Previsto");
        model.addColumn("Horário Decolagem");
        table.setModel(model);

        // display all flights

        ArrayList<Voo> voos = controladorAeroporto.getVoos();
        // sort by horarioPrevisto
        voos.sort((voo1, voo2) -> voo1.getHorarioPrevisto().compareTo(voo2.getHorarioPrevisto()));

        voos.forEach(voo -> {
            model.addRow(new Object[]{
                    voo.getAeroportoOrigem(),
                    voo.getAeroportoDestino(),
                    voo.getHorarioPrevisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    voo.getHorarioPartida() != null ? voo.getHorarioPartida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "",
            });
        });

        // listen on click, open dialog to confirm decolagem. user must enter horarioDecolagem

        // disable editing
        table.setDefaultEditor(Object.class, null);
        table.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    // open frame to confirm decolagem. Should contain a text field to enter horarioDecolagem and
                    // a button to confirm
                    Voo voo = voos.get(row);
                    if (voo.getHorarioPartida() != null) {
                        JOptionPane.showMessageDialog(null, "Voo já decolou");
                        return;
                    }

                    JFrame confirmarDecolagemFrame = new JFrame("Confirmar Decolagem");
                    JPanel confirmarDecolagemPanel = new JPanel();
                    confirmarDecolagemPanel.setLayout(null);

                    JLabel horarioDecolagemLabel = new JLabel("Horário Decolagem");
                    horarioDecolagemLabel.setBounds(10, 10, 100, 20);
                    confirmarDecolagemPanel.add(horarioDecolagemLabel);

                    JTextField horarioDecolagemTextField = new JTextField();
                    horarioDecolagemTextField.setBounds(120, 10, 100, 20);
                    confirmarDecolagemPanel.add(horarioDecolagemTextField);

                    JButton confirmarButton = new JButton("Confirmar");
                    confirmarButton.setBounds(10, 40, 100, 20);

                    confirmarButton.addActionListener(e -> {
                        String horarioDecolagem = horarioDecolagemTextField.getText();
                        try {
                            LocalTime horarioDecolagemTime = LocalTime.parse(horarioDecolagem);
                            controladorAeroporto.atestarDecolagem(voo, horarioDecolagemTime);
                            JOptionPane.showMessageDialog(null, "Decolagem confirmada");
                            confirmarDecolagemFrame.dispose();
                            frame.dispose();
                            new ConfirmarDecolagem(controladorAeroporto);
                        }
                        catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Horário inválido");
                        }

                    });

                    confirmarDecolagemPanel.add(confirmarButton);

                    confirmarDecolagemFrame.add(confirmarDecolagemPanel);
                    confirmarDecolagemFrame.setSize(250, 150);
                    confirmarDecolagemFrame.setVisible(true);
                }
            }
        });
        frame.setSize(550, 500);
        frame.setVisible(true);

    }

}
