package src.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Desconto implements Serializable {
    private float porcentagem;

    public Desconto(float porcentagem) {
        this.porcentagem = porcentagem;
    }

    public static ArrayList<Desconto> possiveisDescontos(Voo voo, ArrayList<Milha> milhas) {
        ArrayList<Desconto> descontos = new ArrayList<>();
        int quantidadeMilhas = 0;
        for (Milha milha : milhas) {
            quantidadeMilhas += milha.getQuantidade();
        }
        int distanciaEmKm = voo.getDistanciaEmKm();
        float milhasNauticas = distanciaEmKm / 1.852f;

        if (quantidadeMilhas >= milhasNauticas * 7f) {
            descontos.add(new Desconto(0.25f));
        }
        if (quantidadeMilhas >= milhasNauticas * 12f) {
            descontos.add(new Desconto(0.5f));
        }
        if (quantidadeMilhas >= milhasNauticas * 25f) {
            descontos.add(new Desconto(1f));
        }
        return descontos;
    }

    public float getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(float porcentagem) {
        this.porcentagem = porcentagem;
    }
}
