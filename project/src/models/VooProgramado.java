package src.models;

import src.utils.types.ClasseAssentos;
import src.utils.types.Recorrencias;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VooProgramado implements Serializable {

    private int id;
    private String aeroportoOrigem;
    private String aeroportoDestino;
    private String aviao;
    private LocalDateTime horarioPrevisto;
    private Recorrencias recorrencias;
    private int vagasEconomica;
    private int vagasExecutiva;
    private int vagasPrimeiraClasse;


    public VooProgramado(String aeroportoOrigem, String aeroportoDestino, String aviao, LocalDateTime horarioPrevisto, Recorrencias recorrencias, int vagasEconomica, int vagasExecutiva, int vagasPrimeira) {
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
        this.aviao = aviao;
        this.horarioPrevisto = horarioPrevisto;
        this.recorrencias = recorrencias;
        this.vagasEconomica = vagasEconomica;
        this.vagasExecutiva = vagasExecutiva;
        this.vagasPrimeiraClasse = vagasPrimeira;
    }

    public VooProgramado(int id, String aeroportoOrigem, String aeroportoDestino, String aviao, LocalDateTime horarioPrevisto, Recorrencias recorrencias, int vagasEconomica, int vagasExecutiva, int vagasPrimeira) {
        this.id = id;
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
        this.aviao = aviao;
        this.horarioPrevisto = horarioPrevisto;
        this.recorrencias = recorrencias;
        this.vagasEconomica = vagasEconomica;
        this.vagasExecutiva = vagasExecutiva;
        this.vagasPrimeiraClasse = vagasPrimeira;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAviao() {
        return aviao;
    }

    public void setAviao(String aviao) {
        this.aviao = aviao;
    }

    public LocalDateTime getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public void setHorarioPrevisto(LocalDateTime horarioPrevisto) {
        this.horarioPrevisto = horarioPrevisto;
    }

    public Recorrencias getRecorrencia() {
        return recorrencias;
    }

    public Recorrencias getRecorrencias() {
        return recorrencias;
    }

    public void setRecorrencias(Recorrencias recorrencias) {
        this.recorrencias = recorrencias;
    }

    public int getVagasEconomica() {
        return vagasEconomica;
    }

    public void setVagasEconomica(int vagasEconomica) {
        this.vagasEconomica = vagasEconomica;
    }

    public int getVagasExecutiva() {
        return vagasExecutiva;
    }

    public void setVagasExecutiva(int vagasExecutiva) {
        this.vagasExecutiva = vagasExecutiva;
    }

    public int getVagasPrimeiraClasse() {
        return vagasPrimeiraClasse;
    }

    public void setVagasPrimeiraClasse(int vagasPrimeiraClasse) {
        this.vagasPrimeiraClasse = vagasPrimeiraClasse;
    }

    public void setRecorrencia(Recorrencias recorrencias) {
        this.recorrencias = recorrencias;
    }

    @Override
    public String toString() {
        return String.format("""
                Voo programado:
                Aeroporto de origem: %s
                Aeroporto de destino: %s
                Aeroporto: %s
                Horário previsto: %s
                Recorrencias: %s
                """, aeroportoOrigem, aeroportoDestino, aviao, horarioPrevisto, recorrencias);
    }
}