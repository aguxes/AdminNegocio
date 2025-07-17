package Clases;

import java.sql.Date;

public class Cliente extends Persona implements Imprimible {
    private int id;
    private int tipCliente;
    private int cantCompras;
    private long telefono;

    public Cliente(int dni, String nombre, String apellido, int id, int tipCliente, int cantCompras, long telefono) {
        super(dni, nombre, apellido /*genero, nacionalidad*/);
        this.id = id;
        this.tipCliente = tipCliente;
        this.cantCompras = cantCompras;
        this.telefono = telefono;
    }
    public int getid() { return id; }
    public void setid(int id) { this.id = id; }

    public int getTipCliente() { return tipCliente; }
    public void setTipCliente(int tipCliente) { this.tipCliente = tipCliente; }

    public int getCantCompras() { return cantCompras; }
    public void setCantCompras(int cantCompras) { this.cantCompras = cantCompras; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    @Override
    public void imprimir() {
        System.out.printf("%-5d %-10s %-15s %-15s %-10s %-10s %-10d %-20s %-5d\n",
                id, DNI, nombre, apellido, tipCliente, cantCompras);
    }
}
