package DataBase;

import Clases.Principales.Producto;
import DataBase.*;
import util.*;
import java.sql.*;
import java.util.ArrayList;


public class ProductoDAO {

    private static final Connection conn = DataBaseConnection.getConnection();

    public static ArrayList<Producto> cargarProductosEnLista() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = """
        SELECT p.idProducto, p.nombre, p.precio, p.costo, p.stock,
        p.fechAlta, p.fechaBaja, c.descripcion AS categoriaNombre, m.descripcion AS medidaNombre
        FROM Producto p
        INNER JOIN CategoriasProd c ON c.Categoria = p.idCategoria
        INNER JOIN MedidasProd m ON m.unidadMedida = p.idMedida
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = Mapper.getProducto(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar productos: " + e.getMessage());
        }
        return lista;
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

    public static Producto obtenerProductoPorID(int id) {
        try {
            String sql = "SELECT * FROM Producto WHERE idProducto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Mapper.getProducto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Integer obtenerIdProductoPorNombre(String nombre) {
        String sql = "SELECT idProducto FROM Producto WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("idProducto");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
