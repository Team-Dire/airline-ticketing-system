package src.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Passageiro implements Serializable {
    private final String nomeCompleto;
    private final String numId;
    private final String tipoNumId;
    private final LocalDate dataNascimento;
    private boolean isFidelizado;

    private ArrayList<Milha> milhas;

    public Passageiro(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento) {
        this.nomeCompleto = nomeCompleto;
        this.numId = numId;
        this.tipoNumId = tipoNumId;
        this.dataNascimento = dataNascimento;
        this.isFidelizado = false;
        this.milhas = new ArrayList<>();
    }

    public Passageiro(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento, boolean isFidelizado) {
        this.nomeCompleto = nomeCompleto;
        this.numId = numId;
        this.tipoNumId = tipoNumId;
        this.dataNascimento = dataNascimento;
        this.isFidelizado = isFidelizado;
        this.milhas = new ArrayList<>();
    }

    public void addMilhas(int quantidade, LocalDate horarioPartida) {
        milhas.add(new Milha(quantidade, horarioPartida));
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNumId() {
        return numId;
    }

    public String getTipoNumId() {
        return tipoNumId;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public boolean isFidelizado() {
        return isFidelizado;
    }

    public void ativarFidelizacao() {
        this.isFidelizado = true;
    }

    public void desativarFidelizacao() {
            this.isFidelizado = false;
    }

    public void setFidelizado(boolean fidelizado) {
        isFidelizado = fidelizado;
    }

    public ArrayList<Milha> getMilhas() {
        return milhas;
    }

    public void setMilhas(ArrayList<Milha> milhas) {
        this.milhas = milhas;
    }
    public int getMilhasTotais() {
        // get non resgatadas
        int milhasTotais = 0;
        for (Milha milha : milhas) {
            if (milha.getResgatadaEm() == null && milha.getExpiraEm().isAfter(LocalDate.now())) {
                milhasTotais += milha.getQuantidade();
            }

        }
        return milhasTotais;
    }

    public void adicionarMilhas(int quantidade, LocalDate horarioPartida) {
        milhas.add(new Milha(quantidade, horarioPartida));
    }

    public void resgatarMilhas(int distancia, Desconto desconto, LocalDate dataResgate) {
        int quantidadeMilhas = 0;
        float milhasNauticas = distancia / 1.852f;
        float porcentagem = desconto.getPorcentagem();
        int porcentagemInt = (int) (porcentagem * 100);
        switch (porcentagemInt) {
            case 0 -> quantidadeMilhas = 0;
            case 25 -> quantidadeMilhas = (int) (milhasNauticas * 7f);
            case 50 -> quantidadeMilhas = (int) (milhasNauticas * 12f);
            case 100 -> quantidadeMilhas = (int) (milhasNauticas * 25f);
        }

        int index = 0;
        while (quantidadeMilhas > 0) {
            if (milhas.size() == 0) {
                return;
            }
            if (milhas.get(index).getResgatadaEm() != null || milhas.get(index).getExpiraEm().isBefore(LocalDate.now())) {
                index++;
                continue;
            }
            milhas.get(index).setResgatadaEm(dataResgate);

            quantidadeMilhas -= milhas.get(index).getQuantidade();


            index++;
        }

        Aeroporto.serialize();
    }
}
