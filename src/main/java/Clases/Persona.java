package Clases;

public class Persona {
    protected int DNI;
    protected String nombre;
    protected String apellido;
    protected int genero;
    protected int nacionalidad;

    public Persona(int dni, String nombre, String apellido, int genero, int nacionalidad) {
        this.DNI = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.nacionalidad = nacionalidad;
    }

    public int getDNI() { return DNI; }
    public void setDNI(int DNI) { this.DNI = DNI; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getGenero() { return genero; }
    public void setGenero(int genero) { this.genero = genero; }

    public int getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(int nacionalidad) { this.nacionalidad = nacionalidad; }
}
