package src.controllers;

import src.models.Aeroporto;
import src.models.Voo;
import src.utils.types.Recorrencias;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ControladorAeroporto {
    private final Aeroporto aeroporto;

    public ControladorAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public String novoAgendamentoDeVoo(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recorrencias recurring, int distancia, int economySeats, int executiveSeats, int firstClassSeats) {
        return aeroporto.agendarVoo(originAirport, destinationAirport, airplane, departureDate, recurring, distancia, economySeats, executiveSeats, firstClassSeats);
    }

    public ArrayList<Voo> getVoos() {
        return aeroporto.getVoos();
    }

    public boolean atestarDecolagem(Voo voo, LocalTime horaDeChegada) {
        return aeroporto.atestarDecolagem(voo, horaDeChegada);
    }

}