package DataBase;

import Clases.Cliente;
import Clases.Imprimible;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClienteDAO {

    public static void cargarClientesEnLista(ArrayList<Imprimible> lista) {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                long telefono = rs.getLong("telefono");
                String localidad = rs.getString("localidad");

                // Se puede usar datos ficticios para campos no devueltos por la tabla
                Cliente c = new Cliente(nombre, 0, apellido, email, telefono, localidad, id);

                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes en lista: " + e.getMessage());
        }

    }

    public void eliminarPorId(Scanner scan) {
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int id = scan.nextInt();

        String sql = "SELECT nombre FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                System.out.println("Vas a eliminar al cliente ID: " + id + " Nombre: " + nombre +
                        "\n¿Está seguro? (1 = Sí, 2 = No)");
                int opcion = scan.nextInt();
                scan.nextLine();

                if (opcion == 1) {
                    String deleteSQL = "DELETE FROM clientes WHERE id = ?";
                    conn.setAutoCommit(false);
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        conn.commit();
                        System.out.println("✅ Cliente eliminado.");
                    }
                } else {
                    System.out.println("❎ Cancelado. No se eliminó a nadie.");
                }

            } else {
                System.out.println("⚠️ No se encontró ningún cliente con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en la base de datos: " + e.getMessage());
        }
    }


    public void actualizarEmail(int id, String nuevoEmail) {
        String sql = "UPDATE clientes SET email = ? WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEmail);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar email del cliente: " + e.getMessage());
        }
    }

    public void imprimirClientes(ArrayList<Imprimible> lista) {
        if(!lista.isEmpty()){
            lista.getFirst().imprimirEncabezado(); //llamo al primer dato del array solo para poner el encabezado, no es importante
            for(Imprimible i: lista) i.imprimir();
        }else{
            System.out.println("Lista Vacia");
        }
    }

    //falta validar datos, pone en nombre un 2 y lo agrega la virgo funcion pero me pudri
    //la modifique por 1 hora y logre esto, otro dia la arreglo
    public Cliente agregarClientePorConsola(Scanner scan) {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scan.nextLine();

        System.out.print("Ingrese el apellido del cliente: ");
        String apellido = scan.nextLine();

        System.out.print("Ingrese el dni del cliente: ");
        int dni = Integer.parseInt(scan.nextLine());  //un scaner para int que no se rompe

        System.out.print("Ingrese el email del cliente: ");
        String email = scan.nextLine();
        while (!email.contains("@") || !email.contains(".")) {
            System.out.print("❌ Email inválido. Ingrese un email válido: ");
            email = scan.nextLine();
        }


        System.out.print("Ingrese el telefono del cliente: ");
        long telefono = Long.parseLong(scan.nextLine());

        System.out.print("Ingrese el localidad del cliente: ");
        String localidad = scan.nextLine();

        Cliente cliente = new Cliente(nombre, dni, apellido, email, telefono, localidad);

        return cliente;
    }

    public static void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes ( nombre, email, telefono, localidad, apellido, dni) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setLong(3, cliente.getTelefono());
            stmt.setString(4, cliente.getLocalidad());
            stmt.setString(5, cliente.getApellido());
            stmt.setInt(6, cliente.getDNI());

            stmt.executeUpdate();

            System.out.println("Cliente insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar cliente: " + e.getMessage());
        }
    }

    public void modificarClientePorId(Scanner scan, ArrayList<Imprimible> lista) {
        System.out.print("Ingrese el id del cliente: ");
        int id = Integer.parseInt(scan.nextLine());

        String sqlSelect = "SELECT * FROM clientes WHERE id = ?";
        String sqlUpdate = "UPDATE clientes SET nombre = ?, apellido = ?, email = ?, telefono = ?, localidad = ?, dni = ? WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setInt(1, id);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                // Datos actuales
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                long telefono = rs.getLong("telefono");
                String localidad = rs.getString("localidad");
                int dni = rs.getInt("dni");

                System.out.println("↩️ Deje el campo vacío si no quiere modificarlo");

                System.out.print("Nuevo nombre (" + nombre + "): ");
                String input = scan.nextLine();
                if (!input.isEmpty()) nombre = input;

                System.out.print("Nuevo apellido (" + apellido + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()) apellido = input;

                System.out.print("Nuevo email (" + email + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()){
                    while (!input.contains("@") || !input.contains(".")) {
                        System.out.print("❌ Email inválido. Ingrese un email válido: ");
                        input = scan.nextLine();
                    }
                    email = input;
                }

                System.out.print("Nuevo teléfono (anterior: " + telefono + "): ");
                String telStr = scan.nextLine();
                if (!telStr.isEmpty()) {
                    try {
                        telefono = Long.parseLong(telStr);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Teléfono inválido. Se mantiene el valor anterior.");
                    }
                }


                System.out.print("Nueva localidad (" + localidad + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()) localidad = input;

                System.out.print("Nuevo DNI (" + dni + "): ");
                String inputDni = scan.nextLine();
                if (!inputDni.isEmpty()) {
                    try {
                        dni = Integer.parseInt(inputDni);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ DNI inválido. Se mantiene el valor anterior.");
                    }
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, nombre);
                    stmtUpdate.setString(2, apellido);
                    stmtUpdate.setString(3, email);
                    stmtUpdate.setLong(4, telefono);
                    stmtUpdate.setString(5, localidad);
                    stmtUpdate.setInt(6, dni);
                    stmtUpdate.setInt(7, id);

                    int filas = stmtUpdate.executeUpdate();
                    System.out.println(filas > 0 ? "✅ Cliente modificado." : "⚠️ No se modificó ningún cliente.");
                    lista.clear();
                    cargarClientesEnLista(lista);
                }

            } else {
                System.out.println("❌ Cliente no encontrado con ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en la base de datos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Ingreso inválido: se esperaba un número");
        }
    }


    public void buscarClientePorDato(Scanner scan) {
        ArrayList<Imprimible> listaTemp = new ArrayList<>();
        System.out.print("Buscar por email o nombre: (Ingrese la palabra 'email' o 'nombre'): ");
        String campo = scan.nextLine().toLowerCase();

        if (campo.equals("email") || campo.equals("nombre")) {
            System.out.print("Ingrese el " + campo + " a buscar: ");
            String valor = scan.nextLine();

            String sql = "SELECT * FROM clientes WHERE " + campo + " LIKE ?";
            try (Connection conn = DataBaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + valor + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String email = rs.getString("email");
                    long telefono = rs.getLong("telefono");
                    String localidad = rs.getString("localidad");

                    Cliente c = new Cliente(nombre, 0, apellido, email, telefono, localidad, id);
                    listaTemp.add(c);
                }

                if (listaTemp.isEmpty()) {
                    System.out.println("❌ No se encontraron clientes con ese " + campo + ".");
                } else {
                    imprimirClientes(listaTemp);
                }

            } catch (SQLException e) {
                System.out.println("❌ Error en la base de datos: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Opción no válida. Debe ingresar 'email' o 'nombre'.");
        }
    }


}

