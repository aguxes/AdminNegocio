package DataBase;

import Clases.Cliente;
import Clases.Imprimible;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void cargarClientesEnLista(ArrayList<Imprimible> lista) {
        String sql = "SELECT id, nombre, email, telefono FROM clientes";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefonoStr = rs.getString("telefono");
                
                int telefono = (telefonoStr != null && !telefonoStr.isEmpty()) ? Integer.parseInt(telefonoStr) : 0;

                // Se puede usar datos ficticios para campos no devueltos por la tabla
                Cliente c = new Cliente(nombre, 0, "", email, telefono, "", id);
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes en lista: " + e.getMessage());
        }
    }

    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, String.valueOf(cliente.getTelefono()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar cliente: " + e.getMessage());
        }
    }

    public void eliminarPorId(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar cliente: " + e.getMessage());
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
}

