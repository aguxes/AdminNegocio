package util;

import Clases.Cliente;
import Clases.Venta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Mapper {

    // Mapeo de Cliente
    // ========================================
    public static Cliente getCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("nombre"),
                rs.getString("dni"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("localidad"),
                rs.getInt("id")
        );
    }

    public static void setCliente(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getDNI());
        stmt.setString(3, cliente.getApellido());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getTelefono());
        stmt.setString(6, cliente.getLocalidad());
    }


    // Mapeo de Venta
    // ========================================
    public static Venta getVenta(ResultSet rs) throws SQLException {
        return new Venta(
                rs.getInt("id"),                             // idVenta
                rs.getInt("empleado_id"),                    // idEmpleado
                rs.getInt("cliente_id"),                     // idCliente
                LocalDate.parse(rs.getString("fecha")),      // fecha (guardada como texto)
                rs.getString("medio_pago"),                  // medioPago (TEXT)
                rs.getBigDecimal("total"),                   // importeTotal
                rs.getString("notas")                        // notas
        );
    }

    public static void setVenta(PreparedStatement stmt, Venta venta) throws SQLException {
        stmt.setInt(1, venta.getIdCliente());
        stmt.setString(2, venta.getFecha().toString()); // Convertir LocalDate a String
        stmt.setBigDecimal(3, venta.getImporteTotal());
        stmt.setInt(4, venta.getIdEmpleado());
        stmt.setString(5, venta.getMedioPago());
        stmt.setString(6, venta.getNotas());
    }

}
