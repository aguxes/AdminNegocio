package DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Querrys {

    public static void mostrarVentasConCliente() {

    }

    public static String obtenerClientesComoTexto() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT id, nombre, email, telefono FROM clientes";

        try (Statement stmt = DataBaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            sb.append(String.format("%-5s %-15s %-25s %-15s\n", "ID", "Nombre", "Email", "Teléfono"));
            sb.append("---------------------------------------------------------------\n");

            while (rs.next()) {
                sb.append(String.format("%-5d %-15s %-25s %-15s\n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono")));
            }

        } catch (SQLException e) {
            sb.append("❌ Error al mostrar clientes: ").append(e.getMessage());
        }

        return sb.toString();
    }

    public static String obtenerVentasComoTexto() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT v.id, c.nombre AS cliente, v.fecha, v.total, v.medio_pago " +
                "FROM ventas v JOIN clientes c ON v.cliente_id = c.id";

        try (Statement stmt = DataBaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            sb.append(String.format("%-5s %-20s %-15s %-10s %-15s\n", "ID", "Cliente", "Fecha", "Total", "Medio Pago"));
            sb.append("----------------------------------------------------------------------\n");

            while (rs.next()) {
                sb.append(String.format("%-5d %-20s %-15s $%-9.2f %-15s\n",
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getString("fecha"),
                        rs.getDouble("total"),
                        rs.getString("medio_pago")));
            }

        } catch (SQLException e) {
            sb.append("❌ Error al mostrar ventas: ").append(e.getMessage());
        }

        return sb.toString();
    }

}
