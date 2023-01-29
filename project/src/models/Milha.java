package src.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Milha implements Serializable {
    private final int quantidade;
    private final LocalDate expiraEm;
    private LocalDate resgatadaEm;

    public Milha(int quantidade, LocalDate dataCompra) {
        this.quantidade = quantidade;
        this.expiraEm = dataCompra.plusYears(1);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDate resgatar() {
        resgatadaEm = LocalDate.now();
        return resgatadaEm;
    }

    public LocalDate getExpiraEm() {
        return expiraEm;
    }

    public LocalDate getResgatadaEm() {
        return resgatadaEm;
    }

    public void setResgatadaEm(LocalDate resgatadaEm) {
        this.resgatadaEm = resgatadaEm;
    }
}
