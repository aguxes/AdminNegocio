package util;

import Clases.Cliente;
import Clases.Venta;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    //Formato de sql para guardar y leer la fecha
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Venta getVenta(ResultSet rs) throws SQLException {
        String fechaTexto = rs.getString("fecha");

        // Si la fecha no tiene hora (ej: "2025-06-19"), le pone hora cero
        if (fechaTexto.length() == 10) {
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
    //Mira que bueno, con esto cuadno pones el mouse arriba del nombre ed la funcion te explica que necesita y para que sirve
    /**
     * 📤 Prepara los parámetros de un PreparedStatement a partir de un objeto Venta
     * Este método se usa cuando queremos insertar o actualizar una venta en la base de datos
     *
     * @param stmt  PreparedStatement con la consulta preparada
     * @param venta Objeto Venta cuyos datos vamos a guardar
     * @throws SQLException Si ocurre un error al setear los parámetros
     */
    public static void setVenta(PreparedStatement stmt, Venta venta) throws SQLException {
        stmt.setInt(1, venta.getIdCliente());
        stmt.setString(2, venta.getFecha().format(FORMATTER));  // Guarda como texto con formato
        stmt.setBigDecimal(3, venta.getImporteTotal());
        stmt.setInt(4, venta.getIdEmpleado());
        stmt.setString(5, venta.getMedioPago());
        stmt.setString(6, venta.getNotas());
    }


}
