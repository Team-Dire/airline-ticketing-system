package src.models;

import src.utils.Database;
import src.utils.types.Recurrence;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Airport {
    private final ArrayList<ScheduledFlight> scheduledFlights;
    private final Database db = new Database();

    public Airport() {
        scheduledFlights = db.getScheduledFlights();
        printScheduledFlights();
    }

    public boolean scheduleFlight(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recurrence recurring) {
        LocalDateTime sevenDaysFromNow = LocalDateTime.now().plusDays(7);
        if (departureDate.isBefore(sevenDaysFromNow)) {
            System.out.println("You can only schedule flights after 7 days from now");
            return false;
        }

        if (departureDate.isBefore(LocalDateTime.now())) {
            System.out.println("You can't schedule a flight in the past.");
            return false;
        }

        // If the flight is 30 minutes before or after from any other flight, it can't be scheduled
        for (ScheduledFlight scheduledFlight : scheduledFlights) {
            if (scheduledFlight.getDepartureDate().isBefore(departureDate.plusMinutes(30)) && scheduledFlight.getDepartureDate().isAfter(departureDate.minusMinutes(30))) {
                System.out.println("You can't schedule a flight 30 minutes before or after another flight.");
                return false;
            }
        }

        ScheduledFlight scheduledFlight = new ScheduledFlight(originAirport, destinationAirport, airplane, departureDate, recurring);
        scheduledFlights.add(scheduledFlight);
        return db.scheduleFlight(scheduledFlight);
    }

    public void printScheduledFlights() {
        for (ScheduledFlight scheduledFlight : scheduledFlights) {
            System.out.println(scheduledFlight);
        }
    }
}
