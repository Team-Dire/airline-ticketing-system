package src.classes;

import java.util.Date;

public class ScheduledFlight {
    private String originAirport;
    private String destinationAirport;
    private String airplane;

    private Date departureDate;
    private Date departureDateReal;
    private String recurring;

    public ScheduledFlight(String originAirport, String destinationAirport, String airplane, Date departureDate, Date departureDateReal, String recurring) {
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.airplane = airplane;
        this.departureDate = departureDate;
        this.departureDateReal = departureDateReal;
        this.recurring = recurring;
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

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getDepartureDateReal() {
        return departureDateReal;
    }

    public void setDepartureDateReal(Date departureDateReal) {
        this.departureDateReal = departureDateReal;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }
}