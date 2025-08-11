package Clases.Principales;

public class Persona {
    protected int DNI;
    protected String nombre;
    protected String apellido;

    private static int cont = 0;//Contador de personas registradas en el programa

    public Persona(int dni, String nombre, String apellido) {
        cont++;
        this.DNI = dni;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public void setDNI(int DNI) { this.DNI = DNI; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}
