package DataBase;

import Clases.Cliente;
import Clases.Imprimible;
import Clases.Producto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductoDAO {
    public void mostrarProductos() {
        ArrayList<Imprimible> temp = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precioUnitario = rs.getDouble("precioUnitario");
                double costo = rs.getDouble("costo");
                int stock = rs.getInt("stock");
                String unidadMedida = rs.getString("unidadMedida");

                Producto p = new Producto(id, nombre, descripcion, precioUnitario, costo, stock, unidadMedida);
                temp.add(p);
            }

            if (!temp.isEmpty()) {
                temp.getFirst().imprimirEncabezado();
                for (Imprimible i : temp) i.imprimir();
            } else {
                System.out.println("⚠️ No hay productos cargados.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes en lista: " + e.getMessage());
        }
    }

    public void agregarProductoPorConsola(Scanner scan) {
    }

    public void modificarProductoPorId(Scanner scan) {
    }

    public void eliminarProductoPorId(Scanner scan) {
    }

    public void buscarProductoPorNombreOCodigo(Scanner scan) {
    }
}
