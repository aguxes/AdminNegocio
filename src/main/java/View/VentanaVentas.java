package View;

import Clases.Principales.Venta;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class VentanaVentas {

    public static ArrayList<Venta> cargarVentasEnLista() {
        ArrayList<Venta> lista = new ArrayList<>();

        String sql = """
                SELECT v.id, v.cliente_id, v.empleado_id, v.fecha, v.total, v.medio_pago, v.notas,
                c.nombre || ' ' || c.apellido AS cliente_nombre,
                e.nombre || ' ' || e.puesto AS empleado_nombre
                FROM ventas v
                INNER JOIN clientes c ON v.cliente_id = c.id
                INNER JOIN empleados e ON v.empleado_id = e.id;
                """;

        try (Connection conn = DataBaseConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta venta = Mapper.getVenta(rs);
                lista.add(venta);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar ventas: " + e.getMessage());
        }
        return lista;
    }

    public static String obtenerVentas(ArrayList<Venta> lista) {
        if (lista == null || lista.isEmpty()) return "Lista de ventas vacía.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-3s %-20s %-25s %-20s %-12s %-10s %-25s\n",
                "ID", "Cliente", "Empleado", "Fecha", "MedioPago", "Total", "Notas"));
        sb.append("---------------------------------------------------------------------------------------------------------------------------\n");

        for (Venta v : lista) {
            sb.append(String.format("%-3d %-20s %-25s %-20s %-12s %-10.2f %-25s\n",
                    v.getIdVenta(),
                    v.getNombreCliente(),
                    v.getNombreEmpleado(),
                    v.getFecha(),
                    v.getMedioPago(),
                    v.getImporteTotal(),
                    v.getNotas()));
        }
        return sb.toString();
    }
}
