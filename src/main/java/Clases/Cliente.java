package Clases;

import java.sql.Date;

public class Cliente extends Persona implements Imprimible {
    private int id;
    private int tipCliente;
    private Date fechaAlta;
    private int cantCompras;

    public Cliente(int dni, String nombre, String apellido, int id, int tipCliente, Date fechaAlta, int cantCompras) {
        super(dni, nombre, apellido /*genero, nacionalidad*/);
        this.id = id;
        this.tipCliente = tipCliente;
        this.fechaAlta = fechaAlta;
        this.cantCompras = cantCompras;
    }
    public int getid() { return id; }
    public void setid(int id) { this.id = id; }

    public int getTipCliente() { return tipCliente; }
    public void setTipCliente(int tipCliente) { this.tipCliente = tipCliente; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

    public int getCantCompras() { return cantCompras; }
    public void setCantCompras(int cantCompras) { this.cantCompras = cantCompras; }

    @Override
    public void imprimir() {
        System.out.printf("%-5d %-10s %-15s %-15s %-10s %-10s %-10d %-20s %-12s %-5d\n",
                id, DNI, nombre, apellido, tipCliente, fechaAlta, cantCompras);
    }
}
