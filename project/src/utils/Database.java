package src.utils;

import src.models.ScheduledFlight;
import src.utils.types.Recurrence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private Connection connection;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            this.seed();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Connected to database");
    }

    private void seed() {
        this.createTables();
    }

    public boolean execute(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String[]> query(String sql) {
        ArrayList<String[]> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] row = new String[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < row.length; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void createTables() {
        execute("CREATE TABLE IF NOT EXISTS scheduledFlights (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "departureAirport VARCHAR(4) NOT NULL CHECK (LENGTH(departureAirport) = 3 OR LENGTH(departureAirport) = 4)," +
                "arrivalAirport VARCHAR(4) NOT NULL CHECK (LENGTH(arrivalAirport) = 3 OR LENGTH(arrivalAirport) = 4)," +
                "airplane VARCHAR(255) NOT NULL," +
                "departureDate DATETIME NOT NULL CHECK (departureDate > CURRENT_TIMESTAMP)," +
                "departureDateReal DATETIME," +
                "recurring VARCHAR(10) CHECK (recurring IN ('DAILY', 'WEEKLY', 'MONTHLY', 'NONE'))" +
                ")");
    }

    public ArrayList<ScheduledFlight> getScheduledFlights() {
        ArrayList<String[]> flights = query("SELECT * FROM scheduledFlights");

        ArrayList<ScheduledFlight> scheduledFlights = new ArrayList<>();
        for (String[] flight : flights) {
            LocalDateTime departureDate = LocalDateTime.parse(flight[4]);
            LocalDateTime departureDateReal = null;
            if (!Objects.equals(flight[5], "null")) {
                departureDateReal = LocalDateTime.parse(flight[5]);
            }

            scheduledFlights.add(new ScheduledFlight(
                    Integer.parseInt(flight[0]),
                    flight[1],
                    flight[2],
                    flight[3],
                    departureDate,
                    departureDateReal,
                    Recurrence.valueOf(flight[6])
            ));
        }
        return scheduledFlights;
    }

    public boolean scheduleFlight(ScheduledFlight scheduledFlight) {
        return execute("INSERT INTO scheduledFlights (departureAirport, arrivalAirport, airplane, departureDate, departureDateReal, recurring) VALUES (" +
                "'" + scheduledFlight.getOriginAirport() + "'," +
                "'" + scheduledFlight.getDestinationAirport() + "'," +
                "'" + scheduledFlight.getAirplane() + "'," +
                "'" + scheduledFlight.getDepartureDate() + "'," +
                "'" + scheduledFlight.getDepartureDateReal() + "'," +
                "'" + scheduledFlight.getRecurring().toString() + "'" +
                ")");
    }
}

