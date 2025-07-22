package Clases.Principales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venta implements Imprimible {

    private int idVenta;
    private int idProducto;
    private int idCliente;
    private int idEmpleado;
    private LocalDateTime fecha;
    private String medioPago;
    private int idFormaDePago;
    private int cantidad;
    private BigDecimal subtotal;
    private BigDecimal importeTotal;
    private String notas;

    private final String nombreCliente;
    private final String nombreEmpleado;

    public Venta (int idVenta, int idEmpleado, int idCliente, LocalDateTime fecha, String medioPago,
                  int cantidad, BigDecimal importeTotal, String notas, String nombreCliente, String nombreEmpleado) {
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.medioPago = medioPago;
        this.importeTotal = importeTotal;
        this.cantidad = cantidad;
        this.notas = notas;
        this.nombreCliente = nombreCliente;
        this.nombreEmpleado = nombreEmpleado;
    }

    public Venta (int idEmpleado, int idCliente, LocalDateTime fecha, String medioPago, int cantidad,
                  BigDecimal importeTotal, String notas, String nombreCliente, String nombreEmpleado) {
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.medioPago = medioPago;
        this.importeTotal = importeTotal;
        this.cantidad = cantidad;
        this.notas = notas;
        this.nombreCliente = nombreCliente;
        this.nombreEmpleado = nombreEmpleado;
    }

    public Venta() { //Constructor vacío
        this.nombreCliente = "";
        this.nombreEmpleado = "";
    }

    public String getFechaFormateada() {
        return fecha != null ? fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
    }


    //Getters
    public int getIdVenta() { return idVenta; }
    public int getIdProducto() {return idProducto;}
    public int getIdCliente() { return idCliente; }
    public int getIdEmpleado() { return idEmpleado; }
    public LocalDateTime getFecha() { return fecha; }
    public int getIdFormaDePago() {return idFormaDePago;}
    public String getMedioPago() { return medioPago; }
    public int getCantidad () { return cantidad; }
    public BigDecimal getSubtotal() {return subtotal;}
    public BigDecimal getImporteTotal() { return importeTotal; }
    public String getNotas() { return notas; }
    public String getNombreCliente() { return nombreCliente; }
    public String getNombreEmpleado() { return nombreEmpleado; }



    // Setters
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente;}
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setIdFormaDePago(int idFormaDePago) { this.idFormaDePago = idFormaDePago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public void setImporteTotal(BigDecimal importeTotal) { this.importeTotal = importeTotal; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public void imprimir() {
        System.out.printf("%-5d %-10s %-10s %-15s %-15s %-10s %-15s %-15s\n",
                idVenta, idCliente, idEmpleado, fecha, medioPago, cantidad, importeTotal, notas);
    }
}

