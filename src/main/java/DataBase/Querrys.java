package DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Querrys {

    public static void mostrarClientes() {
        Connection conn = DataBase.Connection.getConnection();

        String sql = "SELECT * FROM clientes";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-15s %-25s %-15s\n", "ID", "Nombre", "Email", "Teléfono");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");

                System.out.printf("%-5d %-15s %-25s %-15s\n", id, nombre, email, telefono);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al mostrar clientes: " + e.getMessage());
        }
    }

}
