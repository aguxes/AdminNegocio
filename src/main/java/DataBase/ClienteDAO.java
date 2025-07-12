package DataBase;

import Clases.Cliente;
import Clases.Imprimible;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClienteDAO
{

    public static void cargarClientesEnLista(ArrayList<Imprimible> lista) {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                Cliente c = Mapper.getCliente(rs);
                lista.add(c);
            }
        } catch (SQLException e) { System.out.println("❌ Error al cargar clientes en lista: " + e.getMessage()); }

    }

    public void eliminarPorId(Scanner scan) {
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int id = scan.nextInt();

        String sql = "SELECT nombre FROM clientes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
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
                    String deleteSQL = "DELETE FROM clientes WHERE id = ?";
                    conn.setAutoCommit(false);
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        conn.commit();
                        System.out.println("✅ Cliente eliminado.");
                    }
                } else { System.out.println("❎ Cancelado. No se eliminó a nadie."); }

            } else { System.out.println("⚠️ No se encontró ningún cliente con ese ID."); }

        } catch (SQLException e) {
            System.out.println("❌ Error en la base de datos: " + e.getMessage());
        }
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
        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scan.nextLine();

        System.out.print("Ingrese el apellido del cliente: ");
        String apellido = scan.nextLine();

        System.out.print("Ingrese el dni del cliente: ");
        String dni = scan.nextLine();

        System.out.print("Ingrese el email del cliente: ");
        String email = scan.nextLine();
        while (!email.contains("@") || !email.contains(".")) {
            System.out.print("❌ Email inválido. Ingrese un email válido: ");
            email = scan.nextLine();
        }
        System.out.print("Ingrese el telefono del cliente: ");
        String telefono = scan.nextLine();

        System.out.print("Ingrese la localidad del cliente: ");
        String localidad = scan.nextLine();

        Cliente c = new Cliente(nombre, apellido, dni, email, telefono, localidad);

        return c;
    }

    public static void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes ( nombre, apellido, dni, email, telefono, localidad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql  ))
        {
            Mapper.setCliente(stmt, cliente);
            stmt.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (SQLException e) { System.out.println("❌ Error al insertar cliente: " + e.getMessage()); }
    }
    public void modificarClientePorId(Scanner scan, ArrayList<Imprimible> lista) {
        System.out.print("Ingrese el id del cliente: ");
        int id = Integer.parseInt(scan.nextLine());

        String sqlSelect = "SELECT * FROM clientes WHERE id = ?";
        String sqlUpdate = "UPDATE clientes SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ?, localidad = ?, WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setInt(1, id);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                Cliente cliente = Mapper.getCliente(rs);
                System.out.println("↩️ Deje el campo vacío si no quiere modificarlo");

                System.out.print("Nuevo nombre (" + cliente.getNombre() + "): ");
                String input = scan.nextLine();
                if (!input.isEmpty()) cliente.setNombre(input);

                System.out.print("Nuevo apellido (" + cliente.getApellido() + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()) cliente.setApellido(input);

                System.out.print("Nuevo email (" + cliente.getEmail() + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()){
                    while (!input.contains("@") || !input.contains(".")) {
                        System.out.print("❌ Email inválido. Ingrese un email válido: ");
                        input = scan.nextLine();
                    }
                    cliente.setEmail(input);
                }

                System.out.print("Nuevo teléfono (anterior: " + cliente.getTelefono() + "): ");
                String telStr = scan.nextLine();
                if (!telStr.isEmpty())
                {
                    try { cliente.setTelefono(telStr); } catch (NumberFormatException e) {
                        System.out.println("⚠️ Teléfono inválido. Se mantiene el valor anterior.");
                    }
                }

                System.out.print("Nueva localidad (" + cliente.getLocalidad() + "): ");
                input = scan.nextLine();
                if (!input.isEmpty()) { cliente.setLocalidad(input); }

                System.out.print("Nuevo DNI (" + cliente.getDNI() + "): ");
                input = scan.nextLine();
                if (input.isEmpty()) {
                    try {
                        cliente.setDNI(input);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ DNI inválido. Se mantiene el valor anterior.");
                    }
                }
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    Mapper.setCliente(stmtUpdate, cliente);

                    int filas = stmtUpdate.executeUpdate();
                    System.out.println(filas > 0 ? "✅ Cliente modificado." : "⚠️ No se modificó ningún cliente.");
                    lista.clear();
                    cargarClientesEnLista(lista);
                }

            } else { System.out.println("❌ Cliente no encontrado con ID: " + id); }

        } catch (SQLException e) { System.out.println("❌ Error en la base de datos: " + e.getMessage()); }
    }


    public void buscarClientePorDato(Scanner scan) {
        ArrayList<Imprimible> listaTemp = new ArrayList<>();
        System.out.print("Buscar por email o nombre: (Ingrese la palabra 'email' o 'nombre'): ");
        String campo = scan.nextLine().toLowerCase();

        if (campo.equals("email") || campo.equals("nombre"))
        {
            System.out.print("Ingrese el " + campo + " a buscar: ");
            String valor = scan.nextLine();

            String sql = "SELECT * FROM clientes WHERE " + campo + " LIKE ?";
            try (Connection conn = DataBaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql))
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
        { System.out.println("⚠️ Opción no válida. Debe ingresar 'email' o 'nombre'."); }
    }

    // Para validaciones en ventas
    public static boolean existeCliente(int idCliente) {
        String sql = "SELECT 1 FROM clientes WHERE id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Error validando cliente: " + e.getMessage());
            return false;
        }
    }


}

