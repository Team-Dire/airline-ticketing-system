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
            System.out.println("Você não pode agendar um voo com menos de 7 dias de antecedência.");
            return false;
        }

        if (departureDate.isBefore(LocalDateTime.now())) {
            System.out.println("Você não pode agendar voos no passado");
            return false;
        }

        // If the flight is 30 minutes before or after from any other flight, it can't be scheduled
        for (ScheduledFlight scheduledFlight : scheduledFlights) {
            LocalDateTime departureTime = scheduledFlight.getDepartureDate();
            String originAirportCode = scheduledFlight.getOriginAirport();

            boolean isSameAirport = originAirportCode.equals(originAirport);
            boolean isBefore = departureTime.isBefore(departureDate.plusMinutes(30));
            boolean isAfter = departureTime.isAfter(departureDate.minusMinutes(30));
            if (isSameAirport && (isBefore || isAfter)) {
                System.out.println("Você não pode agendar um voo 30 minutos antes ou depois de outro voo.");
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
