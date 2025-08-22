package Clases.Principales;

import Clases.Extras.Telefono;
import Clases.Extras.Roles;

public class Empleado extends Persona implements Imprimible {
    private int empleadoID;
    private int dni;
    private Roles rolID;
    private double sueldo;
    private boolean vacaciones;
    private int faltas;
    private String fechaDeIngreso;
    private String fechaDeEgreso;
    private boolean activo;
    private Telefono telefono;

    public Empleado( int empleadoID, int dni, String nombre, String apellido, Telefono telefono,
                    Roles rolID, double sueldo, boolean vacaciones, int faltas, String fechaDeIngreso,
                    String fechaDeEgreso, boolean activo) {

        super(dni, nombre, apellido, telefono);
        this.empleadoID = empleadoID;
        this.dni = dni;
        this.rolID = rolID;
        this.sueldo = sueldo;
        this.vacaciones = vacaciones;
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

    public int getId() { return empleadoID; }

    public int getDni() {
        return dni;
    }

    public Roles getRol() { return rolID; }

    public double getSueldo() {
        return sueldo;
    }

    public boolean getVacaciones() {
        return vacaciones;
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

    public boolean getActivo() {
        return activo;
    }

    //  SETTERS

    public void setID(int empleadoID) { this.empleadoID = empleadoID; }

    public void setDni(int dni) { this.dni = dni; }

    public void setRolID(Roles rolID) { this.rolID = rolID; }

    public void setSueldo(double sueldo) { this.sueldo = sueldo; }

    public void setVacaciones(boolean vacaciones) { this.vacaciones = vacaciones; }

    public void setFaltas(int faltas) { this.faltas = faltas; }

    public void setFechaDeIngreso(String fechaDeIngreso) { this.fechaDeIngreso = fechaDeIngreso; }

    public void setFechaDeEgreso(String fechaDeEgreso) { this.fechaDeEgreso = fechaDeEgreso; }

    public void setActivo(boolean activo) { this.activo = activo; }
}