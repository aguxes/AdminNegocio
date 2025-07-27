package util;

import Clases.Principales.*;
import Clases.Extras.Telefono;
import Clases.Extras.TiposClientes;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Mapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Entidades Primarias

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
    public static void modPersona(PreparedStatement stmt, Persona p) throws SQLException {
        stmt.setString(1, p.getNombre());
        stmt.setString(2, p.getApellido());
        stmt.setInt(3, p.getDNI());
    }


            // Mapeo de Cliente
    // ========================================
    public static Cliente getCliente(ResultSet rs) throws SQLException {

        TiposClientes tipoC = new TiposClientes( rs.getInt("tipo"), rs.getString("descripcion")); // Extra de la clase cliente
        Telefono tel = new Telefono(rs.getInt("DNI"), rs.getLong("telefono")); // Extra de la clase cliente
        return new Cliente(
                rs.getInt("DNI"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("id"),
                tipoC,
                rs.getInt("cantCompras"),
                tel
        );
    }
    public static void setCliente(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setInt(1, c.getDNI());
        stmt.setInt(2, c.getTipo().getTipo());
        stmt.setInt(3, c.getCantCompras());
    }
    public static void modCliente(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setInt(1, c.getTipo().getTipo());
        stmt.setInt(2, c.getCantCompras());
        stmt.setInt(3, c.getDNI());
    }

    // Mapeo de Venta
    // ========================================
    //Formato de sql para guardar y leer la fecha
    //private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Venta getVenta(ResultSet rs) throws SQLException {
        String fechaStr = rs.getString("fecha");
        LocalDateTime fecha;

        try {
            fecha = LocalDateTime.parse(fechaStr, FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("⚠️ Fecha inválida en base de datos: " + fechaStr);
            fecha = LocalDateTime.of(2000, 1, 1, 0, 0);
        }
        return new Venta(
                rs.getInt("nFactura"),
                rs.getInt("idE"),
                rs.getInt("idC"),
                fecha,
                rs.getString("mediopago"),
                rs.getInt("cantidad"),
                rs.getBigDecimal("total"),
                rs.getString("producto_nombre"),
                rs.getString("cliente_nombre"),
                rs.getString("empleado_nombre")
        );
    }

    // Este método sirve si vas a insertar una nueva venta
    public static void setVenta(PreparedStatement stmt, Venta venta) throws SQLException {
        stmt.setInt(1, venta.getIdProducto());
        stmt.setInt(2, venta.getIdCliente());
        stmt.setInt(3, venta.getIdEmpleado());
        stmt.setInt(4, venta.getIdFormaDePago());
        stmt.setInt(5, venta.getCantidad());
        stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setBigDecimal(7, venta.getSubtotal());
        stmt.setBigDecimal(8, venta.getImporteTotal());
    }
    //EMPLEADO

    public static Empleado getEmpleado(ResultSet rs) throws SQLException {
        int empleadoID = rs.getInt("ID");
        int dni = rs.getInt("DNI");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        int rolID = rs.getInt("idRol");
        double sueldo = rs.getDouble("Sueldo");
        boolean vacaciones = rs.getBoolean("Vacaciones");
        int faltas = rs.getInt("Faltas");
        String fechaIngreso = rs.getString("FechaIngreso");
        String fechaEgreso = rs.getString("FechaEgreso");
        boolean activo = rs.getBoolean("Activo");

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
        stmt.setBoolean(5, e.getVacacionesActivas());
        stmt.setInt(6, e.getFaltas());
        stmt.setString(7, e.getFechaDeIngreso());
        stmt.setString(8, e.getFechaDeEgreso());
        stmt.setInt(9, e.isActivo() ? 1 : 0);
    }

    //PRODUCTO

    public static Producto getProducto(ResultSet rs) throws SQLException {
        String fechaAltaRaw = rs.getString("fechAlta");
        LocalDateTime fechaAlta = LocalDateTime.parse(fechaAltaRaw, FORMATTER);

        String fechaBajaRaw = rs.getString("fechaBaja");
        LocalDateTime fechaBaja = (fechaBajaRaw == null || fechaBajaRaw.isBlank()) ? null : LocalDateTime.parse(fechaBajaRaw);

        return new Producto(
                rs.getInt("idProducto"),
                rs.getString("nombre"),
                rs.getBigDecimal("precio"),
                rs.getDouble("costo"),
                rs.getInt("stock"),
                rs.getString("medidanombre"),
                rs.getString("categorianombre"),
                fechaAlta,
                fechaBaja
        );
    }
    public static void setProducto(PreparedStatement stmt, Producto p) throws SQLException {
        stmt.setString(1, p.getNombreProducto());
        stmt.setBigDecimal(2, p.getPrecioUnitario());
        stmt.setDouble(3, p.getCosto());
        stmt.setInt(4, p.getStock());
        stmt.setInt(5, p.getIdMedida());
        stmt.setInt(6, p.getIdCategoria());
        stmt.setObject(7, p.getFechaAlta());
        if (p.getFechaBaja() != null) {
            stmt.setObject(8, p.getFechaBaja());
        } else {
            stmt.setNull(8, java.sql.Types.TIMESTAMP);
        }

    }
    // Entidades secundarias
    //Mapeo Telefono
    public static Telefono getTelefono(ResultSet rs) throws SQLException {
       Telefono t = new Telefono(
                rs.getInt("idPersona"),
                rs.getLong("telefono")
        );
        return t;
    }
    public static void setTelefono(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setInt(1, c.getDNI());
        stmt.setLong(2, c.getTelefono().getTelefono());
    }
    //entre esto y la querry simple anda modificar clinete, antes esperaba 3 parametros y le pasamos oslo 2, x eso se rompia
    public static void modTelefono(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setLong(1, c.getTelefono().getTelefono());
        stmt.setInt(2, c.getDNI());
    }


}
