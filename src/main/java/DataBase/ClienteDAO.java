package DataBase;

import Clases.Cliente;
import Clases.Imprimible;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteDAO {

    public void cargarClientesEnLista(ArrayList<Imprimible> lista) {
        String sql = "SELECT * FROM clientes";

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
}

