package View;

import Clases.Principales.Empleado;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;

public class VentanaEmpleado {

    public static void cargarEmpleadosEnLista(ArrayList<Empleado> lista) {
        String sql = """
        SELECT e.ID, e.DNI, p.nombre, p.apellido, e.idRol, e.Sueldo,
               e.Vacaciones, e.Faltas, e.FechaIngreso, e.FechaEgreso, e.Activo
        FROM Empleado e
        INNER JOIN Persona p ON e.DNI = p.DNI
        """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado e = Mapper.getEmpleado(rs);
                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al cargar empleados: " + e.getMessage());
        }
    }

    public static String obtenerTextoEmpleados(ArrayList<Empleado> lista) {
        if (lista.isEmpty()) return "Lista vacía.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-10s %-15s %-15s %-10s\n", "ID", "DNI", "Nombre", "Apellido", "Rol"));
        sb.append("--------------------------------------------------------------\n");

        for (Empleado e : lista) {
            sb.append(String.format("%-5d %-10d %-15s %-15s %-10d\n",
                    e.getEmpleadoID(),
                    e.getDNI(),
                    e.getNombre(),
                    e.getApellido(),
                    e.getRolID()
            ));
        }

        return sb.toString();
    }

    public static Integer obtenerIdEmpleadoPorDni(int dni) {
        String sql = "SELECT ID FROM Empleado WHERE DNI = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("ID");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
