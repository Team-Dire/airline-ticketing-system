package src.views;

import src.controllers.AirportController;
import src.utils.types.Recurrence;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class FlightRegister {
    private final AirportController airportController;
    private final JFrame frame;
    private JTextField originAirportField;
    private JTextField destinationAirportField;
    private JTextField airplaneField;
    private JTextField departureDateField;
    private JTextField departureTimeField;
    private JComboBox<?> recurringComboBox;

    public FlightRegister(AirportController airportController) {
        this.airportController = airportController;
        frame = new JFrame("Cadastrar voo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUIComponents();
    }

    private void createUIComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        JLabel originAirportLabel = new JLabel("Aeroporto de origem");
        JLabel destinationAirportLabel = new JLabel("Aeroporto de destino");
        JLabel airplaneLabel = new JLabel("Avião");
        JLabel departureDateLabel = new JLabel("Data de partida prevista (Ex: 13/01/2000)");
        JLabel departureTimeLabel = new JLabel("Hora de partida prevista (Ex: 13:00)");
        JLabel recurringLabel = new JLabel("Recorrência");


        originAirportField = new JTextField();
        originAirportField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (!Character.isLetter(evt.getKeyChar()) || originAirportField.getText().length() >= 4) {
                    evt.consume();
                }
            }
        });

        destinationAirportField = new JTextField();
        destinationAirportField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (!Character.isLetter(evt.getKeyChar()) || destinationAirportField.getText().length() >= 4) {
                    evt.consume();
                }
            }
        });

        airplaneField = new JTextField();

        departureDateField = new JTextField();
        departureDateField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (departureDateField.getText().length() >= 10) {
                    evt.consume();
                }
            }
        });

        departureTimeField = new JTextField();
        departureTimeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (departureTimeField.getText().length() >= 5) {
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
            Recurrence recurring;
            switch (recurrenceValue) {
                case "Mensal":
                    recurring = Recurrence.MONTHLY;
                    break;
                case "Semanal":
                    recurring = Recurrence.WEEKLY;
                    break;
                case "Diário":
                    recurring = Recurrence.DAILY;
                    break;
                default:
                    recurring = Recurrence.NONE;
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
            if (originAirport.length() < 3 || destinationAirport.length() < 3) {
                JOptionPane.showMessageDialog(null, "Código de aeroporto inválido");
                return;
            }

            if (originAirport.equals(destinationAirport)) {
                JOptionPane.showMessageDialog(null, "Aeroporto de origem e destino não podem ser iguais");
                return;
            }
            

            boolean success = airportController.newFlightSchedule(originAirport, destinationAirport, airplane, departureDateTime, recurring);
            if (success) {
                JOptionPane.showMessageDialog(null, "Voo cadastrado com sucesso");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível cadastrar o voo");
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
