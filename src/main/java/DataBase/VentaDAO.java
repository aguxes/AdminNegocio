package DataBase;

import Clases.Empleado;
import Clases.Imprimible;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VentaDAO {
    public void mostrarTodasLasVentas() {
        String sql = "SELECT v.id, c.nombre AS cliente, e.nombre AS empleado, " +
                "DATETIME(v.fecha, '-3 hours') AS fecha, v.total, v.medio_pago " + //eso de -3 hours es xq muestra la hora como el orto
                "FROM ventas v " +
                "JOIN clientes c ON v.cliente_id = c.id " +
                "JOIN empleados e ON v.empleado_id = e.id";



        System.out.printf("%-5s %-20s %-20s %-25s %-10s %-15s\n", "id", "cliente", "empleado", "fecha", "total", "medio de pago");


        System.out.println("-----------------------------------------------------------------------------------------------------------------");


        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {



            while (rs.next()){
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                String fecha =  rs.getString("fecha");
                double total =  rs.getDouble("total");
                String medio_pago = rs.getString("medio_pago");

                System.out.printf("%-5d %-20s %-20s %-25s $%-9.2f %-15s\n", id, cliente, empleado, fecha, total, medio_pago);
            }


        }catch(Exception e){
            System.out.println("Error al mostrar ventas" + e.getMessage());
        }
    }

    //esta funcion no la entienod la vdd, osea si pero es un quilombo y hay partes que no, me la dio chat
        public void registrarVentaPorConsola(Scanner scan, ArrayList<Imprimible> listaVentas) {
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
                String getProductoSQL = "SELECT precioUnitario, stock FROM productos WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(getProductoSQL)) {
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
                String insertVentaSQL = "INSERT INTO ventas (cliente_id, empleado_id, medio_pago, total) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtVenta = conn.prepareStatement(insertVentaSQL, Statement.RETURN_GENERATED_KEYS);
                stmtVenta.setInt(1, clienteId);
                stmtVenta.setInt(2, empleadoId);
                stmtVenta.setString(3, medioPago);
                stmtVenta.setDouble(4, total);
                stmtVenta.executeUpdate();

                ResultSet generatedKeys = stmtVenta.getGeneratedKeys();
                int ventaId = 0;
                if (generatedKeys.next()) {
                    ventaId = generatedKeys.getInt(1);
                }

                // Paso 4: Insertar en detalles_ventas
                String insertDetalleSQL = "INSERT INTO detalles_ventas (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtDetalle = conn.prepareStatement(insertDetalleSQL);
                stmtDetalle.setInt(1, ventaId);
                stmtDetalle.setInt(2, productoId);
                stmtDetalle.setInt(3, cantidad);
                stmtDetalle.setDouble(4, precioUnitario);
                stmtDetalle.executeUpdate();

                // Paso 5: Actualizar stock
                String updateStockSQL = "UPDATE productos SET stock = stock - ? WHERE id = ?";
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

        String sql = "SELECT v.id, c.nombre AS cliente, v.fecha, v.total, v.medio_pago FROM ventas v JOIN clientes c ON v.cliente_id = c.id WHERE v.cliente_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            System.out.printf("%-5s %-20s %-20s %-10s %-15s\n", "ID", "Cliente", "Fecha", "Total", "Medio Pago");
            System.out.println("--------------------------------------------------------------------------");

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                int ventaId = rs.getInt("id");
                String cliente = rs.getString("cliente");
                String fecha = rs.getString("fecha");
                double total = rs.getDouble("total");
                String medioPago = rs.getString("medio_pago");

                System.out.printf("%-5d %-20s %-20s $%-9.2f %-15s\n", ventaId, cliente, fecha, total, medioPago);
            }

            if (!hayResultados) {
                System.out.println("⚠️ No se encontraron ventas para ese cliente.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al mostrar ventas: " + e.getMessage());
        }
    }


}
