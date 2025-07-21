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

    @FXML
    public void initialize() {
        ventanaProducto = new VentanaProducto(contenedor, outputArea);
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
                crearBoton("Buscar Cliente", e -> buscarCliente()),
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
        contenedor.getChildren().clear();

        TableView<Cliente> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay clientes cargados."));

        TableColumn<Cliente, Integer> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("DNI"));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo Cliente");
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo().getDescripcion()));

        TableColumn<Cliente, Integer> colCompras = new TableColumn<>("Compras");
        colCompras.setCellValueFactory(new PropertyValueFactory<>("cantCompras"));

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tabla.getColumns().addAll( colId, colDni, colNombre, colApellido, colTipo, colCompras, colTelefono);

        // Cargar datos
        ArrayList<Cliente> clientes = new ArrayList<>();
        VentanaClientes.cargarClientesEnLista(clientes);
        tabla.setItems(FXCollections.observableArrayList(clientes));

        VBox layout = new VBox(10, new Label("👤 Lista de clientes"), tabla);
        layout.setPadding(new Insets(20));

        contenedor.getChildren().add(layout);
    }

    private void buscarCliente() {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.CENTER_LEFT);

        Label lblCampo = new Label("Buscar por:");
        ChoiceBox<String> choiceCampo = new ChoiceBox<>();
        choiceCampo.getItems().addAll("nombre", "apellido", "ID", "DNI", "tipo", "cantCompras", "telefono");
        choiceCampo.setValue("nombre");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: Juan o 2");

        Button btnBuscar = new Button("Buscar");

        TableView<Cliente> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();
            ArrayList<Cliente> lista = VentanaClientes.buscarClientePorDato(campo, valor);

            if (lista.isEmpty()) {
                VBox card = new VBox();
                card.getStyleClass().add("card-error");

                Label titulo = new Label("Sin resultados");
                titulo.getStyleClass().add("card-error-titulo");

                Label mensaje = new Label("No se encontró ningún cliente con ese dato.");
                mensaje.getStyleClass().add("card-error-mensaje");

                Button volverBtn = new Button("Volver a buscar");
                volverBtn.getStyleClass().add("btn-error-volver");
                volverBtn.setOnAction(ev -> contenedor.getChildren().setAll(tarjeta));

                card.getChildren().addAll(titulo, mensaje, volverBtn);
                contenedor.getChildren().setAll(card);
            }


            tabla.getItems().setAll(lista);
        });

        //Estilos
        tarjeta.getStyleClass().add("card");
        lblCampo.getStyleClass().add("label-form");
        choiceCampo.getStyleClass().add("input-form");
        txtValor.getStyleClass().add("input-form");
        btnBuscar.getStyleClass().add("btn-verde");
        tabla.getStyleClass().add("tabla-clientes");


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


    // VENTAS
    public void verVentas() {
        contenedor.getChildren().clear();

        Label titulo = new Label("📊 Lista de Ventas");
        titulo.getStyleClass().add("titulo-seccion");

        TableView<Venta> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Venta, Integer> colFactura = new TableColumn<>("Factura");
        colFactura.setCellValueFactory(new PropertyValueFactory<>("idVenta"));

        TableColumn<Venta, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));

        TableColumn<Venta, String> colEmpleado = new TableColumn<>("Empleado");
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));

        TableColumn<Venta, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(new PropertyValueFactory<>("notas"));

        TableColumn<Venta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada")); // lo armamos abajo

        TableColumn<Venta, String> colPago = new TableColumn<>("Pago");
        colPago.setCellValueFactory(new PropertyValueFactory<>("medioPago"));

        TableColumn<Venta, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        TableColumn<Venta, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("importeTotal"));

        tabla.getColumns().addAll(colFactura, colCliente, colEmpleado, colProducto, colFecha, colPago, colSubtotal, colTotal);
        tabla.getItems().addAll(VentanaVentas.cargarVentasEnLista());

        VBox layout = new VBox(10, titulo, tabla);
        layout.setPadding(new Insets(15));

        contenedor.getChildren().add(layout);
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

        Label lblCantidad = new Label("Cantidad:");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Ej: 3");

        Button btnAgregarProducto = new Button(" Agregar Producto");
        btnAgregarProducto.getStyleClass().add("boton-secundario");
        btnAgregarProducto.setOnAction(e -> ventanaProducto.mostrarVentanaSeleccionProducto(txtProductoId));

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
            try {
                int productoId = Integer.parseInt(txtProductoId.getText().trim());
                int nFactura = Integer.parseInt(txtFactura.getText().trim());
                int clienteId = Integer.parseInt(txtClienteId.getText().trim());
                int empleadoId = Integer.parseInt(txtEmpleadoId.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String medio = medioPago.getValue();

                Producto prod = ProductoDAO.obtenerProductoPorID(productoId);
                if (prod == null) {
                    mostrarAlerta("❌ Producto no encontrado.");
                    return;
                }

                BigDecimal subtotal = prod.getPrecioUnitario().multiply(new BigDecimal(cantidad));
                BigDecimal total = subtotal; //dsp aca sirve para impuesto no?



                int idPago = switch (medio) {
                    case "Efectivo" -> 1;
                    case "Crédito" -> 2;
                    case "Débito" -> 3;
                    case "Transferencia" -> 4;
                    default -> throw new IllegalArgumentException("Forma de pago inválida.");
                };

                Venta venta = new Venta();
                venta.setIdVenta(nFactura);
                venta.setIdProducto(productoId);
                venta.setIdCliente(clienteId);
                venta.setIdEmpleado(empleadoId);
                venta.setIdFormaDePago(idPago);
                venta.setSubtotal(total);
                venta.setImporteTotal(total);

                String sql = "INSERT INTO Venta (nFactura, idProd, idC, idE, formaDePago, fecha, subtotal, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                Mapper.setVenta(stmt, venta);
                stmt.executeUpdate();
                mostrarAlerta("✅ Venta registrada correctamente.");
                contenedor.getChildren().clear();
                contenedor.getChildren().add(outputArea);

                boolean exito = VentanaVentas.actualizarStockProducto(productoId, cantidad);
                if (!exito) {
                    mostrarAlerta("⚠ No se pudo actualizar el stock.");
                }


            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                titulo,
                txtFactura,
                new HBox(10, txtProductoId, btnAgregarProducto),
                new HBox(10, lblCantidad, txtCantidad),
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

        TextArea areaTexto = new TextArea(VentanaClientes.obtenerClientes(clientes));
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

    public static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void ventasPorCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Ventas por Cliente");
        ventana.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        // Mostrar lista de clientes
        ArrayList<Cliente> clientes = new ArrayList<>();
        VentanaClientes.cargarClientesEnLista(clientes);
        TextArea areaTexto = new TextArea(VentanaClientes.obtenerClientes(clientes));
        areaTexto.setEditable(false);
        areaTexto.setWrapText(true);
        areaTexto.setPrefHeight(300);

        // Campo para ingresar DNI
        TextField dniInput = new TextField();
        dniInput.setPromptText("Ingrese DNI del cliente");

        Button buscarBtn = new Button("🔍 Buscar Ventas");
        buscarBtn.setOnAction(e -> {
            try {
                int dni = Integer.parseInt(dniInput.getText().trim());
                Integer id = VentanaClientes.obtenerIdClientePorDni(dni);

                if (id != null) {
                    ArrayList<Venta> ventas = VentanaVentas.obtenerVentasPorCliente(id);
                    outputArea.setText(VentanaVentas.obtenerVentas(ventas));
                    ventana.close();
                } else {
                    mostrarAlerta("❌ No se encontró ningún cliente con ese DNI.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("❌ DNI inválido.");
            }
        });

        layout.getChildren().addAll(areaTexto, dniInput, buscarBtn);
        Scene escena = new Scene(layout, 600, 450);
        ventana.setScene(escena);
        ventana.showAndWait();
    }


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
