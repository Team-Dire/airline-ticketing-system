package src.models;

import src.utils.types.ClasseAssentos;

import java.time.LocalDate;
import java.util.ArrayList;

public class Classe {
    private final ClasseAssentos tipoClasse;
    private final ArrayList<Vaga> vagas;

    public Classe(ClasseAssentos tipoClasse, int quantidadeTotalVagas) {
        this.tipoClasse = tipoClasse;
        this.vagas = new ArrayList<>();
        for (int i = 0; i < quantidadeTotalVagas; i++) {

            String prefix = tipoClasse.toString().substring(0, 2).concat(String.valueOf(i + 1));
            vagas.add(new Vaga(prefix));
        }
    }

    public Classe(String tipoClasse, ArrayList<Vaga> vagas) {

        this.tipoClasse = ClasseAssentos.valueOf(tipoClasse);
        this.vagas = vagas;
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

