package View;

import DataBase.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.util.function.Consumer;

public class AppController {
    private static final Connection conn = DataBaseConnection.getConnection();

    @FXML private VBox menuLateral;
    @FXML private TextArea outputArea;
    @FXML
    private VBox contenedor;
    private VentanaEmpleado ventanaEmpleado;
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
    private void mostrarSubmenuEmpleados() {
        menuLateral.getChildren().clear();
     //   menuLateral.getChildren().addAll(
             //   crearBoton("Ver Empleados", e -> ventanaEmpleado.verEmpleados()),
                //   crearBoton("Agregar Empleado", e -> ventanaEmpleado.agregarEmpleado()),
                //  crearBoton("Modificar Empleado", e -> ventanaEmpleado.modificarEmpleado()),
                // crearBoton("Eliminar Empleado", e -> ventanaEmpleado.EliminarEmpleado()),
                // crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
     //   );
    }
    private void mostrarSubmenuVentas() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Ventas", e -> ventanaVentas.verVentas()),
                crearBoton("Nueva Venta", e -> ventanaVentas.registrarVenta()),
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
    // FABRICADOR DE BOTONES
    private Button crearBoton(String texto, javafx.event.EventHandler<javafx.event.ActionEvent> evento) {
        Button btn = new Button(texto);
        btn.setOnAction(evento);
        btn.getStyleClass().add("boton-menu");
        btn.setPrefWidth(160);
        return btn;
    }
    //----------------------------------------------------------------------------

    private void verReportes() {
        outputArea.setText("📊 Mostrar reportes de ventas/clientes/productos.");
    }

}
