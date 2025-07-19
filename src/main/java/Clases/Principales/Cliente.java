package Clases.Principales;

import Clases.Extras.Telefono;
import Clases.Extras.TiposClientes;

public class Cliente extends Persona implements Imprimible {
    private int id;
    private TiposClientes tipo;
    private int cantCompras;
    private Telefono telefono;


    public Cliente(int dni, String nombre, String apellido, int id, TiposClientes tipo, int cantCompras, Telefono telefono) {
        super(dni, nombre, apellido );
        this.id = id;
        this.tipo = tipo;
        this.cantCompras = cantCompras;
        this.telefono = telefono;
    }

    public Cliente() { super(0, "", ""); }// Constructor vacío

    public int getId() { return id; }
    public void setid(int id) { this.id = id; }

    public TiposClientes getTipo() { return tipo; }
    public void setTipCliente(TiposClientes tipo) { this.tipo = tipo; }

    public int getCantCompras() { return cantCompras; }
    public void setCantCompras(int cantCompras) { this.cantCompras = cantCompras; }

    public Telefono getTelefono() { return telefono; }
    public void setTelefono(Telefono telefono) { this.telefono = telefono; }

    @Override
    public void imprimir() {
        System.out.printf("%-5s %-10s %-15s %-15s %-15s %-12s %-15s\n",
                "ID", "DNI", "Nombre", "Apellido", "Tipo", "Compras", "Teléfono");
        System.out.println("-------------------------------------------------------------------------------");

        System.out.printf("%-5d %-10d %-15s %-15s %-15s %-12d %-15s\n",
                id,
                getDNI(),
                getNombre(),
                getApellido(),
                tipo != null ? tipo.getDescripcion() : "N/A",
                cantCompras,
                telefono != null ? String.valueOf(telefono.getTelefono()) : "N/A"
        );
    }


}
