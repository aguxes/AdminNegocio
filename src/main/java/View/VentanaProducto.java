package View;

import Clases.Principales.Producto;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VentanaProducto {
    private static final Connection conn = DataBaseConnection.getConnection();
    public static void cargarProductosEnLista(ArrayList<Producto> lista) {
        String sql = """
        SELECT idProducto, nombre, precio, costo, stock,
               idMedida, idCategoria, fechAlta, fechaBaja
        FROM Producto
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

    public static ArrayList<Producto> cargarProductosConDescripcion() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = """
        SELECT p.idProducto, p.nombre, p.precio, p.costo, p.stock, 
               p.fechAlta, p.fechaBaja,
               c.descripcion AS categoriaNombre,
               m.descripcion AS medidaNombre
        FROM Producto p
        JOIN CategoriasProd c ON p.idCategoria = c.Categoria
        JOIN MedidasProd m ON p.idMedida = m.unidadMedida
    """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {



            while (rs.next()) {
                String fechaAltaStr = rs.getString("fechAlta");
                String fechaBajaStr = rs.getString("fechaBaja");

                LocalDate fechaAlta = (fechaAltaStr != null && !fechaAltaStr.isEmpty()) ? LocalDate.parse(fechaAltaStr) : null;
                LocalDate fechaBaja = (fechaBajaStr != null && !fechaBajaStr.isEmpty()) ? LocalDate.parse(fechaBajaStr) : null;

                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getInt("stock"),
                        rs.getString("medidaNombre"),
                        rs.getString("categoriaNombre"),
                        fechaAlta,
                        fechaBaja
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar productos con descripción: " + e.getMessage());
        }
        return lista;
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
