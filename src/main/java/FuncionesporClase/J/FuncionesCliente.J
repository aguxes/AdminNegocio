import java.io.*;
import java.util.*;

public class ArchivoClientes {
    private String archivo;

    public ArchivoClientes(String archivo) {
        this.archivo = archivo;
    }

    public boolean guardar(Cliente cliente) {
        List<Cliente> lista = leerTodos();
        lista.add(cliente);
        return escribirLista(lista);
    }

    public Cliente buscarPorID(int id) {
        return leerTodos().stream()
            .filter(c -> c.getId() == id)
            .findFirst().orElse(null);
    }

    public void filtrarPorNombre(String nombre) {
        leerTodos().stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
            .forEach(Cliente::mostrar);
    }

    public void ordenarPorNombre() {
        leerTodos().stream()
            .sorted(Comparator.comparing(Cliente::getNombre))
            .forEach(Cliente::mostrar);
    }

    public void mostrarTodos() {
        leerTodos().forEach(Cliente::mostrar);
    }

    public void eliminarCliente(int id) {
        List<Cliente> lista = leerTodos();
        boolean eliminado = lista.removeIf(c -> c.getId() == id);
        if (eliminado) {
            escribirLista(lista);
            System.out.println("Cliente eliminado con éxito.");
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    public void exportarCSV(String nombreCSV) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreCSV))) {
            for (Cliente c : leerTodos()) {
                pw.println(c.getId() + "," + c.getNombre() + "," + c.getApellido() + "," + c.getEmail());
            }
            System.out.println("Clientes exportados a CSV.");
        } catch (IOException e) {
            System.out.println("Error exportando CSV.");
        }
    }

    private List<Cliente> leerTodos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Cliente>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private boolean escribirLista(List<Cliente> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}