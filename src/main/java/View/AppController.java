package View;

import Clases.Principales.*;

import Clases.Extras.Telefono;

import DataBase.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



public class AppController {
    @FXML private VBox menuLateral;
    @FXML private TextArea outputArea;
    @FXML
    private VBox contenidoPrincipal;
    @FXML
    private VBox contenedor;

    private TextField txtClienteId;
    private TextField txtEmpleadoId;

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

        TextField txtID = new TextField();
        TextField txtDNI = new TextField();
        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        //TextField txtGenero = new TextField();
        //TextField txtNacionalidad = new TextField();
        TextField txtTipoCliente = new TextField();
        TextField txtCantidadCompras = new TextField();
        TextField txtTelefono = new TextField();

        txtID.setPromptText("ID");
        txtDNI.setPromptText("DNI");
        txtNombre.setPromptText("Nombre");
        txtApellido.setPromptText("Apellido");
        //txtGenero.setPromptText("Género");
        //txtNacionalidad.setPromptText("Nacionalidad");
        txtTipoCliente.setPromptText("Tipo de Cliente");
        txtCantidadCompras.setPromptText("Cantidad de Compras");
        txtTelefono.setPromptText("Telefono");

        Button btnGuardar = new Button("Guardar");

        btnGuardar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtID.getText().trim()); //es autogenerado
                int dni = Integer.parseInt(txtDNI.getText().trim());
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                //int genero = Integer.parseInt(txtGenero.getText().trim());
                //int nacionalidad = Integer.parseInt(txtNacionalidad.getText().trim());
                int tipoCliente = Integer.parseInt(txtTipoCliente.getText().trim());
                int cantCompras = Integer.parseInt(txtCantidadCompras.getText().trim());
                long telefonox = Long.parseLong(txtTelefono.getText().trim());

                Telefono telefono = new Telefono(dni, telefonox);
                // la unica forma de asignarle datetime.today en java que encontre

                // Faltaria Llamar una funcion con validaciones acá. O en el diseño usar una herramienta para validar como son los RequiredFieldValidator en el VS

                //Cliente cliente = new Cliente(dni, nombre, apellido, id, tipoCliente, cantCompras); // falta fechaAlta
                //ClienteDAO.insertar(cliente);

                Cliente c = new Cliente(
                        dni,
                        nombre,
                        apellido,
                        id,
                        tipoCliente,
                        cantCompras,
                        telefono
                );
                Persona p = c;

                VentanaClientes.insertar(c, p);
                outputArea.setText(VentanaClientes.insertar(c, p));
                outputArea.setText("✅ Cliente agregado correctamente.");
                ventana.close();
            } catch (NumberFormatException ex) {
                outputArea.setText("❌ Error: Verificá que todos los campos numéricos tengan un valor válido.");
            }
        });

        VBox layout = new VBox(10, txtID, txtDNI, txtNombre, txtApellido, /* txtGenero, txtNacionalidad, */ txtTipoCliente, txtCantidadCompras, txtTelefono, btnGuardar);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 350, 500);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
    }

    private void modificarCliente() {
        outputArea.setText("✏️ Función modificar cliente (en construcción)");
    }

    // VENTAS
    private void verVentas() {
        ArrayList<Venta> lista = VentanaVentas.cargarVentasEnLista();
        outputArea.setText(VentanaVentas.obtenerVentas(lista));
    }

    public void registrarVenta() {
        contenedor.getChildren().clear();

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.getStyleClass().add("form-box");

        Label titulo = new Label("📋 Registrar Venta");
        titulo.getStyleClass().add("titulo-principal");

        TextField txtFactura = new TextField();
        txtFactura.setPromptText("N° Factura");
        txtFactura.getStyleClass().add("text-field");

        TextField txtProductoId = new TextField();
        txtProductoId.setPromptText("ID Producto");
        txtProductoId.getStyleClass().add("text-field");

        Button btnAgregarProducto = new Button(" Agregar Producto");
        btnAgregarProducto.getStyleClass().add("boton-secundario");
        btnAgregarProducto.setOnAction(e -> mostrarVentanaSeleccionProducto(txtProductoId));

        TextField txtClienteId = new TextField();
        txtClienteId.setPromptText("ID Cliente");
        txtClienteId.getStyleClass().add("text-field");

        TextField txtEmpleadoId = new TextField();
        txtEmpleadoId.setPromptText("ID Empleado");
        txtEmpleadoId.getStyleClass().add("text-field");

        ComboBox<String> medioPago = new ComboBox<>();
        medioPago.getItems().addAll("Efectivo", "Crédito", "Débito", "Transferencia");
        medioPago.setPromptText("Medio de Pago");
        medioPago.getStyleClass().add("choice-box");

        TextField txtTotal = new TextField();
        txtTotal.setPromptText("Total");
        txtTotal.getStyleClass().add("text-field");

        Button btnAgregarCliente = new Button(" Agregar Cliente");
        btnAgregarCliente.getStyleClass().add("boton-secundario");
        btnAgregarCliente.setOnAction(e -> mostrarVentanaSeleccionCliente(txtClienteId));

        Button btnAgregarEmpleado = new Button(" Agregar Empleado");
        btnAgregarEmpleado.getStyleClass().add("boton-secundario");
        btnAgregarEmpleado.setOnAction(e -> mostrarVentanaSeleccionEmpleado(txtEmpleadoId));

        Button btnRegistrar = new Button("✅ Registrar Venta");
        btnRegistrar.getStyleClass().add("boton-accion");
        btnRegistrar.setOnAction(e -> {
            try (Connection conn = DataBaseConnection.getConnection()) {
                int nFactura = Integer.parseInt(txtFactura.getText().trim());
                int clienteId = Integer.parseInt(txtClienteId.getText().trim());
                int empleadoId = Integer.parseInt(txtEmpleadoId.getText().trim());
                String medio = medioPago.getValue();
                BigDecimal total = new BigDecimal(txtTotal.getText().trim());
                BigDecimal subtotal = total;
                int productoId = Integer.parseInt(txtProductoId.getText().trim());

                int idPago = switch (medio) {
                    case "Efectivo" -> 1;
                    case "Crédito" -> 2;
                    case "Débito" -> 3;
                    case "Transferencia" -> 4;
                    default -> throw new IllegalArgumentException("Forma de pago inválida.");
                };

                String sql = "INSERT INTO Venta (nFactura, idProd, idC, idE, formaDePago, fecha, subtotal, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, nFactura);
                stmt.setInt(2, productoId);
                stmt.setInt(3, clienteId);
                stmt.setInt(4, empleadoId);
                stmt.setInt(5, idPago);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                stmt.setString(6, LocalDateTime.now().format(formatter));
                stmt.setBigDecimal(7, subtotal);
                stmt.setBigDecimal(8, total);

                stmt.executeUpdate();
                mostrarAlerta("✅ Venta registrada correctamente.");
                contenedor.getChildren().clear();  // Vaciamos el formulario
                contenedor.getChildren().add(outputArea); // Volvemos a mostrar el área de texto

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                titulo,
                txtFactura,
                new HBox(10, txtProductoId, btnAgregarProducto),
                new HBox(10, txtClienteId, btnAgregarCliente),
                new HBox(10, txtEmpleadoId, btnAgregarEmpleado),
                medioPago,
                txtTotal,
                btnRegistrar
        );

        Button btnCancelar = new Button("❌ Cancelar Venta");
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



    private void mostrarVentanaSeleccionCliente(TextField idClienteField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Cliente");
        ventana.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        ArrayList<Cliente> clientes = new ArrayList<>();
        VentanaClientes.cargarClientesEnLista(clientes);

        TextArea areaTexto = new TextArea(VentanaClientes.obtenerTextoClientes(clientes));
        areaTexto.setEditable(false);
        areaTexto.setWrapText(true);
        areaTexto.setPrefHeight(300);

        TextField dniInput = new TextField();
        dniInput.setPromptText("Ingrese DNI del cliente");

        Button buscarBtn = new Button("🔍 Buscar");
        buscarBtn.setOnAction(e -> {
            try {
                int dni = Integer.parseInt(dniInput.getText().trim());
                Integer id = VentanaClientes.obtenerIdClientePorDni(dni);

                if (id != null) {
                    idClienteField.setText(id.toString());
                    ventana.close();
                } else {
                    mostrarAlerta("No se encontró ningún cliente con ese DNI.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("DNI inválido.");
            }
        });

        root.getChildren().addAll(areaTexto, dniInput, buscarBtn);

        Scene scene = new Scene(root, 600, 400);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

    private void mostrarVentanaSeleccionEmpleado(TextField idEmpleadoField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Empleado");
        ventana.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        ArrayList<Empleado> empleados = new ArrayList<>();
        VentanaEmpleado.cargarEmpleadosEnLista(empleados);

        TextArea areaTexto = new TextArea(VentanaEmpleado.obtenerTextoEmpleados(empleados));
        areaTexto.setEditable(false);
        areaTexto.setWrapText(true);
        areaTexto.setPrefHeight(300);

        TextField dniInput = new TextField();
        dniInput.setPromptText("Ingrese DNI del empleado");

        Button buscarBtn = new Button("🔍 Buscar");
        buscarBtn.setOnAction(e -> {
            try {
                int dni = Integer.parseInt(dniInput.getText().trim());
                Integer id = VentanaEmpleado.obtenerIdEmpleadoPorDni(dni);

                if (id != null) {
                    idEmpleadoField.setText(id.toString());
                    ventana.close();
                } else {
                    mostrarAlerta("No se encontró ningún empleado con ese DNI.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("DNI inválido.");
            }
        });

        root.getChildren().addAll(areaTexto, dniInput, buscarBtn);

        Scene scene = new Scene(root, 600, 400);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

    private void mostrarVentanaSeleccionProducto(TextField idProductoField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Producto");
        ventana.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        ArrayList<Producto> productos = new ArrayList<>();
        VentanaProducto.cargarProductosEnLista(productos);

        TextArea areaTexto = new TextArea(VentanaProducto.obtenerTextoProductos(productos));
        areaTexto.setEditable(false);
        areaTexto.setWrapText(true);
        areaTexto.setPrefHeight(300);

        TextField idInput = new TextField();
        idInput.setPromptText("Ingrese ID del producto");

        Button buscarBtn = new Button("🔍 Seleccionar");
        buscarBtn.setOnAction(e -> {
            try {
                int idProducto = Integer.parseInt(idInput.getText().trim());
                boolean existe = productos.stream().anyMatch(p -> p.getProductoID() == idProducto);

                if (existe) {
                    idProductoField.setText(String.valueOf(idProducto));
                    ventana.close();
                } else {
                    mostrarAlerta("No se encontró ningún producto con ese ID.");
                }

            } catch (NumberFormatException ex) {
                mostrarAlerta("ID inválido.");
            }
        });

        root.getChildren().addAll(areaTexto, idInput, buscarBtn);

        Scene scene = new Scene(root, 650, 400);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
