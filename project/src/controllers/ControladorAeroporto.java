package src.controllers;

import src.models.Aeroporto;
import src.utils.types.Recorrencias;

import java.time.LocalDateTime;

public class ControladorAeroporto {
    private final Aeroporto aeroporto;

    public ControladorAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public String novoAgendamentoDeVoo(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recorrencias recurring, int economySeats, int executiveSeats, int firstClassSeats) {
        return aeroporto.agendarVoo(originAirport, destinationAirport, airplane, departureDate, recurring, economySeats, executiveSeats, firstClassSeats);
    }
}