package Clases.Principales;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Producto implements Imprimible {
    private int productoID;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private double costo;
    private int stock;

    private int idMedida;
    private int idCategoria;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    // Opcionales si hacés JOINs para mostrar
    private String nombreUnidadMedida; // opcional
    private String nombreCategoria;    // opcional

    // Constructor principal
    public Producto(int productoID, String nombreProducto, BigDecimal precioUnitario, double costo, int stock,
                    int idMedida, int idCategoria, LocalDate fechaAlta, LocalDate fechaBaja) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.costo = costo;
        this.stock = stock;
        this.idMedida = idMedida;
        this.idCategoria = idCategoria;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    //aca se usan los atributos opcionales de arriba
    public Producto(int productoID, String nombreProducto, BigDecimal precioUnitario, double costo, int stock,
                    String nombreUnidadMedida, String nombreCategoria,
                    LocalDate fechaAlta, LocalDate fechaBaja) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.costo = costo;
        this.stock = stock;
        this.nombreUnidadMedida = nombreUnidadMedida;
        this.nombreCategoria = nombreCategoria;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    public Producto() { }// Constructor vacío

    // Getters y setters
    public int getProductoID() { return productoID; }
    public String getNombreProducto() { return nombreProducto; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public double getCosto() { return costo; }
    public int getStock() { return stock; }
    public int getIdMedida() { return idMedida; }
    public int getIdCategoria() { return idCategoria; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public LocalDate getFechaBaja() { return fechaBaja; }

    public String getMedidaNombre() {return nombreUnidadMedida;}

    public String getCategoriaNombre() {return nombreCategoria;}


    // Métodos para impresión
    public void imprimirEncabezado() {
        System.out.printf("%-5s %-20s %-10s %-10s %-10s\n",
                "ID", "Nombre", "Precio", "Costo", "Stock");
        System.out.println("---------------------------------------------------");
    }
    public void imprimir() {
        System.out.printf("%-5d %-20s %-10.2f %-10.2f %-10d\n",
                productoID, nombreProducto, precioUnitario, costo, stock);
    }
}

