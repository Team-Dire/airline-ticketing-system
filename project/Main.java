import src.classes.Airport;
import src.controllers.AirportController;

import java.util.Date;

public class Main {
    Airport airport;

    AirportController airportController;

    public Main() {
        airport = new Airport();
        airportController = new AirportController(airport);

        airportController.newFlightSchedule("LAX", "JFK", "Boeing 747", new Date(), new Date(), "Weekly");

    }

    public static void main(String[] args) {
        new Main();
    }
}