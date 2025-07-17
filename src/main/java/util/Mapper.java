package util;

import Clases.Principales.*;
import Clases.Extras.Telefono;

import java.sql.*;
import java.time.LocalDate;
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

        return new Cliente(
                rs.getInt("DNI"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("tipoCliente"),                // ID FK
                rs.getString("tipoCliente"),        // Descripción del tipo
                rs.getInt("cantCompras"),
                new Telefono(rs.getInt("DNI"), rs.getLong("telefono"))
        );


    }

    public static void setCliente(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setInt(2, c.getDNI());
        stmt.setInt(1, c.getid());
        stmt.setString(3, c.getTipCliente());
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
                rs.getString("producto_nombre"),
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

    //EMPLEADO

    public static Empleado getEmpleado(ResultSet rs) throws SQLException {
        int empleadoID = rs.getInt("ID");
        int dni = rs.getInt("DNI");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        int rolID = rs.getInt("idRol");
        double sueldo = rs.getDouble("Sueldo");
        int vacaciones = rs.getInt("Vacaciones");
        int faltas = rs.getInt("Faltas");
        String fechaIngreso = rs.getString("FechaIngreso");
        String fechaEgreso = rs.getString("FechaEgreso");
        boolean activo = rs.getInt("Activo") == 1;

        return new Empleado(
                dni, nombre, apellido, empleadoID, dni, rolID,
                sueldo, vacaciones, faltas, fechaIngreso, fechaEgreso, activo
        );
    }

    public static void setEmpleado(PreparedStatement stmt, Empleado e) throws SQLException {
        stmt.setInt(1, e.getEmpleadoID());
        stmt.setInt(2, e.getDNI());
        stmt.setInt(3, e.getRolID());
        stmt.setDouble(4, e.getSueldo());
        stmt.setInt(5, e.getVacacionesActivas());
        stmt.setInt(6, e.getFaltas());
        stmt.setString(7, e.getFechaDeIngreso());
        stmt.setString(8, e.getFechaDeEgreso());
        stmt.setInt(9, e.isActivo() ? 1 : 0);
    }


    //PRODUCTO
    public static Producto getProducto(ResultSet rs) throws SQLException {
        String fechaBajaRaw = rs.getString("fechaBaja");
        LocalDate fechaBaja = (fechaBajaRaw == null || fechaBajaRaw.isBlank()) ? null : LocalDate.parse(fechaBajaRaw);

        return new Producto(
                rs.getInt("idProducto"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getDouble("costo"),
                rs.getInt("stock"),
                rs.getInt("idMedida"),
                rs.getInt("idCategoria"),
                LocalDate.parse(rs.getString("fechAlta")),
                fechaBaja
        );
    }


}
