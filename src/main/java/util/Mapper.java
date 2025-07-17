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
    public static Persona getPersona (ResultSet rs) throws SQLException {
        Persona p = new Persona (
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
        stmt.setString(2, c.getNombre());
        stmt.setString(2, c.getApellido());
        stmt.setInt(3, c.getTipCliente());
        stmt.setInt(5, c.getCantCompras());
    }

    // Mapeo de Venta
    // ========================================

    //Formato de sql para guardar y leer la fecha
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Venta getVenta(ResultSet rs) throws SQLException {
        String fechaTexto = rs.getString("fecha");

        // Si la fecha no tiene hora (ej: "2025-06-19"), le pone hora cero
        if (fechaTexto.length() <= 10) {
            fechaTexto += " 00:00:00";
        }

        LocalDateTime fecha = LocalDateTime.parse(fechaTexto, FORMATTER);

        return new Venta(
                rs.getInt("id"),
                rs.getInt("empleado_id"),
                rs.getInt("cliente_id"),
                fecha,
                rs.getString("medio_pago"),
                rs.getBigDecimal("total"),
                rs.getString("notas") != null ? rs.getString("notas") : "",
                rs.getString("cliente_nombre"),
                rs.getString("empleado_nombre")
        );
    }
    //Mira que bueno, con esto cuadno pones el mouse arriba del nombre de la funcion te explica que necesita y para que sirve
    /**
      📤 Prepara los parámetros de un PreparedStatement a partir de un objeto Venta
      Este método se usa cuando queremos insertar o actualizar una venta en la base de datos

      @param stmt  PreparedStatement con la consulta preparada
      @param venta Objeto Venta cuyos datos vamos a guardar
      @throws SQLException Si ocurre un error al setear los parámetros
     */
    public static void setVenta(PreparedStatement stmt, Venta venta) throws SQLException {
        stmt.setInt(1, venta.getIdCliente());
        stmt.setString(2, venta.getFecha().format(FORMATTER));
        stmt.setBigDecimal(3, venta.getImporteTotal());
        stmt.setInt(4, venta.getIdEmpleado());
        stmt.setString(5, venta.getMedioPago());
        stmt.setString(6, venta.getNotas());
    }
}
