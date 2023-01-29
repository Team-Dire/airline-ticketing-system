package src.controllers;

import src.models.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControladorVenda {
    private final Aeroporto aeroporto;

    public ControladorVenda(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public ArrayList<String> getAssentosDisponiveis(Voo voo, String classe) {
        return voo.getAssentosDisponiveis(classe);
    }
    public ArrayList<Voo> getVoosDisponiveis(String origem, String destino, boolean isIdaVolta) {
        return aeroporto.getVoosDisponiveis(origem, destino, isIdaVolta);
    }

    public boolean novaVenda(String nomeCompleto, LocalDate dataNascimento, String numId, String tipoNumId, Voo voo, String classe, String assento, Desconto desconto) {
        boolean possivelOcupar = aeroporto.vagaLivreVoo(nomeCompleto, dataNascimento, numId,  tipoNumId,  voo, classe, assento, desconto);
        return possivelOcupar;
    }

    public Passageiro buscaPassageiro(String nomeCompleto, String numId, String tipoNumId, LocalDate dataNascimento) {
        ArrayList<Passageiro> passageiros = aeroporto.getPassageiros();
        return passageiros.stream().filter(passageiro -> passageiro.getNomeCompleto().equals(nomeCompleto) && passageiro.getNumId().equals(numId) && passageiro.getTipoNumId().equals(tipoNumId) && passageiro.getDataNascimento().equals(dataNascimento)).findFirst().orElse(null);
    }
}
