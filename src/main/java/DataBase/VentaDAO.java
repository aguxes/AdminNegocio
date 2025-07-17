package DataBase;

//import Clases.Empleado;
import Clases.Principales.Imprimible;
import Clases.Principales.Venta;
import util.Mapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class VentaDAO {
    public void mostrarTodasLasVentas() {
        String sql = """
               SELECT v.id, c.nombre AS cliente, e.nombre AS empleado,
               v.fecha,  AS fecha, v.total, v.medio_pago
               FROM ventas v
               INNER JOIN clientes c ON v.cliente_id = c.id
               INNER JOIN empleados e ON v.empleado_id = e.id;
               """;

        System.out.printf("%-5s %-20s %-20s %-25s %-10s %-15s\n", "ID", "cliente", "empleado",  "fecha", "total", "medio de pago");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()){
                Mapper.getVenta(rs);
                System.out.printf("%-5s %-20s %-20s %-10s %-15s\n", "ID", "Cliente", "Fecha", "Total", "Medio Pago");
            }


        }catch(Exception e){
            System.out.println("Error al mostrar ventas" + e.getMessage());
        }
    }

    //Esta copada, igual mucho no sirve porque es para consola pero me guwsta.
        public void registrarVentaPorConsola(Scanner scan, ArrayList<Imprimible> listaVentas) {
            String queryStockPrice = "SELECT precioUnitario, stock FROM productos WHERE id = ?";
            String queryInsertV = "INSERT INTO ventas (cliente_id, empleado_id, medio_pago, total) VALUES (?, ?, ?, ?)";
            String queryInsertD = "INSERT INTO detalles_ventas (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            String updateStockSQL = "UPDATE productos SET stock = stock - ? WHERE id = ?";
            try (Connection conn = DataBaseConnection.getConnection()) {
                conn.setAutoCommit(false); // Para hacer todo en una transacción

                // Paso 1: Obtener datos
                System.out.print("ID del cliente: ");
                int clienteId = Integer.parseInt(scan.nextLine());

                System.out.print("ID del empleado: ");
                int empleadoId = Integer.parseInt(scan.nextLine());

                System.out.print("Medio de pago: ");
                String medioPago = scan.nextLine();

                System.out.print("ID del producto vendido: ");
                int productoId = Integer.parseInt(scan.nextLine());

                System.out.print("Cantidad vendida: ");
                int cantidad = Integer.parseInt(scan.nextLine());

                // Paso 2: Obtener precio unitario del producto
                double precioUnitario = 0;
                int stockActual = 0;
                try (PreparedStatement stmt = conn.prepareStatement(queryStockPrice)) {
                    stmt.setInt(1, productoId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        precioUnitario = rs.getDouble("precioUnitario");
                        stockActual = rs.getInt("stock");
                    } else {
                        System.out.println("❌ Producto no encontrado.");
                        return;
                    }
                }

                if (stockActual < cantidad) {
                    System.out.println("❌ Stock insuficiente.");
                    return;
                }

                double total = precioUnitario * cantidad;

                // Paso 3: Insertar en tabla ventas
                LocalDateTime fecha = LocalDateTime.now();
                Venta venta = new Venta( // ESTO TA MAL PERO NO SE COMO HACELL
                        0,
                        empleadoId,
                        clienteId,
                        fecha,
                        medioPago,
                        new java.math.BigDecimal(total),
                        "",
                        "",
                        ""
                );
                PreparedStatement stmtVenta = conn.prepareStatement(queryInsertV, Statement.RETURN_GENERATED_KEYS);
                Mapper.setVenta(stmtVenta, venta);
                stmtVenta.executeUpdate();

                ResultSet generatedKeys = stmtVenta.getGeneratedKeys();
                int ventaId = 0;
                if (generatedKeys.next()) {
                    ventaId = generatedKeys.getInt(1);
                }

                // Paso 4: Insertar en detalles_ventas
                PreparedStatement stmtDetalle = conn.prepareStatement(queryInsertD);
                stmtDetalle.setInt(1, ventaId);
                stmtDetalle.setInt(2, productoId);
                stmtDetalle.setInt(3, cantidad);
                stmtDetalle.setDouble(4, precioUnitario);
                stmtDetalle.executeUpdate();

                // Paso 5: Actualizar stock
                PreparedStatement stmtStock = conn.prepareStatement(updateStockSQL);
                stmtStock.setInt(1, cantidad);
                stmtStock.setInt(2, productoId);
                stmtStock.executeUpdate();

                conn.commit();
                System.out.println("✅ Venta registrada correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("❌ Error al registrar la venta: " + e.getMessage());
            }
        }

    public void mostrarVentasPorCliente(Scanner scan) {
        System.out.print("Ingrese el ID del cliente: ");
        int id = scan.nextInt();
        scan.nextLine();

        String sql = """
        SELECT v.id, c.nombre AS cliente, v.fecha, v.total, v.medio_pago
        FROM ventas v
        INNER JOIN clientes c ON v.cliente_id = c.id 
        WHERE v.cliente_id = ?;
        """;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            System.out.printf("%-5s %-20s %-20s %-10s %-15s\n", "ID", "Cliente", "Fecha", "Total", "Medio Pago");
            System.out.println("--------------------------------------------------------------------------");

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                Mapper.getVenta(rs);

                System.out.printf("%-5s %-20s %-20s %-10s %-15s\n", "ID", "Cliente", "Fecha", "Total", "Medio Pago");
            }

            if (!hayResultados) {
                System.out.println("⚠️ No se encontraron ventas para ese cliente.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al mostrar ventas: " + e.getMessage());
        }
    }

    public static boolean insertarVenta(Venta venta) {
        String sql = "INSERT INTO ventas (cliente_id, fecha, total, empleado_id, medio_pago, notas) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Mapper.setVenta(stmt, venta);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar venta: " + e.getMessage());
            return false;
        }
    }
}
