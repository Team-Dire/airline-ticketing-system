package src.controllers;

import src.models.Aeroporto;
import src.models.Passageiro;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControladorProgramaFidelidade {
    private final Aeroporto aeroporto;
    public ControladorProgramaFidelidade(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public boolean fidelizar(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento) {
        ArrayList<Passageiro> passageiros = aeroporto.getPassageiros();
        for (Passageiro passageiro : passageiros) {
            if (passageiro.getNomeCompleto().equals(nomeCompleto) && passageiro.getNumId().equals(numId) && passageiro.getTipoNumId().equals(tipoNumId) && passageiro.getDataNascimento().equals(dataNascimento)) {
                if (passageiro.getDataNascimento().isAfter(LocalDate.now().minusYears(18))) {
                    'return false;
                }
                passageiro.ativarFidelizacao();
                Aeroporto.serialize();
                return true;
            }
        }

        Passageiro passageiro = new Passageiro(nomeCompleto, numId, tipoNumId, dataNascimento, true);
        passageiros.add(passageiro);
        Aeroporto.serialize();
        return true;
    }

    public boolean tirarFidelizacao(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento) {
        ArrayList<Passageiro> passageiros = aeroporto.getPassageiros();
        for (Passageiro passageiro : passageiros) {
            if (passageiro.getNomeCompleto().equals(nomeCompleto) && passageiro.getNumId().equals(numId) && passageiro.getTipoNumId().equals(tipoNumId) && passageiro.getDataNascimento().equals(dataNascimento)) {
                passageiro.desativarFidelizacao();
                Aeroporto.serialize();
                return true;
            }
        }
        Aeroporto.serialize();
        return false;
    }

    public ArrayList<Passageiro> getPassageiros() {
        return aeroporto.getPassageiros();
    }

}
