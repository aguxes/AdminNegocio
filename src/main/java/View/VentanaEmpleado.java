package View;

import Clases.Principales.Persona;
import Clases.Principales.Cliente;
import DataBase.DataBaseConnection;
import util.Mapper;

import java.sql.*;
import java.util.ArrayList;

public class VentanaEmpleado {

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
