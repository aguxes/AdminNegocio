package DataBase;

import Clases.Imprimible;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VentaDAO {
    public void mostrarTodasLasVentas() {
        
    }

    public void registrarVentaPorConsola(Scanner scan, ArrayList<Imprimible> lista) {
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
