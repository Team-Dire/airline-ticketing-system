package src.models;

import src.utils.types.ClasseAssentos;
import src.utils.types.Recorrencias;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Voo {

    private int id;
    private int vooProgramadoId;
    private String aeroportoOrigem;
    private String aeroportoDestino;
    private String aeroporto;
    private LocalDateTime horarioPrevisto;
    private LocalDateTime horarioPartida;
    private Recorrencias recorrencias;
    private ArrayList<Classe> classes;

    public Voo(int id, int vooProgramadoId, String aeroportoOrigem, String aeroportoDestino, String aviao, LocalDateTime horarioPrevisto, LocalDateTime horarioPartida, Recorrencias recorrencias, ArrayList<Classe> classes) {
        this.id = id;
        this.vooProgramadoId = vooProgramadoId;
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
        this.aeroporto = aviao;
        this.horarioPrevisto = horarioPrevisto;
        this.horarioPartida = horarioPartida;
        this.recorrencias = recorrencias;
        this.classes = classes;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getVooProgramadoId() {
        return vooProgramadoId;
    }

    public void setVooProgramadoId(int vooProgramadoId) {
        this.vooProgramadoId = vooProgramadoId;
    }

    public Recorrencias getRecorrencias() {
        return recorrencias;
    }

    public void setRecorrencias(Recorrencias recorrencias) {
        this.recorrencias = recorrencias;
    }

    public ArrayList<Classe> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Classe> classes) {
        this.classes = classes;
    }

    public String getAeroportoOrigem() {
        return aeroportoOrigem;
    }

    public void setAeroportoOrigem(String aeroportoOrigem) {
        this.aeroportoOrigem = aeroportoOrigem;
    }

    public String getAeroportoDestino() {
        return aeroportoDestino;
    }

    public void setAeroportoDestino(String aeroportoDestino) {
        this.aeroportoDestino = aeroportoDestino;
    }

    public String getAeroporto() {
        return aeroporto;
    }

    public void setAeroporto(String aeroporto) {
        this.aeroporto = aeroporto;
    }

    public LocalDateTime getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public void setHorarioPrevisto(LocalDateTime horarioPrevisto) {
        this.horarioPrevisto = horarioPrevisto;
    }

    public LocalDateTime getHorarioPartida() {
        return horarioPartida;
    }

    public void setHorarioPartida(LocalDateTime horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public Recorrencias getRecorrencia() {
        return recorrencias;
    }

    public void setRecorrencia(Recorrencias recorrencias) {
        this.recorrencias = recorrencias;
    }

    public ArrayList<String> getAssentosDisponiveis(String classeString) {
        ArrayList<String> assentos = new ArrayList<>();
        for (Classe classe : classes) {
            if (classe.getTipoClasse().toString().equals(classeString)) {
                for (Vaga vaga : classe.getVagas()) {
                    if (!vaga.isOcupada()) {
                        assentos.add(vaga.getAssento());
                    }
                }
            }
        }
        return assentos;
    }
    @Override
    public String toString() {

        return String.format("""
                Voo:
                id: %d
                Aeroporto de Origem: %s
                Aeroporto de Destino: %s
                Aeroporto: %s
                Horário Previsto: %s
                Horário de Partida: %s
                Recorrências: %s
                Classes: %s
                """, id, aeroportoOrigem, aeroportoDestino, aeroporto, horarioPrevisto, horarioPartida, recorrencias, classes);
    }
}