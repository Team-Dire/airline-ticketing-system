package src.views;

import src.controllers.ControladorAeroporto;
import src.controllers.ControladorVenda;
import src.models.Voo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VendaDeTiquetes {
    private final ControladorVenda controladorVenda;
    private ArrayList<Voo> voosDisponiveis;
    JFrame frame;
    JTable voosTable = new JTable();


    public VendaDeTiquetes(ControladorVenda controladorVenda) {
        this.controladorVenda = controladorVenda;
        this.voosDisponiveis = new ArrayList<>();
        frame = new JFrame("Vender passagem");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUIComponents();
    }

    private void createUIComponents() {
        JScrollPane scrollPane = new JScrollPane(voosTable);
        scrollPane.setBounds(125, 40, 400, 500);

        
        this.buscarVoos("", "", false);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel origemLabel = new JLabel("Origem");
        origemLabel.setBounds(10, 10, 500, 30);
        panel.add(origemLabel);

        JTextField origemField = new JTextField();
        origemField.setBounds(10, 40, 100, 30);
        panel.add(origemField);

        JLabel destinoLabel = new JLabel("Destino");
        destinoLabel.setBounds(10, 80, 100, 30);
        panel.add(destinoLabel);

        JTextField destinoField = new JTextField();
        destinoField.setBounds(10, 110, 100, 30);
        panel.add(destinoField);

        JButton buscarVoosButton = new JButton("Buscar voos");
        buscarVoosButton.setBounds(10, 150, 100, 30);
        buscarVoosButton.addActionListener(e -> {
                    String origem = origemField.getText();
                    String destino = destinoField.getText();
//                    boolean isIdaVolta = idaEVoltaCheckbox.isSelected();
                    boolean isIdaVolta = false;
                    this.buscarVoos(origem, destino, isIdaVolta);
                }
        );
        panel.add(buscarVoosButton);

        JLabel voosLabel = new JLabel("Voos");
        voosLabel.setBounds(125, 10, 100, 30);
        panel.add(voosLabel);

        panel.add(scrollPane);


        voosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = voosTable.rowAtPoint(evt.getPoint());
                int col = voosTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    Voo voo = voosDisponiveis.get(row);
                    mostrarVoo(voo);
                }
            }

        });

        voosTable.setDefaultEditor(Object.class, null);

        frame.add(panel);
    }
    private void mostrarVoo(Voo voo) {
        VendaAssentos vendaAssentos = new VendaAssentos(controladorVenda, voo);
    }
    private void buscarVoos(String origem, String destino, boolean isIdaVolta) {
        ArrayList<Voo> voos = controladorVenda.getVoosDisponiveis(origem, destino, isIdaVolta);
        this.voosDisponiveis = voos;
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Origem");
        model.addColumn("Destino");
        model.addColumn("Distância (km)");
        model.addColumn("Data");

        voosTable.setModel(model);


        for (Voo voo : voos) {
            String dateBRL = voo.getHorarioPrevisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String origemName = voo.getAeroportoOrigem();
            String destinoName = voo.getAeroportoDestino();
            int distancia = voo.getDistanciaEmKm();
            model.addRow(new Object[]{origemName, destinoName, distancia, dateBRL});
        }

    }
}
