package src.views;

import src.controllers.ControladorAeroporto;
import src.controllers.ControladorVenda;
import src.models.Voo;

import javax.swing.*;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VendaDeTiquetes {
    private final ControladorVenda controladorVenda;
    private ArrayList<Voo> voosDisponiveis;

    JFrame frame;

    JList voosList = new JList();


    public VendaDeTiquetes(ControladorVenda controladorVenda) {
        this.controladorVenda = controladorVenda;
        this.voosDisponiveis = new ArrayList<>();
        frame = new JFrame("Vender passagem");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUIComponents();
    }

    private void createUIComponents() {
//        voosList.setBounds(125, 40, 300, 500);
        JScrollPane scrollPane = new JScrollPane(voosList);
        scrollPane.setBounds(125, 40, 300, 500);

        
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

//        JLabel idaEVoltaLabel = new JLabel("Ida e volta");
//        idaEVoltaLabel.setBounds(10, 150, 100, 30);
//        panel.add(idaEVoltaLabel);

//        JCheckBox idaEVoltaCheckbox = new JCheckBox();
//        idaEVoltaCheckbox.setBounds(10, 180, 100, 30);
//        panel.add(idaEVoltaCheckbox);

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

        // call this.mostrarVoo if some element is double clicked
        voosList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = voosList.locationToIndex(evt.getPoint());
                    mostrarVoo(voosDisponiveis.get(index));
                }
            }
        });

        frame.add(panel);
    }

    private void mostrarVoo(Voo voo) {
        VendaAssentos vendaAssentos = new VendaAssentos(controladorVenda, voo);
    }

    private void buscarVoos(String origem, String destino, boolean isIdaVolta) {
        ArrayList<Voo> voos = controladorVenda.getVoosDisponiveis(origem, destino, isIdaVolta);
        this.voosDisponiveis = voos;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Voo voo : voos) {
            String dateBRL = voo.getHorarioPrevisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String origemName = voo.getAeroportoOrigem();
            String destinoName = voo.getAeroportoDestino();
            String vooString = String.format("Origem: %s, Destino: %s, Data: %s", origemName, destinoName, dateBRL);
            listModel.addElement(vooString);
        }
        voosList.setListData(listModel.toArray());
    }



}
