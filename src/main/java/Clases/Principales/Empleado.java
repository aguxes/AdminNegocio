package Clases.Principales;

import Clases.Extras.Telefono;

public class Empleado extends Persona implements Imprimible {
    private int empleadoID;
    private int dni;
    //private int rolID;
    private double sueldo;
    private boolean vacacionesActivas;
    private int faltas;
    private String fechaDeIngreso;
    private String fechaDeEgreso;
    private boolean activo;
    private Telefono telefono;

    public Empleado( int dni, String nombre, String apellido, Telefono telefono, int empleadoID,
                    /*int rolID,*/ double sueldo, boolean vacacionesActivas, int faltas, String fechaDeIngreso,
                    String fechaDeEgreso, boolean activo) {

        super(dni, nombre, apellido, telefono);
        this.empleadoID = empleadoID;
        this.dni = dni;
       // this.rolID = rolID;
        this.sueldo = sueldo;
        this.vacacionesActivas = vacacionesActivas;
        this.faltas = faltas;
        this.fechaDeIngreso = fechaDeIngreso;
        this.fechaDeEgreso = fechaDeEgreso;
        this.activo = activo;
        this.telefono = telefono;
    }

    public Empleado() { super(0, "Sin nombre","Sin apellido", new Telefono(0,0)); } // Constructor vacío

    @Override
    public void imprimir() { }

    //GETTERS

    public int getEmpleadoID() {
        return empleadoID;
    }

    public int getDni() {
        return dni;
    }

    /*public int getRolID() {
        return rolID;
    }*/

    public double getSueldo() {
        return sueldo;
    }

    public boolean getVacacionesActivas() {
        return vacacionesActivas;
    }

    public int getFaltas() {
        return faltas;
    }

    public String getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public String getFechaDeEgreso() {
        return fechaDeEgreso;
    }

    public boolean isActivo() {
        return activo;
    }

    //  SETTERS

    public void setEmpleadoID(int empleadoID) { this.empleadoID = empleadoID; }

    public void setDni(int dni) { this.dni = dni; }

    //public void setRolID(int rolID) { this.rolID = rolID; }

    public void setSueldo(double sueldo) { this.sueldo = sueldo; }

    public void setVacacionesActivas(boolean vacacionesActivas) { this.vacacionesActivas = vacacionesActivas; }

    public void setFaltas(int faltas) { this.faltas = faltas; }

    public void setFechaDeIngreso(String fechaDeIngreso) { this.fechaDeIngreso = fechaDeIngreso; }

    public void setFechaDeEgreso(String fechaDeEgreso) { this.fechaDeEgreso = fechaDeEgreso; }

    public void setActivo(boolean activo) { this.activo = activo; }
}