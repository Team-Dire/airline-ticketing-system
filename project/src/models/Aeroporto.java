package src.models;


import src.utils.types.Recorrencias;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Aeroporto implements Serializable {
    private int latestId = 0;
    private static Aeroporto instance;
    private ArrayList<VooProgramado> vooProgramados;
    private ArrayList<Voo> voos;

    public Aeroporto() {
        this.vooProgramados = new ArrayList<>();
        this.voos = new ArrayList<>();
    }

    private void criarVooDesdeVooProgramado(VooProgramado vooProgramado) {
        int sumDays;
        switch (vooProgramado.getRecorrencia()) {
            case DIÁRIO -> sumDays = 1;
            case SEMANAL -> sumDays = 7;
            case MENSAL -> sumDays = 30;
            case ÚNICO -> sumDays = 0;
            default -> throw new IllegalStateException("Unexpected value: " + vooProgramado.getRecorrencia());
        }

        ArrayList<LocalDateTime> horariosToBeCreated = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime curDate = vooProgramado.getHorarioPrevisto();

        if (sumDays != 0) {
            int maxDaysFromNow = 31;
            while (curDate.isBefore(now)) {
                curDate = curDate.plusDays(sumDays);
            }
            while (curDate.isBefore(now.plusDays(maxDaysFromNow))) {
                horariosToBeCreated.add(curDate);
                curDate = curDate.plusDays(sumDays);
            }
        }
        else if (curDate.isAfter(now)) {
            horariosToBeCreated.add(curDate);
        }


        for (LocalDateTime horario : horariosToBeCreated) {
            for (Voo voo : voos) {
                if (voo.getVooProgramadoId() == vooProgramado.getId() && voo.getHorarioPrevisto().equals(horario)) {
                    horariosToBeCreated.remove(horario);
                }
            }
        }

        for (LocalDateTime horario : horariosToBeCreated) {
            Voo voo = new Voo(vooProgramado);
            voos.add(voo);
        }
        Aeroporto.serialize();
    }

    private void refreshVoos() {
        vooProgramados.stream().forEach(this::criarVooDesdeVooProgramado);
    }

    public static Aeroporto getInstance() {
        if (instance == null) {
            try {
            instance = (Aeroporto) new ObjectInputStream(new FileInputStream("aeroporto.ser")).readObject();
            } catch (IOException | ClassNotFoundException e) {
                instance = new Aeroporto();
            }
        }
        return instance;
    }

    public static void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("aeroporto.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Aeroporto.instance);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            String originAirportCode = vooProgramado.getAeroportoOrigem();

            boolean isSameAirport = originAirportCode.equals(originAirport);
            boolean is30MinutesBefore = departureTime.isBefore(newFlightDepartureTime.minusMinutes(30));
            boolean is30MinutesAfter = departureTime.isAfter(newFlightDepartureTime.plusMinutes(30));
            if (isSameAirport && !is30MinutesBefore && !is30MinutesAfter) {
                return "Você não pode agendar um voo 30 minutos antes ou depois de outro voo.";
            }
        }

        if (vagasEconomica < 0 || vagasExecutiva < 0 || vagasPrimeira < 0) {
            return "Você não pode agendar um voo com menos de 0 vagas para alguma classe.";
        }

        int id = latestId++;
        int size = vooProgramados.size();
        VooProgramado vooProgramado = new VooProgramado(id, originAirport, destinationAirport, airplane, departureDate, recurring, vagasEconomica, vagasExecutiva, vagasPrimeira);
        vooProgramados.add(vooProgramado);
        if (vooProgramados.get(size).getId() != id) {
            return "Erro ao criar voo programado";
        }
        criarVooDesdeVooProgramado(vooProgramado);
        Aeroporto.serialize();
        return "Voo programado com sucesso";
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


    public boolean vagaLivreVoo(String nomeCompleto, LocalDate dataNascimento, String numId, String tipoNumId, Voo voo, String classe, String assento) {
        Classe classeDesejada = voo.getClasses().stream().filter(c -> c.getTipoClasse().toString().equals(classe)).findFirst().orElse(null);
        if (classeDesejada == null) return false;

        Vaga vagaDesejada = classeDesejada.getVagas().stream().filter(v -> v.getAssento().equals(assento)).findFirst().orElse(null);
        if (vagaDesejada == null) return false;

        Vaga vagaOcupada = vagaDesejada.ocuparVaga(nomeCompleto, dataNascimento, numId, tipoNumId);
        if (vagaOcupada == null) return false;

        Aeroporto.serialize();
        return true;
    }
}
