package src.models;

import src.utils.types.ClasseAssentos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Classe implements Serializable {
    private final int id;
    private final ClasseAssentos tipoClasse;
    private final ArrayList<Vaga> vagas;

    public Classe(ClasseAssentos tipoClasse, int quantidadeTotalVagas) {
        this.id = 0;
        this.tipoClasse = tipoClasse;
        this.vagas = new ArrayList<>();
        for (int i = 0; i < quantidadeTotalVagas; i++) {
            String indexPadded = String.format("%03d", i + 1);
            String prefix = tipoClasse.toString().substring(0, 2).concat(indexPadded);
            vagas.add(new Vaga(prefix));
        }
    }

    public Classe(int id, String tipoClasse, ArrayList<Vaga> vagas) {
        this.id = id;
        this.tipoClasse = ClasseAssentos.valueOf(tipoClasse);
        this.vagas = vagas;
    }


    public int getId() {
        return id;
    }

    public boolean ocuparVaga(String nomeCompleto, LocalDate dataNascimento, String numId, String tipoNumId, String assento) {
        for (Vaga vaga : vagas) {
            if (vaga.getAssento().equals(assento) && !vaga.isOcupada()) {
                Vaga vagaOcupada = vaga.ocuparVaga(nomeCompleto, dataNascimento, numId, tipoNumId);
                // if the seat is already occupied, return false
                return vagaOcupada != null && vagaOcupada.getPassageiro().getNumId().equals(numId);
            }
        }
        return false;
    }

    public ClasseAssentos getTipoClasse() {
        return tipoClasse;
    }

    public ArrayList<Vaga> getVagas() {
        return vagas;
    }
}

