package src.models;

import src.utils.Database;
import src.utils.types.Recorrencias;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Aeroporto {
    private ArrayList<VooProgramado> vooProgramados;
    private ArrayList<Voo> voos;
    private final Database db = new Database();

    public Aeroporto() {
        vooProgramados = db.obterVoosProgramados();
        db.criarVoosDesdeVoosProgramados(vooProgramados);
        voos = db.obterVoos();
        imprimirVoosProgramados();
    }

    public String agendarVoo(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recorrencias recurring, int vagasEconomica, int vagasExecutiva, int vagasPrimeira) {
        LocalDateTime sevenDaysFromNow = LocalDateTime.now().plusDays(7);
        if (departureDate.isBefore(sevenDaysFromNow)) {
            return "A data de partida deve ser no mínimo 7 dias após a data atual";
        }

        if (departureDate.isBefore(LocalDateTime.now())) {
            return "Você não pode agendar voos no passado";
        }

        // If the flight is 30 minutes before or after from any other flight, it can't be scheduled
        for (VooProgramado vooProgramado : vooProgramados) {
            LocalTime departureTime = vooProgramado.getHorarioPrevisto().toLocalTime();
            LocalTime newFlightDepartureTime = departureDate.toLocalTime();
            System.out.println(String.format("""
                            Voo existente: %s
                            Horário de partida: %s
                            """,
                    vooProgramado.getId(),
                    departureTime
            ));
            System.out.println(String.format("""
                            Voo novo: %s
                            Horário de partida: %s
                            """,
                    airplane,
                    newFlightDepartureTime
            ));
            String originAirportCode = vooProgramado.getAeroportoOrigem();

            boolean isSameAirport = originAirportCode.equals(originAirport);
            boolean is30MinutesBefore = departureTime.isBefore(newFlightDepartureTime.minusMinutes(30));
            boolean is30MinutesAfter = departureTime.isAfter(newFlightDepartureTime.plusMinutes(30));
            System.out.println(String.format("""
                            Mesmo aeroporto: %s
                            Antes: %s
                            Depois: %s
                            """,
                    isSameAirport,
                    is30MinutesBefore,
                    is30MinutesAfter
            ));
            if (isSameAirport && !is30MinutesBefore && !is30MinutesAfter) {
                return "Você não pode agendar um voo 30 minutos antes ou depois de outro voo.";
            }
        }

        if (vagasEconomica < 0 || vagasExecutiva < 0 || vagasPrimeira < 0) {
            return "Você não pode agendar um voo com menos de 0 vagas para alguma classe.";
        }
        VooProgramado vooProgramado = new VooProgramado(originAirport, destinationAirport, airplane, departureDate, recurring, vagasEconomica, vagasExecutiva, vagasPrimeira);
        VooProgramado novoVooProgramado = db.armazenarVooProgramado(vooProgramado);
        if (novoVooProgramado != null) {
            vooProgramados.add(novoVooProgramado);
            this.obterVoos();
            return "Sucesso";
        } else {
            return "Houve algum erro ao armazenar voo programado.";
        }
    }

    public ArrayList<Voo> getVoosDisponiveis(String origem, String destino, boolean idaVolta) {
        ArrayList<Voo> voosDisponiveis = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Voo voo : voos) {
            LocalDateTime dataAtual = voo.getHorarioPrevisto();
            boolean isAfterNow = dataAtual.isAfter(now);
            // verify if string contains substring
            boolean isSameOrigin = voo.getAeroportoOrigem().contains(origem);
            boolean isSameDestination = voo.getAeroportoDestino().contains(destino);

            boolean shouldConsider = isAfterNow && isSameOrigin && isSameDestination;
            if (shouldConsider) {
                if (idaVolta) {

//                    List voosAft = voos.stream().filter(v -> v.getAeroportoOrigem().equals(destino) && v.getAeroportoDestino().equals(origem)).toList();
                    List voosAfterCurrent = voos.stream().filter(v -> v.getHorarioPrevisto().isAfter(voo.getHorarioPrevisto()) && v.getAeroportoOrigem().contains(destino) && v.getAeroportoDestino().contains(origem)).toList();

                    if (voosAfterCurrent.size() > 0) {
                        voosDisponiveis.add(voo);
                    }
                } else {
                    voosDisponiveis.add(voo);
                }
            }
        }
        return voosDisponiveis;
    }

    public void imprimirVoosProgramados() {
        for (VooProgramado vooProgramado : vooProgramados) {
        }
    }

    public void obterVoos() {
        db.criarVoosDesdeVoosProgramados(vooProgramados);
        ArrayList<Voo> voos = db.obterVoos();
        this.voos = voos;
    }
}
