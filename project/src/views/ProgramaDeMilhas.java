package src.views;

import src.controllers.ControladorProgramaFidelidade;
import src.models.Passageiro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProgramaDeMilhas {
    public ProgramaDeMilhas(ControladorProgramaFidelidade controladorProgramaFidelidade) {
        JFrame frame = new JFrame("Programa de Milhas");
        frame.setSize(500, 500);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        frame.add(panel);


        JTable table = new JTable();
        // table showing: nome do passageiro, numId, tipoNumId, dataNascimento, total de milhas acumuladas, é fidelizado?
        JScrollPane scrollPane = new JScrollPane(table);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nome do passageiro");
        model.addColumn("Número de identificação");
        model.addColumn("Tipo de número de identificação");
        model.addColumn("Data de nascimento");
        model.addColumn("Total de milhas acumuladas");
        model.addColumn("É fidelizado?");
        table.setModel(model);

//        disable editing
        table.setDefaultEditor(Object.class, null);


        ArrayList<Passageiro> passageiros = controladorProgramaFidelidade.getPassageiros();
        for (Passageiro passageiro : passageiros) {
//            if (passageiro.isFidelizado()) {
                String fidelizado = passageiro.isFidelizado() ? "Sim" : "Não";
                LocalDate dataNascimento = passageiro.getDataNascimento();
                String dataNascimentoString = dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                model.addRow(new Object[]{passageiro.getNomeCompleto(), passageiro.getNumId(), passageiro.getTipoNumId(), dataNascimentoString, passageiro.getMilhasTotais(), fidelizado});
//            }
        }

        JButton button = new JButton("Voltar");
        panel.add(button);
        button.addActionListener(e -> {
            frame.dispose();
        });

        JButton button2 = new JButton("Fidelizar novo passageiro");
        panel.add(button2);

        button2.addActionListener(e -> {

            // open new dialog containg all the fields
            JFrame frame2 = new JFrame("Fidelizar novo passageiro");
            frame2.setSize(550, 400);
            frame2.setVisible(true);

            JPanel panel2 = new JPanel();
            frame2.add(panel2);
            panel2.setLayout(null);

            JLabel nomePassageiro = new JLabel("Nome do passageiro");
            nomePassageiro.setBounds(10, 10, 500, 30);

            JTextField nomePassageiroField = new JTextField();
            nomePassageiroField.setBounds(10, 40, 500, 30);

            JLabel vatPassageiro = new JLabel("Documento do passageiro");
            vatPassageiro.setBounds(10, 80, 500, 30);

            JTextField vatPassageiroField = new JTextField();
            vatPassageiroField.setBounds(10, 110, 500, 30);

            JLabel vatType = new JLabel("Tipo de documento");
            vatType.setBounds(10, 150, 500, 30);

            String[] tiposDeDocumento = {"RG", "CPF", "Passaporte"};
            JComboBox vatTypeField = new JComboBox(tiposDeDocumento);
            vatTypeField.setBounds(10, 180, 500, 30);

            JLabel dataNascimento = new JLabel("Data de nascimento");
            dataNascimento.setBounds(10, 220, 500, 30);

            JTextField dataNascimentoField = new JTextField();
            dataNascimentoField.setBounds(10, 250, 500, 30);

            JButton fidelizar = new JButton("Fidelizar");
            fidelizar.setBounds(10, 290, 500, 30);


            fidelizar.addActionListener(e2 -> {
                String nome = nomePassageiroField.getText();
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome do passageiro não pode ser vazio");
                    return;
                }
                String vat = vatPassageiroField.getText();
                if (vat.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Documento do passageiro não pode ser vazio");
                    return;
                }
                String tipoDocumento = vatTypeField.getSelectedItem().toString();
                if (tipoDocumento.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tipo de documento do passageiro não pode ser vazio");
                    return;
                }
                if (tipoDocumento.equals("CPF")) {
                    if (!vat.matches("[0-9]{11}")) {
                        JOptionPane.showMessageDialog(null, "CPF deve conter 11 dígitos");
                        return;
                    }
                } else if (tipoDocumento.equals("RG")) {
                    if (!vat.matches("[0-9]{9}")) {
                        JOptionPane.showMessageDialog(null, "RG deve conter 9 dígitos");
                        return;
                    }
                } else if (tipoDocumento.equals("Passaporte")) {
                    if (!vat.matches("[0-9]{9}")) {
                        JOptionPane.showMessageDialog(null, "Passaporte deve conter 9 dígitos");
                        return;
                    }
                }

                LocalDate dataNascimentoPassageiro;
                try {
                    dataNascimentoPassageiro = LocalDate.parse(dataNascimentoField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (dataNascimentoPassageiro.isAfter(LocalDate.now())) {
                        JOptionPane.showMessageDialog(null, "Data de nascimento não pode ser no futuro");
                        return;
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Data de nascimento inválida");
                    return;
                }

                boolean fidelizou = controladorProgramaFidelidade.fidelizar(nome, vat, tipoDocumento, dataNascimentoPassageiro);
                if (fidelizou) {
                    JOptionPane.showMessageDialog(null, "Fidelizado com sucesso!");
                    frame2.dispose();
                    frame.dispose();
                    new ProgramaDeMilhas(controladorProgramaFidelidade);
                } else {
                    if (dataNascimentoPassageiro.isAfter(LocalDate.now().minusYears(18))) {
                        JOptionPane.showMessageDialog(null, "Passageiro não pode ser menor de idade");
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Não foi possível fidelizar!");
                }
            });

            panel2.add(nomePassageiro);
            panel2.add(nomePassageiroField);
            panel2.add(vatPassageiro);
            panel2.add(vatPassageiroField);
            panel2.add(vatType);
            panel2.add(vatTypeField);
            panel2.add(dataNascimento);
            panel2.add(dataNascimentoField);
            panel2.add(fidelizar);
        });



        JButton button3 = new JButton("Fidelizar atual");
        panel.add(button3);

        button3.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            String nomeCompleto = (String) table.getValueAt(selectedRow, 0);
            String numId = (String) table.getValueAt(selectedRow, 1);
            String tipoNumId = (String) table.getValueAt(selectedRow, 2);
            String dataNascimento = (String) table.getValueAt(selectedRow, 3);
            LocalDate parsedDataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println(String.format("nomeCompleto: %s, numId: %s, tipoNumId: %s, dataNascimento: %s", nomeCompleto, numId, tipoNumId, dataNascimento));
            boolean fidelizou = controladorProgramaFidelidade.fidelizar(nomeCompleto, numId, tipoNumId, parsedDataNascimento);
            if (fidelizou) {
                JOptionPane.showMessageDialog(null, "Fidelizado com sucesso!");
                frame.dispose();
                new ProgramaDeMilhas(controladorProgramaFidelidade);
            } else {
                if (parsedDataNascimento.isAfter(LocalDate.now().minusYears(18))) {
                    JOptionPane.showMessageDialog(null, "Passageiro não pode ser menor de idade");
                    return;
                }
                JOptionPane.showMessageDialog(null, "Não foi possível fidelizar!");
            }
        });


        JButton button4 = new JButton("Desfidelizar atual");
        panel.add(button4);

        button4.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            String nomeCompleto = (String) table.getValueAt(selectedRow, 0);
            String numId = (String) table.getValueAt(selectedRow, 1);
            String tipoNumId = (String) table.getValueAt(selectedRow, 2);
            String dataNascimento = (String) table.getValueAt(selectedRow, 3);
            LocalDate parsedDataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            boolean desfidelizou = controladorProgramaFidelidade.tirarFidelizacao(nomeCompleto, numId, tipoNumId, parsedDataNascimento);

            if (desfidelizou) {
                JOptionPane.showMessageDialog(null, "Desfidelizado com sucesso!");
                frame.dispose();
                new ProgramaDeMilhas(controladorProgramaFidelidade);
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível desfidelizar!");
            }
        });




        panel.add(scrollPane);

    }
}
