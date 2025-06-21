package Clases;

public class Cliente extends Persona implements Imprimible{
    private int ClienteID;

    public Cliente(String nombre, int dni, String apellido, String email, long telefono, String localidad, int clienteID) {
        super(nombre, dni, apellido, email, telefono, localidad);
        this.ClienteID = clienteID;
    }

    public Cliente(String nombre, int dni, String apellido, String email, long telefono, String localidad) {
        super(nombre, dni, apellido, email, telefono, localidad);
    }


    public int getClienteID() {return ClienteID;}

    public void setClienteID(int clienteID) {ClienteID = clienteID;}








    @Override
    public void imprimir() { //Los porsentajes son la cantidad de caracteres que tiene cada columna
        System.out.printf("%-5d %-15s %-15s %-35s %-15d %-15s\n",
                ClienteID, nombre, apellido, email, telefono, localidad);
    }

}
