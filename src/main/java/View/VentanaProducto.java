package View;

import Clases.Principales.Producto;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;

public class VentanaProducto {

    public static void cargarProductosEnLista(ArrayList<Producto> lista) {
        String sql = """
        SELECT idProducto, nombre, precio, costo, stock,
               idMedida, idCategoria, fechAlta, fechaBaja
        FROM Producto
    """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = Mapper.getProducto(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar productos: " + e.getMessage());
        }
    }

    public static String obtenerTextoProductos(ArrayList<Producto> lista) {
        if (lista.isEmpty()) return "Lista vacía.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-20s %-10s %-10s %-10s\n", "ID", "Nombre", "Precio", "Costo", "Stock"));
        sb.append("=".repeat(65)).append("\n");

        for (Producto p : lista) {
            sb.append(String.format("%-5d %-20s %-10.2f %-10.2f %-10d\n",
                    p.getProductoID(),
                    p.getNombreProducto(),
                    p.getPrecioUnitario(),
                    p.getCosto(),
                    p.getStock()
            ));
        }

        sb.append("\nTotal de productos: ").append(lista.size());
        return sb.toString();
    }




    public static Integer obtenerIdProductoPorNombre(String nombre) {
        String sql = "SELECT idProducto FROM Producto WHERE nombre = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("idProducto");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
