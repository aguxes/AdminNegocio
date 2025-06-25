package View;

import DataBase.DataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentanaClientes {

    public static String MostrarClientes() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM clientes";

        sb.append(String.format("%-5s %-13s %-13s %-13s %-35s %-15s %-15s\n",
                "ID","DNI", "Nombre", "Apellido", "Email", "Teléfono", "Localidad"));
        sb.append("---------------------------------------------------------------------------------------------\n");

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sb.append(String.format("%-5d %-13s %-13s %-13s %-35s %-15s %-15s\n",
                        rs.getInt("id"),
                        rs.getInt("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("localidad")));
            }

        } catch (SQLException e) {
            sb.append("❌ Error al mostrar clientes: ").append(e.getMessage());
        }

        return sb.toString();
    }

}
