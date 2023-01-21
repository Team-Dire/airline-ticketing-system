package src.models;

import java.time.LocalDate;

public class Vaga {
    private Passageiro passageiro;
    private final String assento;
    private boolean ocupada;


    public Vaga(String assento) {
        this.assento = assento;
        this.ocupada = false;
        this.passageiro = null;
    }

    public Vaga(String assento, boolean ocupada, Passageiro passageiro) {
        this.assento = assento;
        this.ocupada = true;
        this.passageiro = passageiro;
    }

    public Vaga ocuparVaga(String nomeCompleto, LocalDate dataNascimento, String numId, String tipoNumId) {
        this.passageiro = new Passageiro(nomeCompleto,numId, tipoNumId, dataNascimento);
        if (!this.ocupada) {
            this.ocupada = true;
            return this;
        }
        return null;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public String getAssento() {
        return assento;
    }

    public boolean isOcupada() {
        return ocupada;
    }
}
