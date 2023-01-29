package src.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Vaga implements Serializable {
    private Passageiro passageiro;
    private final String assento;
    private boolean ocupada;

    private Desconto descontoUtilizado;


    public Vaga(String assento) {
        this.assento = assento;
        this.ocupada = false;
        this.passageiro = null;
        this.descontoUtilizado = null;
    }

    public Vaga(String assento, boolean ocupada, Passageiro passageiro, Desconto descontoUtilizado) {
        this.assento = assento;
        this.ocupada = ocupada;
        this.passageiro = passageiro;
        this.descontoUtilizado = null;
    }

    public Vaga ocuparVaga(Passageiro passageiro, Desconto desconto) {
        if (!this.ocupada) {
            this.passageiro = passageiro;
            this.descontoUtilizado = desconto;
            this.ocupada = true;
            return this;
        }
        return null;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public Desconto getDescontoUtilizado() {
        return descontoUtilizado;
    }

    public void setDescontoUtilizado(Desconto descontoUtilizado) {
        this.descontoUtilizado = descontoUtilizado;
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

    @Override
    public String toString() {
        return String.format("""
                Vaga:
                Passageiro: %s
                Assento: %s
                Ocupada: %s
                """, passageiro, assento, ocupada);

    }
}
