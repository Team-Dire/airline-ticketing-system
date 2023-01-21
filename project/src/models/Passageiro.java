package src.models;

import java.time.LocalDate;

public class Passageiro {
    private final String nomeCompleto;
    private final String numId;
    private final String tipoNumId;
    private final LocalDate dataNascimento;

    public Passageiro(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento) {
        this.nomeCompleto = nomeCompleto;
        this.numId = numId;
        this.tipoNumId = tipoNumId;
        this.dataNascimento = dataNascimento;
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

}
