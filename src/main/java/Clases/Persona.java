package Clases;

public class Persona{
    protected int DNI; //Le pongo protected, se hace asi en la clase padre, las hijas tienen private
    protected String nombre;
    protected String apellido;
    protected String email;
    protected long telefono;
    protected String localidad;


    public Persona(String nombre, int dni, String apellido, String email, long telefono, String localidad) {
        this.nombre = nombre;
        this.DNI = dni;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.localidad = localidad;
    }

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getDNI() {return DNI;}

    public void setDNI(int DNI) {this.DNI = DNI;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public long getTelefono() {return telefono;}

    public void setTelefono(int telefono) {this.telefono = telefono;}

    public String getLocalidad() {return localidad;}

    public void setLocalidad(String localidad) {this.localidad = localidad;}
}
