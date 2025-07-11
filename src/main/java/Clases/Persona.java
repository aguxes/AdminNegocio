package Clases;

public class Persona{
    protected String DNI; //Todos los atributos con numeros los paso a string porque despues son mas faciles de manipular
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;
    protected String localidad;


    public Persona(String nombre, String dni, String apellido, String email, String telefono, String localidad) {
        this.nombre = nombre;
        this.DNI = dni;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.localidad = localidad;
    }

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDNI() {return DNI;}

    public void setDNI(String DNI) {this.DNI = DNI;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getTelefono() {return telefono;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getLocalidad() {return localidad;}

    public void setLocalidad(String localidad) {this.localidad = localidad;}
}
