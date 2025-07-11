package View;

import DataBase.ClienteDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Clases.Cliente;
import View.VentanaClientes;
import View.VentanaVentas;
import javafx.stage.Modality;
import javafx.stage.Stage;
import DataBase.ClienteDAO;

public class AppController {

    @FXML private VBox menuLateral;
    @FXML private TextArea outputArea;


    private Cliente clienteAEliminar; // Variable temporal para eliminar

    @FXML
    public void initialize() {
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

    private void mostrarSubmenuClientes() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Clientes", e -> verClientes()),
                crearBoton("Agregar Cliente", e -> agregarCliente()),
                crearBoton("Modificar Cliente", e -> modificarCliente()),
                crearBoton("Eliminar Cliente", e -> abrirVentanaEliminarCliente()),
                crearBoton("Buscar Cliente", e -> abrirVentanaBuscarCliente()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }

    private void mostrarSubmenuVentas() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Ventas", e -> verVentas()),
                crearBoton("Nueva Venta", e -> registrarVenta()),
                crearBoton("Ventas por Cliente", e -> ventasPorCliente()),
                crearBoton("🔙 Volver", e -> cargarMenuPrincipal())
        );
    }

    private void mostrarSubmenuInventario() {
        menuLateral.getChildren().clear();
        menuLateral.getChildren().addAll(
                crearBoton("Ver Productos", e -> verProductos()),
                crearBoton("Agregar Producto", e -> agregarProducto()),
                crearBoton("Modificar Producto", e -> modificarProducto()),
                crearBoton("Eliminar Producto", e -> eliminarProducto()),
                crearBoton("Buscar Producto", e -> buscarProducto()),
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

    public void abrirVentanaBuscarCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Buscar Cliente");

        Label lblCampo = new Label("Buscar por:");
        ChoiceBox<String> choiceCampo = new ChoiceBox<>();
        choiceCampo.getItems().addAll("nombre", "email");
        choiceCampo.setValue("nombre");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: Juan o juan@mail.com");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();

            String resultado = VentanaClientes.buscarClientePorDato(campo, valor);
            outputArea.setText(resultado);

            ventana.close(); // Se cierra si querés automático al buscar
        });

        VBox layout = new VBox(10, lblCampo, choiceCampo, txtValor, btnBuscar);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 300, 200);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
    }

    public void abrirVentanaEliminarCliente() {
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


    // CLIENTES
    private void verClientes() {
        var lista = new java.util.ArrayList<Cliente>();
        VentanaClientes.cargarClientesEnLista(lista);
        outputArea.setText(VentanaClientes.obtenerTextoClientes(lista));
    }

    private void agregarCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Agregar Cliente");

        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        TextField txtDNI = new TextField();
        TextField txtEmail = new TextField();
        TextField txtTelefono = new TextField();
        TextField txtLocalidad = new TextField();

        txtNombre.setPromptText("Nombre");
        txtApellido.setPromptText("Apellido");
        txtDNI.setPromptText("DNI");
        txtEmail.setPromptText("Email");
        txtTelefono.setPromptText("Teléfono");
        txtLocalidad.setPromptText("Localidad");

        Button btnGuardar = new Button("Guardar");

        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dni = txtDNI.getText().trim();
            String email = txtEmail.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String localidad = txtLocalidad.getText().trim();

            // Faltaria Llamar una funcion con validaciones acá. O en el diseño usar una herramienta para validar como son los RequiredFieldValidator en el VS

            Cliente cliente = new Cliente(nombre, apellido, dni, email, telefono, localidad);
            ClienteDAO.insertar(cliente);

            outputArea.setText("✅ Cliente agregado correctamente.");
            ventana.close();
        });

        VBox layout = new VBox(10, txtNombre, txtApellido, txtDNI, txtEmail, txtTelefono, txtLocalidad, btnGuardar);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 350, 350);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
    }

    private void modificarCliente() {
        outputArea.setText("✏️ Función modificar cliente (en construcción)");
    }


    // VENTAS
    private void verVentas() {
        outputArea.setText(VentanaVentas.obtenerVentasComoTexto());
    }

    private void registrarVenta() {
        outputArea.setText("🧾 Lógica para registrar una nueva venta.");
    }

    private void ventasPorCliente() {
        outputArea.setText("📄 Ventas filtradas por cliente.");
    }

    // INVENTARIO
    private void verProductos() {
        outputArea.setText("📦 Lista de productos...");
    }

    private void agregarProducto() {
        outputArea.setText("➕ Formulario para agregar producto.");
    }

    private void modificarProducto() {
        outputArea.setText("✏️ Editar un producto existente.");
    }

    private void eliminarProducto() {
        outputArea.setText("🗑️ Eliminar producto por ID.");
    }

    private void buscarProducto() {
        outputArea.setText("🔍 Buscar producto por nombre o ID.");
    }

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
