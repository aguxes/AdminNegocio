package util;

import Clases.Principales.*;
import Clases.Extras.Telefono;
import Clases.Extras.TiposClientes;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
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

    // Mapeo de Venta
    // ========================================
    //Formato de sql para guardar y leer la fecha
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Venta getVenta(ResultSet rs) throws SQLException {
                                // TEMA FECHA (UN BARDOOO)

        LocalDateTime fecha = LocalDateTime.now();

        return new Venta(
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
        stmt.setString(6, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
        stmt.setBoolean(5, e.getVacacionesActivas());
        stmt.setInt(6, e.getFaltas());
        stmt.setString(7, e.getFechaDeIngreso());
        stmt.setString(8, e.getFechaDeEgreso());
        stmt.setInt(9, e.isActivo() ? 1 : 0);
    }


    //PRODUCTO

    //mas feo pero tolerante para la consulta sql sin joins
    public static Producto getProducto(ResultSet rs) throws SQLException {
        int id = rs.getInt("idProducto");
        String nombre = rs.getString("nombre");
        BigDecimal precio = rs.getBigDecimal("precio");
        double costo = rs.getDouble("costo");
        int stock = rs.getInt("stock");
        String fechaAltaRaw = rs.getString("fechAlta");
        LocalDate fechaAlta = (fechaAltaRaw == null || fechaAltaRaw.isBlank()) ? null : LocalDate.parse(fechaAltaRaw);

        String fechaBajaRaw = rs.getString("fechaBaja");
        LocalDate fechaBaja = (fechaBajaRaw == null || fechaBajaRaw.isBlank()) ? null : LocalDate.parse(fechaBajaRaw);

        try {
            // Si viene desde una query con joins
            String medidaNombre = rs.getString("medidanombre");
            String categoriaNombre = rs.getString("categorianombre");

            return new Producto(id, nombre, precio, costo, stock, medidaNombre, categoriaNombre, fechaAlta, fechaBaja);
        } catch (SQLException e) {
            // Si viene desde la tabla base sin joins
            int idMedida = rs.getInt("idMedida");
            int idCategoria = rs.getInt("idCategoria");
            return new Producto(id, nombre, precio, costo, stock, idMedida, idCategoria, fechaAlta, fechaBaja);
        }
    }

    public static void setProducto(PreparedStatement stmt, Producto p) throws SQLException {
        stmt.setInt(1, p.getProductoID());
        stmt.setString(2, p.getNombreProducto());
        stmt.setBigDecimal(3, p.getPrecioUnitario());
        stmt.setDouble(4, p.getCosto());
        stmt.setInt(5, p.getStock());
        stmt.setInt(6, p.getIdMedida());
        stmt.setInt(7, p.getIdCategoria());
        stmt.setString(8, p.getFechaAlta().toString());
        stmt.setString(8, p.getFechaBaja().toString());
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
}
