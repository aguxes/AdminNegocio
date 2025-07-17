package util;

import Clases.Principales.Persona;
import Clases.Principales.Cliente;
import Clases.Extras.Telefono;
import Clases.Principales.Venta;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    // Mapeo de Persona
    // ========================================
    public static Persona getPersona(ResultSet rs) throws SQLException {
        Persona p = new Persona(
                rs.getInt("DNI"),
                rs.getString("nombre"),
                rs.getString("apellido")
        );
        return p;
    }

    public static void setPersona(PreparedStatement stmt, Persona p) throws SQLException {
        stmt.setInt(1, p.getDNI());
        stmt.setString(2, p.getNombre());
        stmt.setString(3, p.getApellido());
    }

    // Mapeo de Cliente
    // ========================================
    public static Cliente getCliente(ResultSet rs) throws SQLException {

        Cliente c = new Cliente(
                rs.getInt("DNI"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("ID"),
                rs.getInt("idTipo"),
                rs.getInt("cantCompras"),
                new Telefono(
                        rs.getInt("DNI"),
                        rs.getLong("telefono")
                )
        );

        return c;
    }

    public static void setCliente(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setInt(2, c.getDNI());
        stmt.setInt(1, c.getid());
        stmt.setInt(3, c.getTipCliente());
        stmt.setInt(5, c.getCantCompras());
        //stmt.setInt(c.getTelefono());
    }

    // Mapeo de Venta
    // ========================================
    //Formato de sql para guardar y leer la fecha
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Venta getVenta(ResultSet rs) throws SQLException {
        String fechaTexto = rs.getString("fecha");

        // Agrega hora si sólo tiene la fecha
        if (fechaTexto.length() <= 10) {
            fechaTexto += " 00:00:00";
        }

        LocalDateTime fecha = LocalDateTime.parse(fechaTexto, FORMATTER);

        return new Venta(
                rs.getInt("nFactura"),
                rs.getInt("idE"),
                rs.getInt("idC"),
                fecha,
                rs.getString("medio_pago"),
                rs.getBigDecimal("total"),
                rs.getString("producto_nombre"), // ahora es producto, no notas
                rs.getString("cliente_nombre"),
                rs.getString("empleado_nombre")
        );

    }

    // Este método sirve si vas a insertar una nueva venta
    public static void setVenta(PreparedStatement stmt, Venta venta) throws SQLException {
        stmt.setInt(1, venta.getIdProducto()); // ahora sí existe
        stmt.setInt(2, venta.getIdEmpleado());
        stmt.setInt(3, venta.getIdCliente());
        stmt.setString(4, venta.getFecha().format(FORMATTER));
        stmt.setInt(5, venta.getIdFormaDePago());  // ahora existe
        stmt.setBigDecimal(6, venta.getSubtotal()); // ahora existe
        stmt.setBigDecimal(7, venta.getImporteTotal());
    }
}
