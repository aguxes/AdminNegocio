package DataBase;

import Clases.Principales.Producto;
import Clases.Principales.Venta;
import util.Mapper;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static View.AppController.mostrarAlerta;

public class VentaDAO {
    private static final Connection conn = DataBaseConnection.getConnection();

    public static ArrayList<Venta> cargarVentasEnLista() {
        ArrayList<Venta> lista = new ArrayList<>();

        String sql = """
        SELECT v.nFactura, v.idC, v.idE, v.fecha, v.total, fp.descripcion AS mediopago, v.cantidad, v.subtotal,
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
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta v = Mapper.getVenta(rs);
                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar ventas: " + e.getMessage());
        }
        return lista;
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
        SELECT v.nFactura, v.idC, v.idE, v.fecha, v.total, v.subtotal, v.cantidad,
               fp.descripcion AS mediopago,
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

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public static boolean actualizarStockProducto(int id, int cantidadVendida) {
        try {
            String sql = "UPDATE Producto SET stock = stock - ? WHERE idProducto = ? AND stock >= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cantidadVendida);
            stmt.setInt(2, id);
            stmt.setInt(3, cantidadVendida);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static boolean modificarProducto(Producto producto) {
//
//    }

    public static boolean realizarVenta(Venta venta) {

        int productoId = venta.getIdProducto();
        int cantidad = venta.getCantidad();
        String sql = "INSERT INTO Venta (idProd, idC, idE, formaDePago, cantidad, fecha, subtotal, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (!VentaDAO.actualizarStockProducto(productoId, cantidad)) {
            mostrarAlerta("⚠ No hay stock del producto.");
            return false;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Mapper.setVenta(stmt, venta);
            stmt.executeUpdate();
            mostrarAlerta("✅ Venta registrada correctamente.");
            return true;
        } catch (Exception ex) {
            mostrarAlerta("❌ Error: " + ex.getMessage());
            return false;
        }

    }
}
