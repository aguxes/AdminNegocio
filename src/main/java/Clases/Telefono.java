package Clases;

public class Telefono {
    private int idPersona;  // DNI en este caso
    private long telefono;

    public Telefono(int idPersona, long telefono) {
        this.idPersona = idPersona;
        this.telefono = telefono;
    }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return String.valueOf(telefono);
    }

}
