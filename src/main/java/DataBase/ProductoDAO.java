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
        System.out.print("Ingrese el ID del prodcuto a eliminar: ");
        int id = scan.nextInt();

        String sql = "SELECT nombre FROM PRODUCTOS WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                System.out.println("Vas a eliminar prducto ID: " + id + " Nombre: " + nombre +
                        "\n¿Está seguro? (1 = Sí, 2 = No)");
                int opcion = scan.nextInt();
                scan.nextLine();

                if (opcion == 1) {
                    String deleteSQL = "DELETE FROM productos WHERE id = ?";
                    conn.setAutoCommit(false);
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        conn.commit();
                        System.out.println("✅ Producto eliminado.");
                    }
                } else {
                    System.out.println("❎ Cancelado. No se elimino.");
                }

            } else {
                System.out.println("⚠️ No se encontró ningún producto con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en la base de datos: " + e.getMessage());
        }

    }

    public void buscarProductoPorNombreOCodigo(Scanner scan) {
        System.out.print("Ingrese el ID del producto a buscar: ");
        int id = scan.nextInt();
        scan.nextLine(); // consumir newline

        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Encabezado
                System.out.printf("%-5s %-20s %-40s %-10s %-10s %-10s %-15s\n",
                        "ID", "Nombre", "Descripción", "Precio", "Costo", "Stock", "Unidad");
                System.out.println("-------------------------------------------------------------------------------------------------");

                // Datos del producto
                System.out.printf("%-5d %-20s %-40s $%-9.2f $%-9.2f %-10d %-15s\n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioUnitario"),
                        rs.getDouble("costo"),
                        rs.getInt("stock"),
                        rs.getString("unidadMedida"));
            } else {
                System.out.println("⚠️ No se encontró ningún producto con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar producto: " + e.getMessage());
        }
    }

}
