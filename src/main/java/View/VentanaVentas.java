package View;

import DataBase.DataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentanaVentas {
    public static String obtenerVentasComoTexto() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT v.id, " +
                "c.nombre || ' ' || c.apellido AS cliente, " + //esa cosa rara es concatenacion en SQLlite, lo que aprende uno
                "e.nombre || '   ' || e.puesto AS empleado, " +
                "DATETIME(v.fecha, '-3 hours') AS fecha, v.total, v.medio_pago " +
                "FROM ventas v " +
                "JOIN clientes c ON v.cliente_id = c.id " +
                "JOIN empleados e ON v.empleado_id = e.id";

        sb.append(String.format("%-5s %-25s %-30s %-25s %-10s %-15s\n",
                "ID", "Cliente", "Empleado", "Fecha", "Total", "Medio Pago"));
        sb.append("--------------------------------------------------------------------------------------------------------------------------\n");

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sb.append(String.format("%-5d %-25s %-30s %-25s $%-9.2f %-15s\n",
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getString("empleado"),
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
