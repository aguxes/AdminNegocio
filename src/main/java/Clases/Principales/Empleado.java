package Clases.Principales;

public class Empleado extends Persona implements Imprimible {
    private int empleadoID;
    private int dni;
    private int rolID;
    private double sueldo;
    private boolean vacacionesActivas;
    private int faltas;
    private String fechaDeIngreso;
    private String fechaDeEgreso;
    private boolean activo;

    public Empleado(int dni, String nombre, String apellido, int empleadoID, int dni1,
                    int rolID, double sueldo, boolean vacacionesActivas, int faltas, String fechaDeIngreso,
                    String fechaDeEgreso, boolean activo) {

        super(dni, nombre, apellido);
        this.empleadoID = empleadoID;
        this.dni = dni1;
        this.rolID = rolID;
        this.sueldo = sueldo;
        this.vacacionesActivas = vacacionesActivas;
        this.faltas = faltas;
        this.fechaDeIngreso = fechaDeIngreso;
        this.fechaDeEgreso = fechaDeEgreso;
        this.activo = activo;
    }

    @Override
    public void imprimir() { }
    public Empleado() { super(0, "", ""); } // Constructor vacío
    public int getEmpleadoID() {
        return empleadoID;
    }

    public int getDni() {
        return dni;
    }

    public int getRolID() {
        return rolID;
    }

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
}