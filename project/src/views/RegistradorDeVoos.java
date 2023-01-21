package src.views;

import src.controllers.ControladorAeroporto;
import src.utils.types.Recorrencias;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class RegistradorDeVoos {
    private final ControladorAeroporto controladorAeroporto;
    private final JFrame frame;
    private JTextField originAirportField;
    private JTextField destinationAirportField;
    private JTextField airplaneField;
    private JTextField departureDateField;
    private JTextField departureTimeField;
    private JComboBox<?> recurringComboBox;

    public RegistradorDeVoos(ControladorAeroporto controladorAeroporto) {
        this.controladorAeroporto = controladorAeroporto;
        frame = new JFrame("Cadastrar voo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUIComponents();
    }

    private void createUIComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));




        JLabel originAirportLabel = new JLabel("Aeroporto de origem");
        JLabel destinationAirportLabel = new JLabel("Aeroporto de destino");
        JLabel airplaneLabel = new JLabel("Avião");
        JLabel departureDateLabel = new JLabel("Data de partida prevista (Ex: 13/01/2000)");
        JLabel departureTimeLabel = new JLabel("Hora de partida prevista (Ex: 13:00)");
        JLabel recurringLabel = new JLabel("Recorrência");


        originAirportField = new JTextField();
        originAirportField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isLowerCase(c)) {
                    String s = ("" + c).toUpperCase();
                    c = s.charAt(0);
                    evt.setKeyChar(c);
                }
                if (originAirportField.getText().length() > 3 || !Character.isLetter(c)) {
                    evt.consume();
                }
            }
        });



        destinationAirportField = new JTextField();

        destinationAirportField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isLowerCase(c)) {
                    String s = ("" + c).toUpperCase();
                    c = s.charAt(0);
                    evt.setKeyChar(c);
                }
                if (destinationAirportField.getText().length() > 3 || !Character.isLetter(c)) {
                    evt.consume();
                }
            }
        });


        airplaneField = new JTextField();

        departureDateField = new JTextField();
        departureDateField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (departureDateField.getText().length() > 9) {
                    evt.consume();
                }
            }
        });

        departureTimeField = new JTextField();
        // Regex for HH:DD. If not, consume the key
        departureTimeField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                String text = textField.getText();
                return text.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
            }
        });
        departureTimeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                String text = departureTimeField.getText();
                boolean isDigit = Character.isDigit(c);
                boolean isColon = c == ':';
                int length = text.length();

                // RULES:
                boolean invalidLength = length > 4;
                boolean invalidChar = !isDigit && !isColon;
                // 00:00 to 23:59
                boolean invalidHour = length == 2 && Integer.parseInt(text) > 23;
                boolean invalidMinute = length == 5 && Integer.parseInt(text.substring(3)) > 59;
                boolean invalidAlphaPosition = length == 2 && isDigit;
                boolean invalidColonPosition = length != 2 && isColon;

                if (invalidLength || invalidChar || invalidHour || invalidMinute || invalidAlphaPosition || invalidColonPosition) {
                    evt.consume();
                }

            }
        });


        String[] recurrenceValues = {"Diário", "Semanal", "Mensal", "Único"};
        recurringComboBox = new JComboBox<>(recurrenceValues);

        JButton registerButton = new JButton("Cadastrar");
        registerButton.addActionListener(e -> {
            String originAirport = originAirportField.getText();
            String destinationAirport = destinationAirportField.getText();
            String airplane = airplaneField.getText();

            String recurrenceValue = Objects.requireNonNull(recurringComboBox.getSelectedItem()).toString();
            Recorrencias recurring;
            switch (recurrenceValue) {
                case "Diário" -> recurring = Recorrencias.DIÁRIO;
                case "Semanal" -> recurring = Recorrencias.SEMANAL;
                case "Mensal" -> recurring = Recorrencias.MENSAL;
                default -> recurring = Recorrencias.ÚNICO;
            }

            LocalDateTime departureDateTime;

            try {
                // parse from brazilian date format to LocalDate (Ex: 13/01/2000 to 2000-01-13)
                LocalDate departureDate = LocalDate.parse(departureDateField.getText(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // parse from brazilian time format to LocalTime (Ex: 13:00 to 13:00)
                LocalTime departureTime = LocalTime.parse(departureTimeField.getText(), java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                departureDateTime = LocalDateTime.of(departureDate, departureTime);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Data ou hora inválida");
                return;
            }
            if (originAirport.length() < 3 || destinationAirport.length() < 3 || originAirport.length() > 4 || destinationAirport.length() > 4) {
                JOptionPane.showMessageDialog(null, "Código de aeroporto inválido");
                return;
            }

            if (originAirport.equals(destinationAirport)) {
                JOptionPane.showMessageDialog(null, "Aeroporto de origem e destino não podem ser iguais");
                return;
            }
            
            String msg = controladorAeroporto.novoAgendamentoDeVoo(originAirport, destinationAirport, airplane, departureDateTime, recurring, 68, 16, 16);
            if (msg.equals("Sucesso")) {
                JOptionPane.showMessageDialog(null, "Voo cadastrado com sucesso");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, msg);
            }
        });
        panel.add(originAirportLabel);
        panel.add(originAirportField);
        panel.add(destinationAirportLabel);
        panel.add(destinationAirportField);
        panel.add(airplaneLabel);
        panel.add(airplaneField);
        panel.add(departureDateLabel);
        panel.add(departureDateField);
        panel.add(departureTimeLabel);
        panel.add(departureTimeField);
        panel.add(recurringLabel);
        panel.add(recurringComboBox);
        panel.add(registerButton);
        frame.add(panel);
    }


}
