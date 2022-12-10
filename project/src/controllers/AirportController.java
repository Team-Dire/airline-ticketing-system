package src.controllers;

import src.classes.Airport;

import java.util.Date;

public class AirportController {
    private final Airport airport;

    public AirportController(Airport airport) {
        this.airport = airport;
    }

    public void newFlightSchedule(String originAirport, String destinationAirport, String airplane, Date departureDate, Date departureDateReal, String recurring) {
        airport.scheduleFlight(originAirport, destinationAirport, airplane, departureDate, departureDateReal, recurring);
    }
}