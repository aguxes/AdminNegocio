package View;

// Importo lo minimo e indispensable
import View.*;
import Clases.Extras.*;
import Clases.Principales.*;
import DataBase.*;
import util.Mapper;
// muchas cosas no se
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;
//SQL y fecha
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Consumer;

public class AppController {
    private static final Connection conn = DataBaseConnection.getConnection();

    @FXML private VBox menuLateral;
    @FXML private TextArea outputArea;
    @FXML
    private VBox contenidoPrincipal;
    @FXML
    private VBox contenedor;

    private TextField txtClienteId;
    private TextField txtEmpleadoId;


    private VentanaProducto ventanaProducto;
    private VentanaVentas ventanaVentas;
    private VentanaClientes ventanaClientes;

    @FXML
    public void initialize() {
        ventanaProducto = new VentanaProducto(contenedor, outputArea);
        ventanaVentas = new VentanaVentas(contenedor, outputArea);
        ventanaClientes = new VentanaClientes(contenedor, outputArea);

        cargarMenuPrincipal();
    }

    private void cargarMenuPrincipal() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Clientes", e -> mostrarSubmenuClientes()),
                crearBoton("Ventas", e -> mostrarSubmenuVentas()),
                crearBoton("Inventario", e -> mostrarSubmenuInventario()),
                crearBoton("Reportes", e -> mostrarSubmenuReportes())
        );
    }
    // SUBMENÚS
    private void mostrarSubmenuClientes() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Clientes", e -> ventanaClientes.verClientes()),
                crearBoton("Agregar Cliente", e -> ventanaClientes.agregarCliente()),
                crearBoton("Modificar Cliente", e -> ventanaClientes.modificarCliente()),
                crearBoton("Eliminar Cliente", e -> ventanaClientes.EliminarCliente()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }

    private void mostrarSubmenuVentas() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Ventas", e -> ventanaVentas.verVentas()),
                crearBoton("Nueva Venta", e -> ventanaVentas.registrarVenta()),
                crearBoton("Ventas por Cliente", e -> ventanaVentas.ventasPorCliente()),
                crearBoton("⬅ Volver", e -> cargarMenuPrincipal())
        );
    }

    private void mostrarSubmenuInventario() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Productos", e -> ventanaProducto.verProductos()),
                crearBoton("Agregar Producto", e -> ventanaProducto.agregarProducto()),
                crearBoton("Modificar Producto", e -> ventanaProducto.modificarProducto()),
                crearBoton("Eliminar Producto", e -> ventanaProducto.eliminarProducto()),
                crearBoton("Buscar Producto", e -> ventanaProducto.buscarProducto()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }

    private void mostrarSubmenuReportes() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Reportes", e -> verReportes()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }
    //FUNCIONES DE APP CONTROLLER
    public static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    //----------------------------------------------------------------------------

    //CLIENTE


    //clase reutilizable para darle doble click y que se selccione
    public static <T> void agregarDobleClickSeleccion(TableView<T> tabla, Consumer<T> onSeleccionar) {
        tabla.setRowFactory(tv -> {
            TableRow<T> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !fila.isEmpty()) {
                    T seleccionado = fila.getItem();
                    onSeleccionar.accept(seleccionado);
                }
            });
            return fila;
        });
    }

    // VENTAS

    // INVENTARIO


    private void verReportes() {
        outputArea.setText("📊 Mostrar reportes de ventas/clientes/productos.");
    }

    // FABRICADOR DE BOTONES
    private Button crearBoton(String texto, javafx.event.EventHandler<javafx.event.ActionEvent> evento) {
        Button btn = new Button(texto);
        btn.setOnAction(evento);
        btn.getStyleClass().add("boton-menu");
        btn.setPrefWidth(160);
        return btn;
    }
}
