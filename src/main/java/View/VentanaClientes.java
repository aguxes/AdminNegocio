package View;

import Clases.Cliente;
import DataBase.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class VentanaClientes {

    public static void cargarClientesEnLista(ArrayList<Cliente> lista) {
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
                int dni = rs.getInt("dni");

                Cliente c = new Cliente(nombre, dni, apellido, email, telefono, localidad, id);
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes: " + e.getMessage());
        }
    }

    public static String obtenerTextoClientes(ArrayList<Cliente> lista) {
        if (lista.isEmpty()) return "Lista vacía.";

        StringBuilder sb = new StringBuilder();

        // Encabezado
        sb.append(String.format("%-5s %-15s %-15s %-15s %-35s %-15s %-15s\n",
                "ID", "DNI", "Nombre", "Apellido", "Email", "Teléfono", "Localidad"));
        sb.append("----------------------------------------------------------------------------------------------------------\n");

        // Datos
        for (Cliente c : lista) {
            sb.append(String.format("%-5d %-15d %-15s %-15s %-35s %-15d %-15s\n",
                    c.getClienteID(),
                    c.getDNI(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getEmail(),
                    c.getTelefono(),
                    c.getLocalidad()));
        }

        return sb.toString();
    }


    public static String eliminarPorId(int id) {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT nombre FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                String deleteSQL = "DELETE FROM clientes WHERE id = ?";
                conn.setAutoCommit(false);
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                    deleteStmt.setInt(1, id);
                    deleteStmt.executeUpdate();
                    conn.commit();
                    result.append("✅ Cliente eliminado: ").append(nombre);
                }
            } else {
                result.append("⚠️ No se encontró ningún cliente con ese ID.");
            }

        } catch (SQLException e) {
            result.append("❌ Error en la base de datos: ").append(e.getMessage());
        }

        return result.toString();
    }

    public static Cliente obtenerClientePorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getString("nombre"),
                        rs.getInt("dni"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getLong("telefono"),
                        rs.getString("localidad"),
                        rs.getInt("id")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }

    public static String buscarNombrePorId(int id) {
        String sql = "SELECT nombre, apellido FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre") + " " + rs.getString("apellido");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar nombre por ID: " + e.getMessage());
        }

        return null;
    }



    public static String buscarClientePorDato(String campo, String valor) {
        ArrayList<Cliente> listaTemp = new ArrayList<>();
        StringBuilder resultado = new StringBuilder();

        if (campo.equals("email") || campo.equals("nombre")) {
            String sql = "SELECT * FROM clientes WHERE " + campo + " LIKE ?";
            try (Connection conn = DataBaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + valor + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Cliente c = new Cliente(
                            rs.getString("nombre"),
                            rs.getInt("dni"),
                            rs.getString("apellido"),
                            rs.getString("email"),
                            rs.getLong("telefono"),
                            rs.getString("localidad"),
                            rs.getInt("id")
                    );
                    listaTemp.add(c);
                }

                if (listaTemp.isEmpty()) {
                    resultado.append("❌ No se encontraron clientes con ese ").append(campo);
                } else {
                    resultado.append(obtenerTextoClientes(listaTemp));
                }

            } catch (SQLException e) {
                resultado.append("❌ Error en la base de datos: ").append(e.getMessage());
            }
        } else {
            resultado.append("⚠️ Opción no válida. Debe ser 'email' o 'nombre'.");
        }

        return resultado.toString();
    }

    public static String insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, email, telefono, localidad, apellido, dni) VALUES (?, ?, ?, ?, ?, ?)";
        StringBuilder resultado = new StringBuilder();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setLong(3, cliente.getTelefono());
            stmt.setString(4, cliente.getLocalidad());
            stmt.setString(5, cliente.getApellido());
            stmt.setInt(6, cliente.getDNI());

            stmt.executeUpdate();
            resultado.append("✅ Cliente insertado correctamente.");

        } catch (SQLException e) {
            resultado.append("❌ Error al insertar cliente: ").append(e.getMessage());
        }

        return resultado.toString();
    }
}
