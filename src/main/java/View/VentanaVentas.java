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
        SELECT v.nFactura, v.idC, v.idE, v.fecha, v.total, fp.descripcion AS medio_pago, v.subtotal,
               pc.nombre || ' ' || pc.apellido AS cliente_nombre,
               pe.nombre || ' ' || pe.apellido AS empleado_nombre
                FROM Venta v
                INNER JOIN Cliente c ON v.idC = c.ID
                INNER JOIN Persona pc ON c.DNI = pc.DNI
                INNER JOIN Empleado e ON v.idE = e.ID
                INNER JOIN Persona pe ON e.DNI = pe.DNI
                INNER JOIN FormaDePagos fp ON v.formaDePago = fp.idPago;
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
