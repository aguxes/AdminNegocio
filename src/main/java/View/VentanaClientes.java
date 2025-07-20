package View;

import Clases.Principales.Persona;
import Clases.Principales.Cliente;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;

public class VentanaClientes {
    private static final Connection conn = DataBaseConnection.getConnection();
    public static void cargarClientesEnLista(ArrayList<Cliente> lista) {
        String sql = """
        SELECT c.ID, p.DNI, p.nombre, p.apellido, tc.tipo, tc.descripcion, c.cantCompras, t.telefono
        FROM Cliente c
         INNER JOIN Persona p ON c.DNI = p.DNI
         LEFT JOIN Telefonos t ON t.idPersona = p.DNI
         INNER JOIN TiposClientes tc ON c.idTipo = tc.tipo
        """;


        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = Mapper.getCliente(rs);
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes: " + e.getMessage());
        }
    }

    public static String obtenerClientes(ArrayList<Cliente> lista) {
        if (lista == null || lista.isEmpty()) return "Lista vacía.";

        StringBuilder sb = new StringBuilder();

        // Encabezado
        sb.append(String.format(" %-5s %-10s %-15s %-15s %-10s %-10s %-15s\n",
                "ID", "DNI", "Nombre", "Apellido", "Compras", "Tipo", "Teléfono"));
        sb.append("----------------------------------------------------------------------------------------------------------\n");

        // Datos
        for (Cliente c : lista) {
            sb.append(String.format(" %-5d %-10d %-15s %-15s %-10d %-10s %-15s\n",
                    c.getId(),
                    c.getDNI(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getCantCompras(),
                    c.getTipo().getDescripcion(),
                    c.getTelefono()
            ));
        }
        return sb.toString();
    }

    public static String eliminarPorId(int id) {
        StringBuilder result = new StringBuilder();
        String query = """
        SELECT p.nombre FROM Cliente c
        INNER JOIN Persona p ON p.DNI = c.DNI
         WHERE c.id = ?
        """;
        String deleteSQL = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                conn.setAutoCommit(false);
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                    deleteStmt.setInt(1, id);
                    deleteStmt.executeUpdate();
                    conn.commit();
                    result.append("✅ Cliente eliminado: ").append(nombre);
                }
            } else {
                result.append("⚠️ No se encontró ningún cliente con ese ID.");
            }

        } catch (SQLException e) {
            result.append("❌ Error en la base de datos: ").append(e.getMessage());
        }

        return result.toString();
    }

    public static Cliente obtenerClientePorId(int id) {
        String query = """
        SELECT p.nombre FROM Cliente c
        INNER JOIN Persona p ON p.DNI = c.DNI
         WHERE c.id = ?
        """;
        StringBuilder result = new StringBuilder();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if ( rs.next() ) { return Mapper.getCliente(rs); }
            else { result.append ("⚠️ No se encontró ningún cliente con ese ID."); }
        } catch ( SQLException e ) { System.out.println("❌ Error al buscar cliente: " + e.getMessage()); }

        return null;
    }

    public static String buscarNombrePorId(int id) {
        String query = """
        SELECT p.nombre, p.apellido
        FROM Cliente c
        JOIN Persona p ON c.DNI = p.DNI
        WHERE c.ID = ?
    """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre") + " " + rs.getString("apellido");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar nombre por ID: " + e.getMessage());
        }

        return null;
    }

    //Ahora devuelve un arrayList para podes hacer la muestra de datos copada
    public static ArrayList<Cliente> buscarClientePorDato(String campo, String valor) {
        ArrayList<Cliente> listaTemp = new ArrayList<>();
        String campoSQL = switch (campo) {
            case "nombre", "apellido", "DNI" -> "p." + campo;
            case "ID", "cantCompras"         -> "c." + campo;
            case "tipo"                      -> "tc.descripcion";
            case "telefono"                  -> "t.telefono";
            default                          -> null;
        };

        if (campoSQL == null) return listaTemp;

        String query =
                "SELECT c.ID, p.DNI, p.nombre, p.apellido, tc.tipo, tc.descripcion, c.cantCompras, t.telefono " +
                        "FROM Cliente c " +
                        "INNER JOIN Persona p ON p.DNI = c.DNI " +
                        "LEFT JOIN Telefonos t ON t.idPersona = p.DNI " +
                        "INNER JOIN TiposClientes tc ON c.idTipo = tc.tipo " +
                        "WHERE " + campoSQL + " LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + valor + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente c = Mapper.getCliente(rs);
                listaTemp.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar cliente: " + e.getMessage());
        }

        return listaTemp;
    }



    public static String insertar(Cliente cliente, Persona persona) {
        String queryP = """
        INSERT INTO Persona (dni, nombre, apellido ) VALUES (?, ?, ?)
        """;
        String queryC = """
        INSERT INTO Cliente (dni, idTipo, cantCompras ) VALUES (?, ?, ?)
        """;
        StringBuilder resultado = new StringBuilder();

        try (PreparedStatement stmtP = conn.prepareStatement(queryP);
             PreparedStatement stmtC = conn.prepareStatement(queryC))

        {
            Mapper.setPersona(stmtP, persona);
            Mapper.setCliente(stmtC, cliente);

            stmtP.executeUpdate();
            stmtC.executeUpdate();

            resultado.append("✅ Cliente insertado correctamente.");

        } catch (SQLException e) { resultado.append("❌ Error al insertar cliente: ").append(e.getMessage()); }

        return resultado.toString();
    }

    public static Integer obtenerIdClientePorDni(int dni) {
        String sql = "SELECT ID FROM Cliente WHERE DNI = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
