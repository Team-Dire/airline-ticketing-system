package src.models;


import src.utils.types.Recorrencias;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Aeroporto implements Serializable {
    private int latestVooProgramadoId = 0;
    private int latestVooId = 0;
    private static Aeroporto instance;
    private ArrayList<VooProgramado> vooProgramados;
    private ArrayList<Voo> voos;
    private ArrayList<Passageiro> passageiros;

    public Aeroporto() {
        this.vooProgramados = new ArrayList<>();
        this.voos = new ArrayList<>();
        this.passageiros = new ArrayList<>();
    }

    public int getLatestVooProgramadoId() {
        return latestVooProgramadoId;
    }

    public void setLatestVooProgramadoId(int latestVooProgramadoId) {
        this.latestVooProgramadoId = latestVooProgramadoId;
    }

    public static void setInstance(Aeroporto instance) {
        Aeroporto.instance = instance;
    }

    public ArrayList<VooProgramado> getVooProgramados() {
        return vooProgramados;
    }

    public void setVooProgramados(ArrayList<VooProgramado> vooProgramados) {
        this.vooProgramados = vooProgramados;
    }

    public ArrayList<Voo> getVoos() {
        return voos;
    }

    public void setVoos(ArrayList<Voo> voos) {
        this.voos = voos;
    }

    public ArrayList<Passageiro> getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(ArrayList<Passageiro> passageiros) {
        this.passageiros = passageiros;
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
            int maxDaysFromNow = 30;
            while (curDate.isBefore(now)) {
                curDate = curDate.plusDays(sumDays);
            }
            while (curDate.isBefore(now.plusDays(maxDaysFromNow))) {
                horariosToBeCreated.add(curDate);
                curDate = curDate.plusDays(sumDays);
            }
        }
        else if (voos.stream().noneMatch(voo -> voo.getVooProgramadoId() == vooProgramado.getId())) {
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
            int id = latestVooId++;
            Voo voo = new Voo(id, vooProgramado);
            voo.setHorarioPrevisto(horario);
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

    public String agendarVoo(String originAirport, String destinationAirport, String airplane, LocalDateTime departureDate, Recorrencias recurring, int distancia, int vagasEconomica, int vagasExecutiva, int vagasPrimeira) {
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

        int id = latestVooProgramadoId++;
        int size = vooProgramados.size();
        VooProgramado vooProgramado = new VooProgramado(id, originAirport, destinationAirport, airplane, departureDate, recurring, distancia, vagasEconomica, vagasExecutiva, vagasPrimeira);
        vooProgramados.add(vooProgramado);
        if (vooProgramados.get(size).getId() != id) {
            return "Erro ao criar voo programado";
        }
        criarVooDesdeVooProgramado(vooProgramado);
        Aeroporto.serialize();
        return "Sucesso";
    }

    public ArrayList<Voo> getVoosDisponiveis(String origem, String destino, boolean idaVolta) {
        ArrayList<Voo> voosDisponiveis = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Voo voo : voos) {
            if (voo.getHorarioPartida() != null) {
                continue;
            }
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


    public boolean vagaLivreVoo(String nomeCompleto, LocalDate dataNascimento, String numId, String tipoNumId, Voo voo, String classe, String assento, Desconto desconto) {
        Classe classeDesejada = voo.getClasses().stream().filter(c -> c.getTipoClasse().toString().equals(classe)).findFirst().orElse(null);
        if (classeDesejada == null) return false;

        Vaga vagaDesejada = classeDesejada.getVagas().stream().filter(v -> v.getAssento().equals(assento)).findFirst().orElse(null);
        if (vagaDesejada == null) return false;

        // verify if passenger already exists
        Passageiro passageiro = passageiros.stream().filter(p -> p.getNomeCompleto().equals(nomeCompleto) && p.getDataNascimento().equals(dataNascimento) && p.getNumId().equals(numId) && p.getTipoNumId().equals(tipoNumId)).findFirst().orElse(null);
        if (passageiro == null) {
            passageiro = new Passageiro(nomeCompleto, numId, tipoNumId, dataNascimento);
            passageiros.add(passageiro);
        }

        Vaga vagaOcupada = vagaDesejada.ocuparVaga(passageiro, desconto);
        if (vagaOcupada == null) return false;

        Aeroporto.serialize();
        return true;
    }

    public boolean atestarDecolagem(Voo voo, LocalTime horarioDecolagem) {
        if (voo.getHorarioPartida() != null) {
            return false;
        }
        LocalDateTime horarioDecolagemCompleto = LocalDateTime.of(voo.getHorarioPrevisto().toLocalDate(), horarioDecolagem);
        voo.setHorarioPartida(horarioDecolagemCompleto);

        this.adicionarMilhasPassageirosFidelizados(voo);
        this.consumirMilhasPassageirosFidelizados(voo);

        Aeroporto.serialize();
        return true;
    }

    private void adicionarMilhasPassageirosFidelizados(Voo voo) {
        ArrayList<Passageiro> passageirosFidelizados = new ArrayList<>();
        voo.getClasses().forEach(classe -> {
            classe.getVagas().forEach(vaga -> {
                Passageiro passageiro = vaga.getPassageiro();
                if (passageiro != null) {
                    System.out.println("Passageiro " + passageiro.getNomeCompleto() + " está no voo. É fidelizado? " + passageiro.isFidelizado());
                    if (passageiro.isFidelizado()) {
                        passageirosFidelizados.add(passageiro);
                    }
                }
            });
        });



        LocalDateTime horarioDecolagem = voo.getHorarioPartida();
        LocalDate dataDecolagem = horarioDecolagem.toLocalDate();

        passageirosFidelizados.forEach(passageiro -> {
            float distanciaMilhasNautica = voo.getDistanciaEmKm() / 1.852f;
            int qtdMilhas = (int) (distanciaMilhasNautica / 5);
            System.out.println("Adicionando " + qtdMilhas + " milhas para o passageiro " + passageiro.getNomeCompleto());
            passageiro.adicionarMilhas(qtdMilhas, dataDecolagem);
        });
        Aeroporto.serialize();
    }

    private void consumirMilhasPassageirosFidelizados(Voo voo) {
        ArrayList<Passageiro> passageirosFidelizados = new ArrayList<>();
        ArrayList<Desconto> descontosUtilizados = new ArrayList<>();

        voo.getClasses().forEach(classe -> {
            classe.getVagas().forEach(vaga -> {
                Passageiro passageiro = vaga.getPassageiro();
                Desconto desconto = vaga.getDescontoUtilizado();
                if (passageiro != null) {
                    if (passageiro.isFidelizado() && desconto.getPorcentagem() > 0f) {
                        passageirosFidelizados.add(passageiro);
                        descontosUtilizados.add(desconto);
                    }
                }
            });
        });

        passageirosFidelizados.forEach(passageiro -> {
            Desconto desconto = descontosUtilizados.get(passageirosFidelizados.indexOf(passageiro));
            System.out.println("Passageiro " + passageiro.getNomeCompleto() + " está no voo. É fidelizado? " + passageiro.isFidelizado() + ". Desconto utilizado: " + desconto.getPorcentagem());
            passageiro.resgatarMilhas(voo.getDistanciaEmKm(), desconto, voo.getHorarioPartida().toLocalDate());
        });
        Aeroporto.serialize();
    }
}
