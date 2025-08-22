package DataBase;

import Clases.Principales.Cliente;
import Clases.Principales.Empleado;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;

public class EmpleadoDAO {
    private static final Connection conn = DataBaseConnection.getConnection();

    public static void cargarEmpleadosEnLista(ArrayList<Empleado> lista) {
        String sql = """
        SELECT e.id, e.DNI, p.nombre, p.apellido, r.rol, r.descripcion, e.sueldo,
               e.vacaciones, e.faltas, t.telefono, e.fechaIngreso, e.fechaEgreso, e.activo
        FROM Empleado e
        INNER JOIN Persona p ON p.DNI = e.DNI
        INNER JOIN Telefonos t ON t.idPersona = p.DNI
        INNER JOIN Roles r ON r.rol = e.idrol
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Empleado e = Mapper.getEmpleado(rs);
                lista.add(e);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar empleados: " + e.getMessage());
        }
    }
    public static String eliminar(int id) {
        StringBuilder result = new StringBuilder();
        String query =
                """
        SELECT p.nombre FROM Empleado e
        INNER JOIN Persona p ON p.DNI = e.DNI
        WHERE e.id = ?
        """;
        String deleteSQL = "DELETE FROM Empleado WHERE id = ?";
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
                    result.append("✅ Empleado eliminado: ").append(nombre);
                }
            } else {
                result.append("⚠️ No se encontró ningún empleado con ese ID.");
            }

        } catch (SQLException e) {
            result.append("❌ Error en la base de datos: ").append(e.getMessage());
        }

        return result.toString();
    }

    public static String buscarxNombre(int id) { return buscarNombrePorId(id, -1); }
    public static String buscarNombrePorId(int id, int dni) {
        String queryID = """
        SELECT p.nombre, p.apellido
        FROM Empleado e
        JOIN Persona p ON c.DNI = p.DNI
        WHERE e.ID = ?
    """;
        String queryDNI = "SELECT ID FROM Empleado WHERE DNI = ?";

        try (PreparedStatement stmtID = conn.prepareStatement(queryID)) {
            stmtID.setInt(1, id);
            ResultSet rs = stmtID.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre") + " " + rs.getString("apellido");
            } else if (dni != -1) {
                try (PreparedStatement stmtDNI = conn.prepareStatement(queryDNI)) {
                    stmtDNI.setInt(1, dni);
                    ResultSet rsDNI = stmtDNI.executeQuery();

                    if (rsDNI.next()) {
                        int nuevoId = rsDNI.getInt("ID");
                        try (PreparedStatement retryStmt = conn.prepareStatement(queryID)) {
                            retryStmt.setInt(1, nuevoId);
                            ResultSet retryRs = retryStmt.executeQuery();
                            if (retryRs.next()) {
                                return retryRs.getString("nombre") + " " + retryRs.getString("apellido");
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar nombre: " + e.getMessage());
            return "Error";
        }

        return "No encontrado";
    }
        public static ArrayList <Empleado> BuscarEmpleadoPorDato(String campo, String valor) {
            ArrayList<Empleado> listaTemp = new ArrayList<>();
            boolean esNumerico = false;
            String campoSQL = switch (campo) {
                case "nombre", "apellido"                                   -> "p." + campo;
                case "dni"                                                  -> "p." + campo;
                case "id", "idrol", "sueldo", "vacaciones", "faltas", "activo"       -> "e." + campo;
                case "telefono"                                             -> "t.telefono";
                default                                                         -> null;
            };

            if (campoSQL == null) return listaTemp;
            esNumerico = switch (campo) {
                case "id", "dni", "idrol", "sueldo", "vacaciones", "faltas", "activo", "telefono" -> true;
                default -> false;
            };
            String query =
                    "SELECT e.ID, p.DNI, p.nombre, p.apellido, r.rol, r.descripcion, e.sueldo, e.vacaciones, e.faltas, t.telefono, e.fechaingreso, e.fechaegreso, e.activo" +
                        "FROM Empleado e " +
                        "INNER JOIN Persona p ON p.DNI = e.DNI " +
                        "LEFT JOIN Telefonos t ON t.idPersona = p.DNI " +
                        "INNER JOIN Roles r ON r.rol = e.idrol" +
                        "WHERE " + (esNumerico? campoSQL + " = ?"
                        : "unaccent(" + campoSQL + ") ILIKE unaccent(?)");

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                if (esNumerico) {
                    stmt.setInt(1, Integer.parseInt(valor));
                } else {
                    stmt.setString(1, "%" + valor + "%");
                }
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Empleado e = Mapper.getEmpleado(rs);
                    listaTemp.add(e);
                }
            } catch (SQLException e) {
                System.out.println("❌ Error al buscar empleado: " + e.getMessage());
            }
            return listaTemp;
        }
    public static Empleado obtenerEmpleadoPorId(int id) {
        String sql = """
        SELECT e.id, e.DNI, p.nombre, p.apellido, r.rol, r.descripcion, e.sueldo,
               e.vacaciones, e.faltas, t.telefono, e.fechaIngreso, e.fechaEgreso, e.activo
        FROM Empleado e
        INNER JOIN Persona p ON p.DNI = e.DNI
        INNER JOIN Telefonos t ON t.idPersona = p.DNI
        INNER JOIN Roles r ON r.rol = e.idrol
        WHERE e.ID = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {return Mapper.getEmpleado(rs); }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener empleado por ID: " + e.getMessage());
        }
        return null;
    }
}
