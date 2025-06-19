import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ArchivoEmpleados {
    private String archivo;

    public ArchivoEmpleados(String archivo) {
        this.archivo = archivo;
    }

    public boolean guardar(Empleado e) {
        List<Empleado> lista = leerTodos();
        lista.add(e);
        return escribirLista(lista);
    }

    public List<Empleado> leerTodos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Empleado>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private boolean escribirLista(List<Empleado> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Empleado buscarPorID(int id) {
        return leerTodos().stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public void filtrarPorNombre(String nombre) {
        leerTodos().stream()
            .filter(e -> e.getNombre().equalsIgnoreCase(nombre))
            .forEach(Empleado::mostrar);
    }

    public void ordenarPorApellido() {
        leerTodos().stream()
            .sorted(Comparator.comparing(Empleado::getApellido))
            .forEach(Empleado::mostrar);
    }

    public void exportarCSV(String nombreCSV) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreCSV))) {
            for (Empleado e : leerTodos()) {
                pw.println(e.getId() + "," + e.getNombre() + "," + e.getApellido() + "," + e.getSueldo());
            }
        } catch (IOException e) {
            System.out.println("Error exportando CSV");
        }
    }
}