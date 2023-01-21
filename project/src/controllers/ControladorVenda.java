package src.controllers;

import src.models.Aeroporto;
import src.models.Classe;
import src.models.Voo;
import src.models.VooProgramado;

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
}
