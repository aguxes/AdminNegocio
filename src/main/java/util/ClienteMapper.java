package util; //utilizo los archivos de esta carpeta

import Clases.Cliente;
import java.sql.*;

public class ClienteMapper {
    public static Cliente GetC(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("localidad"),
                rs.getInt("id")
        );
    }

    public static void SetC(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getApellido());
        stmt.setString(3, cliente.getDNI());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getTelefono());
        stmt.setString(6, cliente.getLocalidad());
    };
}