package View;

import Clases.Extras.Telefono;
import Clases.Extras.TiposClientes;
import Clases.Principales.Cliente;
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
import java.sql.*;
import java.util.ArrayList;

import static View.AppController.mostrarAlerta;

public class VentanaClientes {
    @FXML private TextArea outputArea;
    @FXML private VBox contenedor;

    private static final Connection conn = DataBaseConnection.getConnection();

    public VentanaClientes(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public void verClientes() {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_LEFT);
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
        txtValor.setMaxWidth(220);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");


        String[][] columnas = { // array de strings para permitir el ingreso de datos llamados entre "".
                {"ID", "id"},
                {"DNI", "dni"},
                {"Nombre", "nombre"},
                {"Apellido", "apellido"},
                {"Tipo", "tipCliente"},
                {"Compras", "cantCompras"},
                {"Teléfono", "telefonoStr"}
        };

        TableView<Cliente> tabla = Tablas.crearTabla(Cliente.class, columnas);


        // Layout de búsqueda agrupado
        HBox barraBusqueda = new HBox(10); // espacio entre elementos
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.getChildren().addAll(lblCampo, choiceCampo, txtValor, btnBuscar);

        //anchos
        choiceCampo.setPrefWidth(140);
        txtValor.setPrefWidth(180);
        btnBuscar.setPrefWidth(100);


        // Cargar todos los clientes por defecto
        ArrayList<Cliente> listaInicial = new ArrayList<>();
        ClienteDAO.cargarClientesEnLista(listaInicial);
        tabla.getItems().addAll(listaInicial);

        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();
            ArrayList<Cliente> resultado = ClienteDAO.buscarClientePorDato(campo, valor);

            if (resultado.isEmpty()) {

                tabla.getItems().clear();
                mostrarAlerta("❌ No se encontró ningún cliente con ese dato.");
            } else {
                tabla.getItems().setAll(resultado);
            }

/*
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
            }*/
        });

        btnBuscar.setPrefWidth(240); // mismo ancho que txtValor
        btnBuscar.setAlignment(Pos.CENTER);

        tarjeta.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho disponible
        VBox.setVgrow(tarjeta, Priority.ALWAYS); // Opcional para que crezca si hay espacio

        tarjeta.getChildren().addAll(barraBusqueda, tabla);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void agregarCliente() {
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
        //Tamanios limite
        txtDNI.setMaxWidth(350);
        txtNombre.setMaxWidth(350);
        txtApellido.setMaxWidth(350);
        txtTipo.setMaxWidth(350);
        txtCantCompras.setMaxWidth(350);
        txtTelefono.setMaxWidth(350);


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

                String queryP = "INSERT INTO Persona (dni, nombre, apellido ) VALUES (?, ?, ?) ";
                String queryC = "INSERT INTO Cliente (dni, idTipo, cantCompras ) VALUES (?, ?, ?)";
                String queryT = "INSERT INTO Telefonos (idPersona, telefono) VALUES (?, ?)";

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
            verClientes();
        });

        HBox filaCancelar = new HBox(btnCancelar);
        filaCancelar.setAlignment(Pos.BOTTOM_RIGHT);

        form.setAlignment(Pos.TOP_CENTER);
        form.getChildren().add(filaCancelar);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(form);
    }

    public void modificarCliente() {
        contenedor.getChildren().clear();

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.getStyleClass().add("form-box");

        Label titulo = new Label("📋 Modificar Cliente");
        titulo.getStyleClass().add("titulo-principal");

        Label lblCampo = new Label("Buscar Cliente");
        lblCampo.getStyleClass().add("label-form");

        ChoiceBox<String> choiceCampo = new ChoiceBox<>();
        choiceCampo.getItems().addAll("ID");
        choiceCampo.setValue("ID");
        choiceCampo.getStyleClass().add("input-form");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: 1");
        txtValor.getStyleClass().add("input-form");
        txtValor.setMaxWidth(220);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");

        HBox barraBusqueda = new HBox(10, lblCampo, choiceCampo, txtValor, btnBuscar);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(10));

        TextField txtmDNI = new TextField();
        txtmDNI.setPromptText("DNI");
        //txtmDNI.setText(String.valueOf(cliente.getDNI()));
        txtmDNI.getStyleClass().add("text-field");

        txtmDNI.setEditable(false); // setea para que el txt no se pueda modificar

        TextField txtmNombre = new TextField();
        txtmNombre.setPromptText("Nombre");
        txtmNombre.getStyleClass().add("text-field");

        TextField txtmApellido = new TextField();
        txtmApellido.setPromptText("Apellido");
        txtmApellido.getStyleClass().add("text-field");

        TextField txtmTipo = new TextField();
        txtmTipo.setPromptText("Tipo de Cliente");
        txtmTipo.getStyleClass().add("text-field");

        TextField txtmCantCompras = new TextField();
        txtmCantCompras.setPromptText("Cantidad de Compras");
        txtmCantCompras.getStyleClass().add("text-field");

        TextField txtmTelefono = new TextField();
        txtmTelefono.setPromptText("Teléfono");
        txtmTelefono.getStyleClass().add("text-field");
        //Tamanios limite
        txtmDNI.setMaxWidth(350);
        txtmNombre.setMaxWidth(350);
        txtmApellido.setMaxWidth(350);
        txtmTipo.setMaxWidth(350);
        txtmCantCompras.setMaxWidth(350);
        txtmTelefono.setMaxWidth(350);

        btnBuscar.setOnAction(ev -> {
            try {
                int id = Integer.parseInt(txtValor.getText().trim());
                Cliente c = ClienteDAO.obtenerClientePorId(id);

                if (c != null) {
                    txtmDNI.setText(String.valueOf(c.getDNI()));
                    txtmNombre.setText(c.getNombre());
                    txtmApellido.setText(c.getApellido());
                    txtmTipo.setText(String.valueOf(c.getTipo().getTipo()));
                    txtmCantCompras.setText(String.valueOf(c.getCantCompras()));
                    txtmTelefono.setText(String.valueOf(c.getTelefono().getTelefono()));
                } else {
                    mostrarAlerta("❌ No se encontró el cliente con ID: " + id);
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("❌ Ingresá un número válido para el ID.");
            }
        });

        Button btnModificarc = new Button("✅ Modificar Cliente");
        btnModificarc.getStyleClass().add("boton-accion");
        btnModificarc.setOnAction(e -> {
            try {
                int DNI = Integer.parseInt(txtmDNI.getText().trim());
                String Nombre = txtmNombre.getText().trim();
                String Apellido = txtmApellido.getText().trim();
                int tipoId = Integer.parseInt(txtmTipo.getText().trim());
                int cantCompras = Integer.parseInt(txtmCantCompras.getText().trim());
                Long telefono = Long.parseLong(txtmTelefono.getText().trim());

                TiposClientes tipo = new TiposClientes(tipoId, "");
                Telefono tel = new Telefono(DNI, telefono);

                Cliente c = new Cliente();
                c.setDNI(DNI);
                c.setNombre(Nombre);
                c.setApellido(Apellido);
                c.setTipCliente(tipo);
                c.setCantCompras(cantCompras);
                c.setTelefono(tel);

                String queryP = "UPDATE Persona SET nombre = ?, apellido = ? WHERE DNI = ?;";
                String queryC = "UPDATE Cliente SET idTipo = ?, cantCompras = ? WHERE DNI = ?";
                String queryT = "UPDATE Telefonos SET idPersona = ?, telefono = ? WHERE idPersona = ?";

                PreparedStatement stmtP = conn.prepareStatement(queryP);
                PreparedStatement stmtC = conn.prepareStatement(queryC);
                PreparedStatement stmtT = conn.prepareStatement(queryT);

                Mapper.modPersona(stmtP, c);
                Mapper.modCliente(stmtC, c);
                Mapper.modTelefono(stmtT, c);

                stmtP.executeUpdate();
                stmtC.executeUpdate();
                stmtT.executeUpdate();

                mostrarAlerta("✅ Cliente modificado correctamente.");
                verClientes();

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                titulo,
                barraBusqueda,
                txtmDNI,
                txtmNombre,
                txtmApellido,
                txtmTipo,
                txtmCantCompras,
                txtmTelefono,
                btnModificarc
        );

        Button btnCancelar = new Button("❌ Cancelar modificar Cliente");
        btnCancelar.getStyleClass().add("boton-cancelar");
        btnCancelar.setOnAction(e -> {
            contenedor.getChildren().clear();
            contenedor.getChildren().add(outputArea);
        });

        HBox filaCancelar = new HBox(btnCancelar);
        filaCancelar.setAlignment(Pos.BOTTOM_RIGHT);

        form.setAlignment(Pos.TOP_CENTER);
        form.getChildren().add(filaCancelar);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(form);
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
            String nombre = ClienteDAO.buscarNombreCliente(id);
            if (nombre != null) {
                lblConfirmacion.setText("¿Eliminar a " + nombre + "?");
            } else {
                lblConfirmacion.setText("Cliente no encontrado.");
            }
        });

        Button btnEliminar = new Button("Sí, eliminar");
        btnEliminar.setOnAction(e -> {
            ClienteDAO.eliminarPorId(Integer.parseInt(txtId.getText()));
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
}
