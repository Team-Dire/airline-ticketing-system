import src.controllers.AirportController;
import src.models.Airport;

public class Main {
    Airport airport;
    AirportController airportController;


    public Main() {
        airport = new Airport();
        airportController = new AirportController(airport);
        new src.views.Main(airportController);

    }

    public static void main(String[] args) {
        new Main();
    }
}