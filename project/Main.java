import src.controllers.ControladorAeroporto;
import src.controllers.ControladorProgramaFidelidade;
import src.controllers.ControladorVenda;
import src.models.Aeroporto;

public class Main {
    Aeroporto aeroporto;
    ControladorAeroporto controladorAeroporto;
    ControladorVenda controladorVenda;

    ControladorProgramaFidelidade controladorProgramaFidelidade;


    public Main() {
        aeroporto = Aeroporto.getInstance();
        controladorAeroporto = new ControladorAeroporto(aeroporto);
        controladorVenda = new ControladorVenda(aeroporto);
        controladorProgramaFidelidade = new ControladorProgramaFidelidade(aeroporto);
        new src.views.Main(controladorAeroporto, controladorVenda, controladorProgramaFidelidade);
    }

    public static void main(String[] args) {
        new Main();
    }
}