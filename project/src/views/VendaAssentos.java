package src.views;

import src.controllers.ControladorAeroporto;
import src.controllers.ControladorVenda;
import src.models.*;

import javax.swing.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VendaAssentos {
    private final ControladorVenda controladorVenda;
    private Voo voosDesejado;

    JFrame frame;

    JList voosList = new JList();


    public VendaAssentos(ControladorVenda controladorVenda, Voo voo) {
        this.controladorVenda = controladorVenda;
        this.voosDesejado = voo;
        frame = new JFrame("Vender passagem");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUIComponents();
    }

    private void createUIComponents() {
        voosList.setBounds(125, 10, 300, 500);

        JPanel panel = new JPanel();
        panel.setLayout(null);

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

        JLabel assento = new JLabel("Assento");
        assento.setBounds(10, 290, 500, 30);

        ArrayList<String> assentosEconomica = this.voosDesejado.getAssentosDisponiveis("ECONÔMICA");
        ArrayList<String> assentosExecutiva = this.voosDesejado.getAssentosDisponiveis("EXECUTIVA");
        ArrayList<String> assentosPrimeiraClasse = this.voosDesejado.getAssentosDisponiveis("PRIMEIRA_CLASSE");


        // concatenate the arrays
        ArrayList<String> assentosDisponiveis = new ArrayList<>();
        assentosDisponiveis.addAll(assentosEconomica);
        assentosDisponiveis.addAll(assentosExecutiva);
        assentosDisponiveis.addAll(assentosPrimeiraClasse);


        String[] assentosDisponiveisArray = assentosDisponiveis.toArray(new String[0]);
        JComboBox assentoField = new JComboBox(assentosDisponiveisArray);
        assentoField.setBounds(10, 320, 500, 30);



        JButton vender = new JButton("Vender");
        vender.setBounds(10, 360, 500, 30);


        vender.addActionListener(e -> {
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


            String assentoPassageiro = assentoField.getSelectedItem().toString();
            if (assentoPassageiro.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Assento não pode ser vazio");
                return;
            }

            String classeAssento;
            // first two characters (EC, EX, PC)
            if (assentoPassageiro.substring(0, 2).equals("EC")) {
                classeAssento = "ECONÔMICA";
            } else if (assentoPassageiro.substring(0, 2).equals("EX")) {
                classeAssento = "EXECUTIVA";
            } else {
                classeAssento = "PRIMEIRA_CLASSE";
            }


            boolean resultadoVenda = false;

            Passageiro passageiro = this.controladorVenda.buscaPassageiro(nome, vat, tipoDocumento, dataNascimentoPassageiro);
            Desconto descontoZero = new Desconto(0f);

            if (passageiro == null || !passageiro.isFidelizado()) {
                resultadoVenda = this.controladorVenda.novaVenda(nome, dataNascimentoPassageiro, vat, tipoDocumento, this.voosDesejado, classeAssento, assentoPassageiro, descontoZero);
            } else {
                // Desconto padrão: 0%
                ArrayList<Desconto> descontos = new ArrayList<>();
                descontos.add(descontoZero);

                // se o passageiro é fidelizado, buscar descontos possíveis
                ArrayList<Desconto> descontosPassageiro = Desconto.possiveisDescontos(this.voosDesejado, passageiro.getMilhas());
                descontos.addAll(descontosPassageiro);

                // se não houver descontos possíveis, vender com desconto padrão
                if (descontos.size() == 1) {
                    System.out.println("passageiro é fidelizado, mas não há descontos possíveis");
                    resultadoVenda = this.controladorVenda.novaVenda(nome, dataNascimentoPassageiro, vat, tipoDocumento, this.voosDesejado, classeAssento, assentoPassageiro, descontoZero);
                } else {
                    // se houver descontos possíveis, perguntar qual o desconto
                    String[] descontosArray = new String[descontos.size()];
                    for (int i = 0; i < descontos.size(); i++) {
                        int porcentagem = (int) (descontos.get(i).getPorcentagem() * 100);
                        descontosArray[i] = porcentagem + "% de desconto";
                    }
                    JComboBox descontoField = new JComboBox(descontosArray);
                    descontoField.setBounds(10, 320, 500, 30);
                    JOptionPane.showMessageDialog(null, descontoField, "Desconto", JOptionPane.QUESTION_MESSAGE);
                    int index = descontoField.getSelectedIndex();

                    if (index != -1) {
                    Desconto desconto = descontos.get(index);
                    resultadoVenda = this.controladorVenda.novaVenda(nome, dataNascimentoPassageiro, vat, tipoDocumento, this.voosDesejado, classeAssento, assentoPassageiro, desconto);
                    }
                }
            }

            if (resultadoVenda) {
                JOptionPane.showMessageDialog(null, "Venda realizada com sucesso");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao realizar venda");
            }
        });


        panel.add(nomePassageiro);
        panel.add(nomePassageiroField);
        panel.add(vatPassageiro);
        panel.add(vatPassageiroField);
        panel.add(vatType);
        panel.add(vatTypeField);
        panel.add(dataNascimento);
        panel.add(dataNascimentoField);
        panel.add(assento);
        panel.add(assentoField);
        panel.add(vender);
        frame.add(panel);
    }
}
