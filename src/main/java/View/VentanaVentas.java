package View;

import Clases.Principales.Cliente;
import Clases.Principales.Empleado;
import Clases.Principales.Producto;
import Clases.Principales.Venta;
import DataBase.DataBaseConnection;
import DataBase.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Mapper;
import util.Tablas;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static View.AppController.agregarDobleClickSeleccion;
import static View.AppController.mostrarAlerta; // te copie papu
import static View.VentanaProducto.mostrarVentanaSeleccionProducto;

public class VentanaVentas {
    @FXML
    private TextArea outputArea;
    @FXML
    private VBox contenedor;

    private static final Connection conn = DataBaseConnection.getConnection();

    public VentanaVentas(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public void verVentas() {
        contenedor.getChildren().clear();

        VBox tarjeta = new VBox(15);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.getStyleClass().add("card");

        // Título
        Label titulo = new Label("📊 Lista de Ventas");
        titulo.getStyleClass().add("titulo-seccion");

        // Barra de búsqueda por cliente
        HBox barraSuperior = new HBox(10);
        barraSuperior.setAlignment(Pos.CENTER_LEFT);

        Label lblBuscar = new Label("🔍 Ver por Cliente:");
        TextField campoCliente = new TextField();
        campoCliente.setPromptText("ID Cliente");
        campoCliente.setPrefWidth(120);

        Button btnAbrirLista = new Button("Seleccionar Cliente");
        btnAbrirLista.getStyleClass().add("btn-azul");

        // Acción para abrir la ventana de selección
        btnAbrirLista.setOnAction(e -> mostrarVentanaSeleccionCliente(campoCliente));

        Button btnFiltrar = new Button("Buscar");
        btnFiltrar.getStyleClass().add("btn-verde");

        barraSuperior.getChildren().addAll(lblBuscar, campoCliente, btnAbrirLista, btnFiltrar);

        // Tabla de ventas
        String[][] columnas = {
                {"Factura", "idVenta"},
                {"Cliente", "nombreCliente"},
                {"Empleado", "nombreEmpleado"},
                {"Producto", "notas"},
                {"Cantidad", "cantidad"},
                {"Fecha", "fechaFormateada"},
                {"Pago", "medioPago"},
                {"Subtotal", "subtotal"},
                {"Total", "importeTotal"},
        };
        TableView<Venta> tabla = Tablas.crearTabla(Venta.class, columnas);

        // Cargar todas las ventas por defecto
        ArrayList<Venta> lista = VentaDAO.cargarVentasEnLista();
        tabla.getItems().addAll(lista);

        Label lblCantidad = new Label("Total de ventas: " + lista.size());
        lblCantidad.getStyleClass().add("label-cantidad");

        // Acción del botón de búsqueda
        btnFiltrar.setOnAction(e -> {
            String idTexto = campoCliente.getText().trim();
            if (idTexto.isEmpty()) {
                ArrayList<Venta> todas = VentaDAO.cargarVentasEnLista();
                tabla.getItems().setAll(todas);
                lblCantidad.setText("Total de ventas: " + todas.size());
            } else {
                try {
                    int idCliente = Integer.parseInt(idTexto);
                    ArrayList<Venta> filtradas = VentaDAO.obtenerVentasPorCliente(idCliente);
                    tabla.getItems().setAll(filtradas);
                    lblCantidad.setText("Ventas del cliente: " + filtradas.size());
                } catch (NumberFormatException ex) {
                    mostrarAlerta("ID inválido.");
                }
            }
        });
        campoCliente.setOnAction(e -> btnFiltrar.fire()); //ENTER para filtrar
        VBox.setVgrow(tarjeta, Priority.ALWAYS); // Esto permite que se expanda verticalmente si hay espacio
        VBox.setVgrow(tabla, Priority.ALWAYS);
        tabla.setMaxHeight(Double.MAX_VALUE); // para que no se achique


        // Armado final
        tarjeta.getChildren().addAll(titulo, barraSuperior, tabla, lblCantidad);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void registrarVenta() {
        contenedor.getChildren().clear();

        Label titulo = new Label("📋 Registrar Venta");
        titulo.getStyleClass().add("titulo-principal");

        Map<String, Object> campos = new HashMap<>();
        Map<String, Runnable> acciones = new HashMap<>();

        acciones.put("productoID", () -> mostrarVentanaSeleccionProducto((TextField) campos.get("productoID")));
        acciones.put("idC", () -> mostrarVentanaSeleccionCliente((TextField) campos.get("idC")));
        acciones.put("idE", () -> mostrarVentanaSeleccionEmpleado((TextField) campos.get("idE")));

        String[][] lineas = {
                {"ID", "productoID", "select"},
                {"Cantidad", "cantidad", "text"},
                {"Cliente", "idC", "select"},
                {"Empleado", "idE", "select"},
                {"Forma de Pago", "formaPago", "combo"},
                {"Fecha de Venta", "fechaVenta", "date"},
        };
        VBox formCampos = Tablas.formtoAddEntidad(lineas, campos, acciones);

        ComboBox<String> comboFormaPago = (ComboBox<String>) campos.get("formaPago");
        comboFormaPago.getItems().addAll("Efectivo", "Debito", "Credito", "Transferencia", "MercadoPago");
        comboFormaPago.getStyleClass().add("choice-box");

        Label lblTotalVenta = new Label("Total de la venta: $0.00");
        lblTotalVenta.getStyleClass().add("etiqueta-total");

        // Actualizar total en tiempo real
        Runnable actualizarTotal = () -> {
            try {
                int cantidad = Integer.parseInt(((TextField) campos.get("cantidad")).getText().trim());
                int idProd = Integer.parseInt(((TextField) campos.get("productoID")).getText().trim());
                Producto p = ProductoDAO.obtenerProductoPorID(idProd);
                if (p != null) {
                    BigDecimal precio = p.getPrecioUnitario();
                    BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));
                    lblTotalVenta.setText("Total de la venta: $" + total);
                }
            } catch (Exception e) {
            }
        };

        ((TextField) campos.get("cantidad")).textProperty().addListener((obs, oldVal, newVal) -> actualizarTotal.run());
        ((TextField) campos.get("productoID")).textProperty().addListener((obs, oldVal, newVal) -> actualizarTotal.run());

        Button btnRegistrar = new Button("✅ Registrar Venta");
        btnRegistrar.getStyleClass().add("boton-accion");
        btnRegistrar.setOnAction(e -> {
            try {
                int productoId = Integer.parseInt(((TextField) campos.get("productoID")).getText().trim());
                int clienteId = Integer.parseInt(((TextField) campos.get("idC")).getText().trim());
                int empleadoId = Integer.parseInt(((TextField) campos.get("idE")).getText().trim());
                int cantidad = Integer.parseInt(((TextField) campos.get("cantidad")).getText().trim());
                String medio = ((ComboBox<String>) campos.get("formaPago")).getValue();

                if (medio == null) { //Mini validaciones MV
                    mostrarAlerta("⚠ Seleccioná una forma de pago.");
                    return;
                }
                Producto prod = ProductoDAO.obtenerProductoPorID(productoId);
                if (prod == null) { //MV
                    mostrarAlerta("❌ Producto no encontrado.");
                    return;
                }

                BigDecimal precio = prod.getPrecioUnitario();
                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
                BigDecimal total = BigDecimal.valueOf(0);
                total = total.add(subtotal);

                int idPago = switch (medio) {
                    case "Efectivo" -> 1;
                    case "Debito" -> 2;
                    case "Credito" -> 3;
                    case "Transferencia" -> 4;
                    case "MercadoPago" -> 5;
                    default -> throw new IllegalArgumentException("Forma de pago inválida.");
                };
                Venta venta = new Venta();
                venta.setIdProducto(productoId);
                venta.setIdCliente(clienteId);
                venta.setIdEmpleado(empleadoId);
                venta.setIdFormaDePago(idPago);
                venta.setCantidad(cantidad);
                venta.setSubtotal(subtotal);
                venta.setImporteTotal(total);
                venta.setFecha(((DatePicker) campos.get("fechaVenta")).getValue().atStartOfDay());

                String sql = "INSERT INTO Venta (idProd, idC, idE, formaDePago, cantidad, fecha, subtotal, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    Mapper.setVenta(stmt, venta);
                    stmt.executeUpdate();
                }
                mostrarAlerta("✅ Venta registrada correctamente.");
                boolean exito = VentaDAO.actualizarStockProducto(productoId, cantidad);
                if (!exito) {
                    mostrarAlerta("⚠ No se pudo actualizar el stock.");
                }

                verVentas();

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        Button btnCancelar = new Button("❌ Cancelar Venta");
        btnCancelar.getStyleClass().add("boton-cancelar");
        btnCancelar.setOnAction(e -> verVentas());

        VBox formFinal = new VBox(10, titulo, formCampos, lblTotalVenta, btnRegistrar, btnCancelar);
        formFinal.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(formFinal);
    }

    public void mostrarVentanaSeleccionCliente(TextField campoDestino) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Cliente");
        ventana.initModality(Modality.APPLICATION_MODAL);
        String [][] columnas = {
            {"ID", "id"},
            {"DNI", "DNI"},
            {"Nombre", "nombre"},
            {"Apellido", "apellido"},
        };
        TableView<Cliente> tabla = Tablas.crearTabla(Cliente.class, columnas);

        ArrayList<Cliente> lista = new ArrayList<>();
        ClienteDAO.cargarClientesEnLista(lista);
        tabla.getItems().addAll(lista);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Ingrese DNI del cliente");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            String dni = txtBuscar.getText().trim();
            ArrayList<Cliente> resultado = ClienteDAO.buscarClientePorDato("DNI", dni);
            tabla.getItems().setAll(resultado);
        });

        agregarDobleClickSeleccion(tabla, cliente -> {
            campoDestino.setText(String.valueOf(cliente.getId()));
            ventana.close();
        });

        VBox layout = new VBox(10, tabla, txtBuscar, btnBuscar);
        layout.setPadding(new Insets(10));

        Scene escena = new Scene(layout, 600, 400);
        ventana.setScene(escena);
        ventana.showAndWait();
    }

    private void mostrarVentanaSeleccionEmpleado(TextField campoDestino) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Empleado");
        ventana.initModality(Modality.APPLICATION_MODAL);

        String[][] columnas = {
                {"ID", "empleadoID"},
                {"DNI", "dni"},
                {"Nombre", "nombre"},
                {"Apellido", "apellido"},
        };
        TableView<Empleado> tabla3 = Tablas.crearTabla(Empleado.class, columnas);

        ArrayList<Empleado> lista = new ArrayList<>();
        EmpleadoDAO.cargarEmpleadosEnLista(lista);
        tabla3.getItems().addAll(lista);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Ingrese DNI del empleado");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
                String dni = txtBuscar.getText().trim();
                ArrayList<Empleado> resultado = EmpleadoDAO.BuscarEmpleadoPorDato("dni", dni);
                tabla3.getItems().setAll(resultado);
            });

            agregarDobleClickSeleccion(tabla3, empleado -> {    // ✅ Doble click para seleccionar automáticamente
                campoDestino.setText(String.valueOf(empleado.getEmpleadoID()));
                ventana.close();
            });

        VBox layout = new VBox(10, tabla3, txtBuscar, btnBuscar);
        layout.setPadding(new Insets(10));

        Scene escena = new Scene(layout, 600, 400);
        ventana.setScene(escena);
        ventana.showAndWait();
    }
}