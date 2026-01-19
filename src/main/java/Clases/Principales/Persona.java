package Clases.Principales;

import Clases.Extras.Telefono;

public abstract class Persona {
    protected int DNI;
    protected String nombre;
    protected String apellido;
    private Telefono telefono;

    private static int cont = 0;//Contador de personas registradas en el programa

    public Persona(int dni, String nombre, String apellido, Telefono telefono) {
        cont++;
        this.DNI = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }
    public Persona()
    {
        cont++;
        this.DNI = 0;
        this.nombre = "Sin nombre";
        this.apellido = "Sin apellido";
    }

    public static int cantPersonas() { return cont+1; }

    public int getDNI() { return DNI; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Telefono getTelefono() { return telefono; }

    public void setDNI(int DNI) { this.DNI = DNI; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(Telefono telefono) { this.telefono = telefono; }

    public String getTelefonoStr() { return telefono != null ? String.valueOf(telefono.getTelefono()) : ""; }
    // Caso de uso de esta cosa??
}