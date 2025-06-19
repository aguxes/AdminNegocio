package View;

import DataBase.Querrys;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AppController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextArea outputArea;

    @FXML
    protected void mostrarClientes() {
        System.out.println("\n\n Mostrando clientes desde botón...");
        String datos = Querrys.obtenerClientesComoTexto(); // NUEVO método
        outputArea.setText(datos);

    }

    @FXML
    protected void mostrarVentas() {
        System.out.println("\n\nMostrando ventas desde botón...");
        String datos = Querrys.obtenerVentasComoTexto(); // NUEVO método
        outputArea.setText(datos);

    }
}
