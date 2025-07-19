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

    /*
    public void mostrarEmpleado() {
        System.out.println("      ID EMPLEADO: " + getID());
        mostrarPersona();
        System.out.println("          Detalles en la empresa:");
        System.out.println("              Fecha de Ingreso: ");
        fechaDeIngreso.mostrarFecha();
        System.out.println("              Asistencias: " + asistencias);
        System.out.print("              Vacaciones Activas: ");
        System.out.println(vacacionesActivas ? "Sí" : "No");
        System.out.println("              Sueldo: " + sueldo);
    }

    public void cargarEmpleado(Scanner scanner, ArchivoEmpleados empleados, Menu menu) {
        Fecha fechaACargar = new Fecha();

        cargarPersona(scanner);

        System.out.println("Ingrese la Fecha de Ingreso: ");
        fechaDeIngreso = fechaACargar.cargarFecha(scanner);

        if (empleados.cantidadRegistros() == 0) {
            empleadoID = 1;
        } else {
            empleadoID = empleados.leer(empleados.cantidadRegistros() - 1).getID() + 1;
        }

        System.out.println("Ingrese las asistencias del empleado: ");
        asistencias = scanner.nextInt();
        scanner.nextLine();

        char inputLetra;
        do {
            System.out.println("Esta en vacaciones S/N? ");
            inputLetra = scanner.nextLine().toUpperCase().charAt(0);
            if (inputLetra != 'S' && inputLetra != 'N') {
                menu.mensajeDeError("Opcion invalida! Intentelo de nuevo.");
            }
        } while (inputLetra != 'S' && inputLetra != 'N');

        vacacionesActivas = (inputLetra == 'S');

        float inputSueldo = 0;
        do {
            System.out.println("Ingrese su sueldo: ");
            inputSueldo = scanner.nextFloat();
            scanner.nextLine();
            if (inputSueldo > 0) {
                sueldo = inputSueldo;
            } else {
                menu.mensajeDeError("Opcion Invalida!");
            }
        } while (inputSueldo < 1);
    }


}
*/