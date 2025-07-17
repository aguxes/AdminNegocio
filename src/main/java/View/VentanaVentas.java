package View;

import Clases.Principales.Venta;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class VentanaVentas {

    public static ArrayList<Venta> cargarVentasEnLista() {
        ArrayList<Venta> lista = new ArrayList<>();

        String sql = """
        SELECT v.nFactura, v.idC, v.idE, v.fecha, v.total, fp.descripcion AS medio_pago, v.subtotal,
           pc.nombre || ' ' || pc.apellido AS cliente_nombre,
           pe.nombre || ' ' || pe.apellido AS empleado_nombre,
           pr.nombre AS producto_nombre
        FROM Venta v
        LEFT JOIN Cliente c ON v.idC = c.ID
        LEFT JOIN Persona pc ON c.DNI = pc.DNI
        LEFT JOIN Empleado e ON v.idE = e.ID
        LEFT JOIN Persona pe ON e.DNI = pe.DNI
        LEFT JOIN FormaDePagos fp ON v.formaDePago = fp.idPago
        LEFT JOIN Producto pr ON v.idProd = pr.idProducto;
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

        sb.append(String.format("%-5s %-20s %-25s %-20s %-15s %-10s %-20s\n",
                "ID", "Cliente", "Empleado", "Fecha", "MedioPago", "Total", "Producto"));
        sb.append("--------------------------------------------------------------------------------------------------------------\n");

        for (Venta v : lista) {
            String fechaFormateada = v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


            sb.append(String.format("%-5d %-20s %-25s %-20s %-15s %-10.2f %-20s\n",
                    v.getIdVenta(),
                    v.getNombreCliente(),
                    v.getNombreEmpleado(),
                    fechaFormateada,
                    capitalize(v.getMedioPago()),
                    v.getImporteTotal(),
                    v.getNotas())); // reutilizado como nombre del producto
        }

        return sb.toString();
    }

    // Capitaliza solo la primera letra
    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static ArrayList<Venta> obtenerVentasPorCliente(int idCliente) {
        ArrayList<Venta> lista = new ArrayList<>();

        //esta consulta me la paso chat, debe poder mejorarse pero no se bien como
        String sql = """
        SELECT v.nFactura, v.idC, v.idE, v.fecha, v.total, v.subtotal,
               fp.descripcion AS medio_pago,
               pc.nombre || ' ' || pc.apellido AS cliente_nombre,
               pe.nombre || ' ' || pe.apellido AS empleado_nombre,
               pr.nombre AS producto_nombre
        FROM Venta v
        JOIN Cliente c ON v.idC = c.ID
        JOIN Persona pc ON c.DNI = pc.DNI
        JOIN Empleado e ON v.idE = e.ID
        JOIN Persona pe ON e.DNI = pe.DNI
        JOIN FormaDePagos fp ON v.formaDePago = fp.idPago
        JOIN Producto pr ON v.idProd = pr.idProducto
        WHERE v.idC = ?;
    """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Venta v = Mapper.getVenta(rs);
                lista.add(v);
            }

        } catch (Exception ex) {
            System.out.println("❌ Error al obtener ventas del cliente: " + ex.getMessage());
        }

        return lista;
    }




}
