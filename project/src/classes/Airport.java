package src.classes;

import java.util.ArrayList;
import java.util.Date;

public class Airport {
    private final ArrayList<ScheduledFlight> scheduledFlights = new ArrayList<>();

    public void scheduleFlight(String originAirport, String destinationAirport, String airplane, Date departureDate, Date departureDateReal, String recurring) {
        ScheduledFlight scheduledFlight = new ScheduledFlight(originAirport, destinationAirport, airplane, departureDate, departureDateReal, recurring);
        scheduledFlights.add(scheduledFlight);
    }
}
