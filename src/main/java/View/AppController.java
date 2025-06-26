package View;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class AppController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextArea outputArea;

    @FXML
    private HBox submenuClientes;



    /*
    @FXML
    protected void mostrarClientes() {
        System.out.println("\n\n Mostrando clientes desde botón...");
        String datos = VentanaClientes.MostrarClientes();
        outputArea.setText(datos);
    }
*/
    @FXML
    protected void mostrarClientes() {
        outputArea.clear();
        submenuClientes.setVisible(true); // 🔁 mostrar el menú de clientes
    }


    @FXML
    protected void mostrarVentas() {
        System.out.println("\n\nMostrando ventas desde botón...");
        String datos = VentanaVentas.obtenerVentasComoTexto();
        outputArea.setText(datos);

    }

    public void mostrarInventario(ActionEvent actionEvent) {
    }

    public void mostrarReportes(ActionEvent actionEvent) {
    }

    @FXML
    private void verClientes() {
        outputArea.setText(VentanaClientes.MostrarClientes());
    }

    @FXML
    private void agregarCliente() {
        // Lógica adaptada que abra un formulario o agregue por consola
    }

    @FXML
    private void modificarCliente() {
        // Mostrar lista de clientes y pedir ID a modificar
    }

    @FXML
    private void eliminarCliente() {
        // Pedir ID y eliminar
    }

    @FXML
    private void buscarCliente() {
        // Buscar por nombre o email
    }

}
