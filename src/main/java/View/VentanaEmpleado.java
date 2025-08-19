package View;

import Clases.Extras.Telefono;
import Clases.Principales.Cliente;
import Clases.Principales.Empleado;
//import Clases.Extras.RolesEmpleados;
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
import java.util.HashMap;
import java.util.Map;

import static View.AppController.mostrarAlerta;

public class VentanaEmpleado {
    @FXML private TextArea outputArea;
    @FXML private VBox contenedor;

    private static final Connection conn = DataBaseConnection.getConnection();

    public VentanaEmpleado(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public void verEmpleados() {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.getStyleClass().add("card");

        Label lblCampo = new Label("Buscar por:");
        lblCampo.getStyleClass().add("label-form");

        ChoiceBox<String> choiceCampo = new ChoiceBox<>();
        choiceCampo.getItems().addAll("nombre", "apellido", "ID", "DNI", "rol", "sueldo", "faltas", "vacaciones", "activo" );
        choiceCampo.setValue("ID");
        choiceCampo.getStyleClass().add("input-form");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: 2");
        txtValor.getStyleClass().add("input-form");
        txtValor.setMaxWidth(220);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");

        String[][] columnas = {
                {"ID", "id"},
                {"DNI", "dni"},
                {"Nombre", "nombre"},
                {"Apellido", "apellido"},
                {"Rol", "rol"},
                {"Sueldo", "sueldo"},
                {"Faltas", "faltas"},
                {"Vacaciones", "vacaciones"},
                {"Activo", "activo"},
                {"Teléfono", "telefonoStr"}
        };

        TableView<Empleado> tabla = Tablas.crearTabla(Empleado.class, columnas);

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.getChildren().addAll(lblCampo, choiceCampo, txtValor, btnBuscar);

        //anchos //STYLE
        choiceCampo.setPrefWidth(140);
        txtValor.setPrefWidth(180);
        btnBuscar.setPrefWidth(100);


        ArrayList<Empleado> listaInicial = new ArrayList<>();
        EmpleadoDAO.cargarEmpleadosEnLista(listaInicial);
        tabla.getItems().addAll(listaInicial);

        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();
            ArrayList<Empleado> resultado = EmpleadoDAO.BuscarEmpleadoPorDato(campo, valor);

            if (resultado.isEmpty()) {

                tabla.getItems().clear();
                mostrarAlerta("❌ No se encontró ningún empleado con ese dato.");
            } else {
                tabla.getItems().setAll(resultado);
            }
        });
        txtValor.setOnAction(e -> btnBuscar.fire());
        btnBuscar.setPrefWidth(240);
        btnBuscar.setAlignment(Pos.CENTER);

        tarjeta.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(tarjeta, Priority.ALWAYS);

        tarjeta.getChildren().addAll(barraBusqueda, tabla);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void agregarEmpleado() {
        contenedor.getChildren().clear();

        Map<String, Object> campos = new HashMap<>();
        String[][] lineas = {
                {"DNI", "dni", "text"},
                {"Nombre", "nombre","text"},
                {"Apellido", "apellido", "text"},
                {"Rol", "rol", "text"}, // ddl
                {"Sueldo", "sueldo", "text"},
                {"Vacaciones", "vacaciones", "text"},
                {"Activo", "activo", "text"},
                {"Teléfono", "telefono", "text"}
        };

        VBox formCampos = Tablas.formtoAddEntidad(lineas, campos, null);

        Label titulo = new Label("📋 Registrar Empleado");
        titulo.getStyleClass().add("titulo-principal");

        Button btnRegistrarc = new Button("✅ Registrar Empleado");
        btnRegistrarc.getStyleClass().add("boton-accion");
        btnRegistrarc.setOnAction(e -> {
            try {

                int DNI = Integer.parseInt(((TextField) campos.get("dni")).getText().trim());
                String Nombre = ((TextField) campos.get("nombre")).getText().trim();
                String Apellido = ((TextField) campos.get("apellido")).getText().trim();
                int rolId = Integer.parseInt(((TextField) campos.get("rol")).getText().trim());
                double sueldo = Double.parseDouble(((TextField) campos.get("sueldo")).getText().trim());
                String telefonoStr = ((TextField) campos.get("telefono")).getText().trim();


                if (!telefonoStr.matches("\\d{8,11}")) {
                    mostrarAlerta("⚠El numero de teléfono debe tener entre 8 y 11 dígitos y solo contener números.");
                    return;
                }

                Long telefono = Long.parseLong(telefonoStr);


                //TipoRol rol = new TipoRol(rolId, "");
                Telefono tel = new Telefono(DNI, telefono);

                Empleado em = new Empleado();
                em.setDNI(DNI);
                em.setNombre(Nombre);
                em.setApellido(Apellido);
                //e.setRolID(rol);
                em.setSueldo(sueldo);
                em.setVacacionesActivas(false);
                em.setActivo(true);
                em.setTelefono(tel);

                em.setFechaDeIngreso("01-01-2001");
                em.setFechaDeEgreso("01-02-2001");


                String queryP = "INSERT INTO Persona (dni, nombre, apellido ) VALUES (?, ?, ?) ";
                String queryC = "INSERT INTO Empleado (id, dni, idrol, sueldo, vacaciones, faltas, fechaingreso, fechaegreso, activo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String queryT = "INSERT INTO Telefonos (idpersona, telefono) VALUES (?, ?)";

                PreparedStatement stmtP = conn.prepareStatement(queryP);
                PreparedStatement stmtC = conn.prepareStatement(queryC);
                PreparedStatement stmtT = conn.prepareStatement(queryT);

                Mapper.setPersona(stmtP, em);
                Mapper.setEmpleado(stmtC, em);
                Mapper.setTelefono(stmtT, em);

                stmtP.executeUpdate();
                stmtC.executeUpdate();
                stmtT.executeUpdate();

                mostrarAlerta("✅ Empleado registrado correctamente.");
                contenedor.getChildren().clear();
                contenedor.getChildren().add(outputArea);

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        Button btnCancelar = new Button("❌ Cancelar nuevo Empleado");
        btnCancelar.getStyleClass().add("boton-cancelar");
        btnCancelar.setOnAction(e -> {
            verEmpleados();
        });

        HBox filaCancelar = new HBox(btnCancelar);
        filaCancelar.setAlignment(Pos.BOTTOM_RIGHT);


        VBox formFinal = new VBox(10, titulo, formCampos, btnRegistrarc, filaCancelar);
        formFinal.setAlignment(Pos.TOP_CENTER);

        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(formFinal);
    }
    public void modificarEmpleado() {
        contenedor.getChildren().clear();

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.getStyleClass().add("form-box");

        Label titulo = new Label("📋 Modificar Empleado");
        titulo.getStyleClass().add("titulo-principal");

        Label lblCampo = new Label("Buscar Empleado");
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

        Map<String, TextField> campos = new HashMap<>();
        String[][] lineas = {
                {"ID", "id"},
                {"DNI", "dni"},
                {"Nombre", "nombre"},
                {"Apellido", "apellido"},
                {"Rol", "rol"},
                {"Sueldo", "sueldo"},
                {"Faltas", "faltas"},
                {"Vacaciones", "vacaciones"},
                {"Activo", "activo"},
                {"Teléfono", "telefonoStr"}
        };

        VBox formCampos = Tablas.crearform(lineas, campos);

        campos.get("dni").setEditable(false);

        btnBuscar.setOnAction(ev -> {
            try {
                int id = Integer.parseInt(txtValor.getText().trim());
                Empleado e = EmpleadoDAO.obtenerEmpleadoPorId(id);

                if (e != null) {
                    campos.get("dni").setText(String.valueOf(e.getDNI()));
                    campos.get("nombre").setText(e.getNombre());
                    campos.get("apellido").setText(e.getApellido());
                    campos.get("telefono").setText(String.valueOf(e.getTelefono().getTelefono()));
                } else {
                    mostrarAlerta("❌ No se encontró el Empleado con ID: " + id);
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("❌ Ingresá un número válido para el ID.");
            }
        });

        Button btnModificarc = new Button("✅ Modificar Empleado");
        btnModificarc.getStyleClass().add("boton-accion");
        btnModificarc.setOnAction(e -> {
            try {
                int DNI = Integer.parseInt(campos.get("dni").getText().trim());
                String Nombre = campos.get("nombre").getText().trim();
                String Apellido = campos.get("apellido").getText().trim();
                String telefonoStr = campos.get("telefono").getText().trim();
                if (!telefonoStr.matches("\\d{8,15}")) { // Validación
                    mostrarAlerta("Ingresa un número de teléfono válido (solo números, 8 a 15 dígitos).");
                    return;
                }
                Long telefono = Long.parseLong(telefonoStr);

                Telefono tel = new Telefono(DNI, telefono);

                Empleado em = new Empleado();
                em.setDNI(DNI);
                em.setNombre(Nombre);
                em.setApellido(Apellido);
                em.setTelefono(tel);

                String queryP = "UPDATE Persona SET nombre = ?, apellido = ? WHERE DNI = ?;";
                String queryE = "UPDATE Empleado SET idrol = ?, sueldo = ?, vacaciones = ?, faltas = ?, activo = ?, WHERE DNI = ?";
                String queryT = "UPDATE Telefonos SET telefono = ? WHERE idPersona = ?";

                PreparedStatement stmtP = conn.prepareStatement(queryP);
                PreparedStatement stmtE = conn.prepareStatement(queryE);
                PreparedStatement stmtT = conn.prepareStatement(queryT);

                Mapper.modPersona(stmtP, em);
                Mapper.modEmpleado(stmtE, em);
                Mapper.modTelefono(stmtT, em);

                stmtP.executeUpdate();
                stmtE.executeUpdate();
                stmtT.executeUpdate();

                mostrarAlerta("✅ Empleado modificado correctamente.");
                verEmpleados();

            } catch (Exception ex) {
                mostrarAlerta("❌ Error: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                titulo,
                barraBusqueda,
                formCampos,
                btnModificarc
        );

        Button btnCancelar = new Button("❌ Cancelar modificar Empleado");
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
    public void EliminarEmpleado() {
        Stage ventana = new Stage();
        ventana.setTitle("Eliminar Empleado");

        TextField txtId = new TextField();
        txtId.setPromptText("ID del Empleado");

        Label lblConfirmacion = new Label();

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = EmpleadoDAO.buscarxNombre(id);
            if (nombre != null) {
                lblConfirmacion.setText("¿Eliminar a " + nombre + "?");
            } else {
                lblConfirmacion.setText("Empleado no encontrado.");
            }
        });

        Button btnEliminar = new Button("Sí, eliminar");
        btnEliminar.setOnAction(e -> {
            EmpleadoDAO.eliminar(Integer.parseInt(txtId.getText()));
            ventana.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnEliminar, btnCancelar);
        botones.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, new Label("ID Empleado:"), txtId, btnBuscar, lblConfirmacion, botones);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 300, 250);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
    }
}