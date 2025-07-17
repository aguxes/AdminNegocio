package DataBase;

import Clases.Cliente;
import Clases.Imprimible;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/// Extras

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClienteDAO
{
    public static void cargarClientesEnLista(ArrayList<Imprimible> lista) {
        String query = """   
        SELECT p.DNI, c.id, p.nombre, p.apellido, c.tipCliente, c.cantCompras
        FROM Cliente c
        INNER JOIN Persona p ON c.DNI = p.DNI;
        """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next()) {
                Cliente c = Mapper.getCliente(rs);
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes en lista: ");
            e.printStackTrace();
        }
    }

    public void eliminarPorId(Scanner scan) {
        String query = """
            SELECT PE.nombre FROM Cliente c
            INNER JOIN Persona PE ON PE.DNI = c.DNI
            WHERE c.id = ?;
            """;
        String deletequery = """
            SELECT PE.nombre FROM Cliente c
            INNER JOIN Persona PE ON PE.DNI = c.DNI
            WHERE c.id = ?;
            """;

        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int id = scan.nextInt();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                System.out.println("Vas a eliminar al cliente ID: " + id + " Nombre: " + nombre +
                        "\n¿Está seguro? (1 = Sí, 2 = No)");
                int opcion = scan.nextInt();
                scan.nextLine();

                if (opcion == 1) {
                    conn.setAutoCommit(false);
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deletequery)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        conn.commit();
                        System.out.println("✅ Cliente eliminado.");
                    }
                } else { System.out.println("❎ Cancelado. No se eliminó a nadie."); }

            } else { System.out.println("⚠️ No se encontró ningún cliente con ese ID."); }

        } catch (SQLException e) { System.out.println("❌ Error en la base de datos: " + e.getMessage()); }
    }

    public void actualizarEmail(int id, String nuevoEmail) {
        String sql = "UPDATE clientes SET email = ? WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEmail);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar email del cliente: " + e.getMessage());
        }
    }

    public void imprimirClientes(ArrayList<Imprimible> lista) {
        if(!lista.isEmpty()){
            lista.getFirst().imprimirEncabezado(); //llamo al primer dato del array solo para poner el encabezado, no es importante
            for(Imprimible i: lista) i.imprimir();
        }else{
            System.out.println("Lista Vacia");
        }
    }

    public Cliente agregarClientePorConsola(Scanner scan) {

        System.out.print("Ingrese el dni del cliente: ");
        int dni = scan.nextInt();

        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scan.nextLine();

        System.out.print("Ingrese el apellido del cliente: ");
        String apellido = scan.nextLine();

        System.out.print("Ingrese el genero del cliente: ");
        int genero = scan.nextInt();

        System.out.print("Ingrese la nacionalidad del cliente: ");
        int nacionalidad = scan.nextInt();

        System.out.print("Ingrese el id del cliente: ");
        int id = scan.nextInt();

        System.out.print("Ingrese el tipo de Cliente del cliente: ");
        int tipCliente = scan.nextInt();


        System.out.print("Ingrese la cantidad de Compras del cliente: ");
        int cantCompras = scan.nextInt();

        Cliente c = new Cliente(dni, nombre, apellido,
        id, tipCliente, cantCompras);

        return c;
    }

    public static void insertar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (id, DNI, tipCliente, cantCompras) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            Mapper.setCliente(stmt, cliente);
            stmt.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (SQLException e) { System.out.println("❌ Error al insertar cliente: " + e.getMessage()); }
    }
    public void modificarClientePorId(Scanner scan, ArrayList<Imprimible> lista) {

        String querySelect = "SELECT * FROM Cliente WHERE id = ?";
        String queryUPD =
                """
        UPDATE Cliente SET nombre = ?, apellido = ?, DNI = ?, genero = ?, nacionalidad = ?, tipCliente = ?, cantCompras = ? WHERE id = ?;
        """;

        System.out.print("Ingrese el ID del cliente a modificar: ");
        int id = scan.nextInt();
        scan.nextLine(); // Limpia el salto de línea después del nextInt()

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(querySelect))
        {
            stmtSelect.setInt(1, id);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                Cliente c = Mapper.getCliente(rs);
                System.out.println("↩️ Deje el campo vacío si no quiere modificarlo");

                System.out.print("Nuevo nombre (" + c.getNombre() + "): ");
                String input = scan.nextLine();
                if (!input.isEmpty()) c.setNombre(input);

                System.out.print("Nuevo apellido (" + c.getApellido() + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()) c.setApellido(input);

                System.out.print("Nuevo DNI (" + c.getDNI() + "): ");
                if (scan.hasNextInt()) {
                    c.setDNI(scan.nextInt());
                } else {
                    String aux = scan.nextLine();
                    if (!aux.isEmpty()) {
                        try {
                            c.setDNI(Integer.parseInt(aux));
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ DNI inválido. Se mantiene el anterior.");
                        }
                    }
                }
                scan.nextLine();

                /*System.out.print("Nuevo género (" + c.getGenero() + "): ");
                if (scan.hasNextInt()) {
                    c.setGenero(scan.nextInt());
                } else {
                    String aux = scan.nextLine();
                    if (!aux.isEmpty()) {
                        try {
                            c.setGenero(Integer.parseInt(aux));
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Género inválido. Se mantiene el anterior.");
                        }
                    }
                }
                scan.nextLine();

                System.out.print("Nueva nacionalidad (" + c.getNacionalidad() + "): ");
                if (scan.hasNextInt()) {
                    c.setNacionalidad(scan.nextInt());
                } else {
                    String aux = scan.nextLine();
                    if (!aux.isEmpty()) {
                        try {
                            c.setNacionalidad(Integer.parseInt(aux));
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Nacionalidad inválida. Se mantiene la anterior.");
                        }
                    }
                }
                scan.nextLine(); */
                System.out.print("Nuevo tipo de cliente (" + c.getTipCliente() + "): ");
                if (scan.hasNextInt()) {
                    c.setTipCliente(scan.nextInt());
                } else {
                    String aux = scan.nextLine();
                    if (!aux.isEmpty()) {
                        try {
                            c.setTipCliente(Integer.parseInt(aux));
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Tipo inválido. Se mantiene el anterior.");
                        }
                    }
                }
                scan.nextLine();

                System.out.print("Nueva cantidad de compras (" + c.getCantCompras() + "): ");
                if (scan.hasNextInt()) { c.setCantCompras(scan.nextInt()); }
                else {
                    String aux = scan.nextLine();
                    if (!aux.isEmpty()) {
                        try {
                            c.setCantCompras(Integer.parseInt(aux));
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Cantidad inválida. Se mantiene la anterior.");
                        }
                    }
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(queryUPD)) {
                        Mapper.setCliente(stmtUpdate, c);

                        int filas = stmtUpdate.executeUpdate();
                        System.out.println(filas > 0 ? "✅ Cliente modificado." : "⚠️ No se modificó ningún cliente.");
                        lista.clear();
                        cargarClientesEnLista(lista);
                    }

                }

            }else{ System.out.println("❌ Cliente no encontrado con ID: " + id); }
        }catch(SQLException e) { System.out.println("❌ Error en la base de datos: " + e.getMessage()); }
    }
    public void buscarClientePorDato(Scanner scan) {

        ArrayList<Imprimible> listaTemp = new ArrayList<>();
        System.out.print("Buscar por email o nombre: (Ingrese la palabra 'apellido' o 'nombre'): ");
        String campo = scan.nextLine().toLowerCase();
        String query = """
                SELECT * FROM Cliente WHERE " + campo + " LIKE ?;
                """;

        if (campo.equals("apellido") || campo.equals("nombre"))
        {
            System.out.print("Ingrese el " + campo + " a buscar: ");
            String valor = scan.nextLine();

            try (Connection conn = DataBaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query))
            {

                stmt.setString(1, "%" + valor + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Cliente c = Mapper.getCliente(rs);
                    listaTemp.add(c);
                }
                if (listaTemp.isEmpty()) { System.out.println("❌ No se encontraron clientes con ese " + campo + "."); }
                else
                { imprimirClientes(listaTemp); }
            }
            catch (SQLException e) { System.out.println("❌ Error en la base de datos: " + e.getMessage()); }
        } else
        { System.out.println("⚠️ Opción no válida. Debe ingresar 'apellido' o 'nombre'."); }
    }

    // Para validaciones en ventas
    public static boolean existeCliente(int idCliente) {
        String sql = "SELECT 1 FROM Cliente WHERE id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e)
        { System.out.println("❌ Error validando cliente: " + e.getMessage()); return false; }
    }
}

