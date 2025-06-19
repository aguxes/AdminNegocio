package Clases;

public class Empleado extends Persona implements Imprimible {
    private int empleadoID;
    private String fechaDeIngreso;
    private String asistencias;
    private String vacacionesActivas;
    private int sueldo;

    public Empleado(String nombre, int dni, String apellido, String email, int telefono, String localidad, String fechaDeIngreso, int empleadoID, String asistencias, String vacacionesActivas, int sueldo) {
        super(nombre, dni, apellido, email, telefono, localidad);
        this.fechaDeIngreso = fechaDeIngreso;
        this.empleadoID = empleadoID;
        this.asistencias = asistencias;
        this.vacacionesActivas = vacacionesActivas;
        this.sueldo = sueldo;
    }

    public int getEmpleadoID() {return empleadoID;}

    public void setEmpleadoID(int empleadoID) {this.empleadoID = empleadoID;}

    public String getFechaDeIngreso() {return fechaDeIngreso;}

    public void setFechaDeIngreso(String fechaDeIngreso) {this.fechaDeIngreso = fechaDeIngreso;}

    public String getAsistencias() {return asistencias;}

    public void setAsistencias(String asistencias) {this.asistencias = asistencias;}

    public String getVacacionesActivas() {return vacacionesActivas;}

    public void setVacacionesActivas(String vacacionesActivas) {this.vacacionesActivas = vacacionesActivas;}

    public int getSueldo() {return sueldo;}

    public void setSueldo(int sueldo) {this.sueldo = sueldo;}

    @Override
    public void imprimir() {
        System.out.printf("%-5d %-15s %-15s %-25s %-12d %-15s %-15s %-12s %-10s %-8d\n",
                empleadoID, nombre, apellido, email, telefono, localidad, fechaDeIngreso, asistencias, vacacionesActivas, sueldo);
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

     */
}
