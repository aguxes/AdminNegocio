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

    @FXML
    public void initialize() {
        ventanaProducto = new VentanaProducto(contenedor, outputArea);
        ventanaVentas = new VentanaVentas(contenedor, outputArea);

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
                crearBoton("Ver Clientes", e -> verClientes()),
                crearBoton("Agregar Cliente", e -> agregarCliente()),
                crearBoton("Modificar Cliente", e -> modificarCliente()),
                crearBoton("Eliminar Cliente", e -> EliminarCliente()),
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

    //FUNCIONES DE APP CONTROLLER
    public static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }



    //-------------------

    private void mostrarSubmenuReportes() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Reportes", e -> verReportes()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }
    //CLIENTE

    private void agregarCliente() {
        contenedor.getChildren().clear();

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.getStyleClass().add("form-box");

        Label titulo = new Label("📋 Registrar Cliente");
        titulo.getStyleClass().add("titulo-principal");

        TextField txtDNI = new TextField();
        txtDNI.setPromptText("DNI");
        txtDNI.getStyleClass().add("text-field");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.getStyleClass().add("text-field");

        TextField txtApellido = new TextField();
        txtApellido.setPromptText("Apellido");
        txtApellido.getStyleClass().add("text-field");

        TextField txtTipo = new TextField();
        txtTipo.setPromptText("Tipo de Cliente");
        txtTipo.getStyleClass().add("text-field");

        TextField txtCantCompras = new TextField();
        txtCantCompras.setPromptText("Cantidad de Compras");
        txtCantCompras.getStyleClass().add("text-field");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");
        txtTelefono.getStyleClass().add("text-field");

        Button btnRegistrarc = new Button("✅ Registrar Cliente");
        btnRegistrarc.getStyleClass().add("boton-accion");
        btnRegistrarc.setOnAction(e -> {
            try {
                int DNI = Integer.parseInt(txtDNI.getText().trim());
                String Nombre = txtNombre.getText().trim();
                String Apellido = txtApellido.getText().trim();
                int tipoId = Integer.parseInt(txtTipo.getText().trim());
                int cantCompras = Integer.parseInt(txtCantCompras.getText().trim());
                Long telefono = Long.parseLong(txtTelefono.getText().trim());

                TiposClientes tipo = new TiposClientes(tipoId, "");
                Telefono tel = new Telefono(DNI, telefono);

                Cliente c = new Cliente();
                c.setDNI(DNI);
                c.setNombre(Nombre);
                c.setApellido(Apellido);
                c.setTipCliente(tipo);
                c.setCantCompras(cantCompras);
                c.setTelefono(tel);

                String queryP = """
                INSERT INTO Persona (dni, nombre, apellido ) VALUES (?, ?, ?)
                """;
                String queryC = """
                INSERT INTO Cliente (dni, idTipo, cantCompras ) VALUES (?, ?, ?)
                """;
                String queryT = """
                INSERT INTO Telefonos (idPersona, telefono) VALUES (?, ?)
                """;

                PreparedStatement stmtP = conn.prepareStatement(queryP);
                PreparedStatement stmtC = conn.prepareStatement(queryC);
                PreparedStatement stmtT = conn.prepareStatement(queryT);

                Mapper.setPersona(stmtP, c);
                Mapper.setCliente(stmtC, c);
                Mapper.setTelefono(stmtT, c);

                stmtP.executeUpdate();
                stmtC.executeUpdate();
                stmtT.executeUpdate();

                mostrarAlerta("✅ Cliente registrado correctamente.");
                contenedor.getChildren().clear();
                contenedor.getChildren().add(outputArea);

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                titulo,
                txtDNI,
                txtNombre,
                txtApellido,
                txtTipo,
                txtCantCompras,
                txtTelefono,
                btnRegistrarc
        );

        Button btnCancelar = new Button("❌ Cancelar nuevo Cliente");
        btnCancelar.getStyleClass().add("boton-cancelar");
        btnCancelar.setOnAction(e -> {
            contenedor.getChildren().clear();
            contenedor.getChildren().add(outputArea);
        });

        HBox filaCancelar = new HBox(btnCancelar);
        filaCancelar.setAlignment(Pos.BOTTOM_RIGHT);

        form.getChildren().add(filaCancelar);


        contenedor.getChildren().add(form);
    }

    private void modificarCliente() {
        outputArea.setText("✏️ Función modificar cliente (en construcción)");
    }

    private void verClientes() {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.getStyleClass().add("card");

        Label lblCampo = new Label("Buscar por:");
        lblCampo.getStyleClass().add("label-form");

        ChoiceBox<String> choiceCampo = new ChoiceBox<>();
        choiceCampo.getItems().addAll("nombre", "apellido", "ID", "DNI", "tipo", "cantCompras", "telefono");
        choiceCampo.setValue("nombre");
        choiceCampo.getStyleClass().add("input-form");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: Juan o 2");
        txtValor.getStyleClass().add("input-form");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");

        TableView<Cliente> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay clientes cargados."));
        tabla.getStyleClass().add("tabla-clientes");

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, Integer> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipCliente"));

        TableColumn<Cliente, Integer> colCompras = new TableColumn<>("Compras");
        colCompras.setCellValueFactory(new PropertyValueFactory<>("cantCompras"));

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoStr"));

        tabla.getColumns().addAll(colId, colDni, colNombre, colApellido, colTipo, colCompras, colTelefono);

        // Cargar todos los clientes por defecto
        ArrayList<Cliente> listaInicial = new ArrayList<>();
        VentanaClientes.cargarClientesEnLista(listaInicial);
        tabla.getItems().addAll(listaInicial);

        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();
            ArrayList<Cliente> resultado = VentanaClientes.buscarClientePorDato(campo, valor);

            if (resultado.isEmpty()) {
                VBox card = new VBox();
                card.getStyleClass().add("card-error");

                Label titulo = new Label("Sin resultados");
                titulo.getStyleClass().add("card-error-titulo");

                Label mensaje = new Label("No se encontró ningún cliente con ese dato.");
                mensaje.getStyleClass().add("card-error-mensaje");

                Button volverBtn = new Button("Volver");
                volverBtn.getStyleClass().add("btn-error-volver");
                volverBtn.setOnAction(ev -> contenedor.getChildren().setAll(tarjeta));

                card.getChildren().addAll(titulo, mensaje, volverBtn);
                contenedor.getChildren().setAll(card);
            } else {
                tabla.getItems().setAll(resultado);
            }
        });

        tarjeta.getChildren().addAll(lblCampo, choiceCampo, txtValor, btnBuscar, tabla);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void EliminarCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Eliminar Cliente");

        TextField txtId = new TextField();
        txtId.setPromptText("ID del cliente");

        Label lblConfirmacion = new Label();

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = VentanaClientes.buscarNombrePorId(id);
            if (nombre != null) {
                lblConfirmacion.setText("¿Eliminar a " + nombre + "?");
            } else {
                lblConfirmacion.setText("Cliente no encontrado.");
            }
        });

        Button btnEliminar = new Button("Sí, eliminar");
        btnEliminar.setOnAction(e -> {
            VentanaClientes.eliminarPorId(Integer.parseInt(txtId.getText()));
            ventana.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnEliminar, btnCancelar);
        botones.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, new Label("ID Cliente:"), txtId, btnBuscar, lblConfirmacion, botones);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 300, 250);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
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
