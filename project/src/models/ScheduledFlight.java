package src.models;

import src.utils.types.Recurrence;

import java.time.LocalDateTime;

public class ScheduledFlight {

    private int id;
    private String originAirport;
    private String destinationAirport;
    private String airplane;

    private LocalDateTime departureDate;
    private LocalDateTime departureDateReal;
    private Recurrence recurring;

    public ScheduledFlight(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recurrence recurring) {
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.airplane = airplane;
        this.departureDate = departureDate;
        this.recurring = recurring;
    }

    public ScheduledFlight(int id, String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, LocalDateTime departureDateReal, Recurrence recurring) {
        this.id = id;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.airplane = airplane;
        this.departureDate = departureDate;
        this.departureDateReal = departureDateReal;
        this.recurring = recurring;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public String getAirplane() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        this.airplane = airplane;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getDepartureDateReal() {
        return departureDateReal;
    }

    public void setDepartureDateReal(LocalDateTime departureDateReal) {
        this.departureDateReal = departureDateReal;
    }

    public Recurrence getRecurring() {
        return recurring;
    }

    public void setRecurring(Recurrence recurring) {
        this.recurring = recurring;
    }

    @Override
    public String toString() {
        return "ScheduledFlight{" +
                "id=" + id +
                ", originAirport='" + originAirport + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", airplane='" + airplane + '\'' +
                ", departureDate=" + departureDate +
                ", departureDateReal=" + departureDateReal +
                ", recurring=" + recurring +
                '}';
    }
}