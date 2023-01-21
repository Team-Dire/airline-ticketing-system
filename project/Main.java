import src.controllers.ControladorAeroporto;
import src.controllers.ControladorVenda;
import src.models.Aeroporto;

public class Main {
    Aeroporto aeroporto;
    ControladorAeroporto controladorAeroporto;
    ControladorVenda controladorVenda;


    public Main() {
        aeroporto = new Aeroporto();
        controladorAeroporto = new ControladorAeroporto(aeroporto);
        controladorVenda = new ControladorVenda(aeroporto);
        new src.views.Main(controladorAeroporto, controladorVenda);

    }

    public static void main(String[] args) {
        new Main();
    }
}