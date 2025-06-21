package DataBase;

import Clases.Cliente;
import Clases.Imprimible;
import Clases.Producto;

import java.sql.*;
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


    public Producto agregarProductoPorConsola(Scanner scan) {
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scan.nextLine();

        System.out.print("Ingrese la descripcion del producto: ");
        String descripcion = scan.nextLine();

        System.out.print("Ingrese el precio unitario del producto: ");
        int precioUnitario = Integer.parseInt(scan.nextLine());  //un scaner para int que no se rompe

        System.out.print("Ingrese el costo del producto: ");
        double costo = Double.parseDouble(scan.nextLine());


        System.out.print("Ingrese el Stock del producto: ");
        int stock = Integer.parseInt(scan.nextLine());

        System.out.print("Ingrese la unidad de medida del producto: ");
        String unidadMedida = scan.nextLine();

        Producto p = new Producto(nombre, descripcion, precioUnitario, costo, stock, unidadMedida);

        return p;
    }

    public static void insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precioUnitario, costo, stock, unidadMedida) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombreProducto());
            stmt.setString(2, producto.getDescripcionProducto());
            stmt.setDouble(3, producto.getPrecioUnitario());
            stmt.setDouble(4, producto.getCosto());
            stmt.setInt(5, producto.getStock());
            stmt.setString(6, producto.getUnidadMedida());

            stmt.executeUpdate();

            System.out.println("Producto insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar cliente: " + e.getMessage());
        }
    }


    public void modificarProductoPorId(Scanner scan) {
    }

    public void eliminarProductoPorId(Scanner scan) {
    }

    public void buscarProductoPorNombreOCodigo(Scanner scan) {
    }
}
