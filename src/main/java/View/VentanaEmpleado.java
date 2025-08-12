package View;

import Clases.Extras.Telefono;
import Clases.Extras.TiposClientes;
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

    /*public void verEmpleados() {
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

        HBox barraBusqueda = new HBox(10); // espacio entre elementos STYLE
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.getChildren().addAll(lblCampo, choiceCampo, txtValor, btnBuscar);

        //anchos //STYLE
        choiceCampo.setPrefWidth(140);
        txtValor.setPrefWidth(180);
        btnBuscar.setPrefWidth(100);


        // Cargar todos los clientes por defecto
        ArrayList<Empleado> listaInicial = new ArrayList<>();
        EmpleadoDAO.cargarEmpleadosEnLista(listaInicial);
        tabla.getItems().addAll(listaInicial);

        btnBuscar.setOnAction(e -> {
            String campo = choiceCampo.getValue();
            String valor = txtValor.getText();
            ArrayList<Empleado> resultado = EmpleadoDAO.buscarEmpleadoPorDato(campo, valor);

            if (resultado.isEmpty()) {

                tabla.getItems().clear();
                mostrarAlerta("❌ No se encontró ningún cliente con ese dato.");
            } else {
                tabla.getItems().setAll(resultado);
            }
        });
        txtValor.setOnAction(e -> btnBuscar.fire());
        btnBuscar.setPrefWidth(240); // mismo ancho que txtValor
        btnBuscar.setAlignment(Pos.CENTER);

        tarjeta.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho disponible
        VBox.setVgrow(tarjeta, Priority.ALWAYS); // Opcional para que crezca si hay espacio

        tarjeta.getChildren().addAll(barraBusqueda, tabla);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void agregarEmpleaod() {
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

                Empleado e = new Empleado();
                e.setDNI(DNI);
                e.setNombre(Nombre);
                e.setApellido(Apellido);
                //e.setRolID(rol);
                e.setSueldo(sueldo);
                e.setVacacionesActivas(false);
                e.setActivo(true);
                e.setTelefono(tel);

                e.setFechaDeIngreso("01-01-2001");
                e.setFechaDeEgreso("01-02-2001");


                String queryP = "INSERT INTO Persona (dni, nombre, apellido ) VALUES (?, ?, ?) ";
                String queryC = "INSERT INTO Empleado (id, dni, idrol, sueldo, vacaciones, faltas, fechaingreso, fechaegreso, activo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String queryT = "INSERT INTO Telefonos (idpersona, telefono) VALUES (?, ?)";

                PreparedStatement stmtP = conn.prepareStatement(queryP);
                PreparedStatement stmtC = conn.prepareStatement(queryC);
                PreparedStatement stmtT = conn.prepareStatement(queryT);

                Mapper.setPersona(stmtP, e);
                Mapper.setEmpleado(stmtC, e);
                Mapper.setTelefono(stmtT, e);

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

        Button btnCancelar = new Button("❌ Cancelar nuevo Cliente");
        btnCancelar.getStyleClass().add("boton-cancelar");
        btnCancelar.setOnAction(e -> {
            verClientes();
        });

        HBox filaCancelar = new HBox(btnCancelar);
        filaCancelar.setAlignment(Pos.BOTTOM_RIGHT);


        VBox formFinal = new VBox(10, titulo, formCampos, btnRegistrarc, filaCancelar);
        formFinal.setAlignment(Pos.TOP_CENTER);

        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(formFinal);
    }
*/
    public static void cargarEmpleadosEnLista(ArrayList<Empleado> lista) {
        String sql = """
        SELECT e.ID, e.DNI, p.nombre, p.apellido, e.idRol, e.Sueldo,
               e.Vacaciones, e.Faltas, e.FechaIngreso, e.FechaEgreso, e.Activo
        FROM Empleado e
        INNER JOIN Persona p ON e.DNI = p.DNI
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Empleado e = Mapper.getEmpleado(rs);
                lista.add(e);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar empleados: " + e.getMessage());
        }
    }
/*
    //esta fletarla capaz, no se usa
    public static String obtenerTextoEmpleados(ArrayList<Empleado> lista) {
        if (lista.isEmpty()) return "Lista vacía.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-10s %-15s %-15s %-10s\n", "ID", "DNI", "Nombre", "Apellido", "Rol"));
        sb.append("--------------------------------------------------------------\n");
        for (Empleado e : lista) {
            sb.append(String.format("%-5d %-10d %-15s %-15s %-10d\n",
                    e.getEmpleadoID(),
                    e.getDNI(),
                    e.getNombre(),
                    e.getApellido(),
                    e.getRolID()
            ));
        }
        return sb.toString();
    }
*/
    public static Integer obtenerIdEmpleadoPorDni(int dni) {
        String sql = "SELECT ID FROM Empleado WHERE DNI = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("ID");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}