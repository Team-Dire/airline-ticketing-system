package src.controllers;

import src.models.Airport;
import src.utils.types.Recurrence;

import java.time.LocalDateTime;

public class AirportController {
    private final Airport airport;

    public AirportController(Airport airport) {
        this.airport = airport;
    }

    public boolean newFlightSchedule(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recurrence recurring) {
        return airport.scheduleFlight(originAirport, destinationAirport, airplane, departureDate, recurring);
    }
}