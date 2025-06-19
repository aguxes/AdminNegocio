import java.io.*;
import java.util.*;

public class ArchivoProductos {
    private String archivo;

    public ArchivoProductos(String archivo) {
        this.archivo = archivo;
    }

    public boolean guardar(Producto producto) {
        List<Producto> lista = leerTodos();
        lista.add(producto);
        return escribirLista(lista);
    }

    public boolean guardar(Producto producto, int pos) {
        List<Producto> lista = leerTodos();
        if (pos >= 0 && pos < lista.size()) {
            lista.set(pos, producto);
            return escribirLista(lista);
        }
        return false;
    }

    public Producto buscar(int id) {
        return leerTodos().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public int buscarPos(int id) {
        List<Producto> lista = leerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) return i;
        }
        return -1;
    }

    public List<Producto> filtrarPorNombre(String nombre) {
        List<Producto> encontrados = new ArrayList<>();
        for (Producto p : leerTodos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> encontrados = new ArrayList<>();
        for (Producto p : leerTodos()) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    public void mostrarTodos() {
        List<Producto> lista = leerTodos();
        if (lista.isEmpty()) {
            System.out.println("No se encontraron productos.");
        }
        for (Producto p : lista) {
            p.mostrarProducto();
        }
    }

    public void modificarProducto(int id) {
        int pos = buscarPos(id);
        if (pos == -1) {
            System.out.println("Producto no encontrado.");
            return;
        }

        Producto prod = leerTodos().get(pos);
        prod.cargarProducto();
        prod.setId(id);
        guardar(prod, pos);
        System.out.println("Producto actualizado.");
    }

    public void eliminarArchivoProductos() {
        File f = new File(archivo);
        if (f.delete()) {
            System.out.println("Archivo eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el archivo.");
        }
    }

    public void eliminarProducto(int id) {
        List<Producto> lista = leerTodos();
        boolean eliminado = lista.removeIf(p -> p.getId() == id);
        if (eliminado) {
            escribirLista(lista);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    public void exportarCSV(String nombreCSV) {
        List<Producto> lista = leerTodos();
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreCSV))) {
            for (Producto p : lista) {
                writer.println("ID: " + p.getId());
                writer.println("NOMBRE: " + p.getNombre());
                writer.println("CATEGORIA: " + p.getCategoria());
                writer.println("PRECIO UNITARIO: " + p.getPrecioUnitario());
                writer.println("STOCK: " + p.getStock());
                writer.println("-----------------------------------------------------");
            }
            System.out.println("Exportados " + lista.size() + " productos a CSV.");
        } catch (IOException e) {
            System.out.println("Error al exportar el CSV.");
        }
    }

    private List<Producto> leerTodos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Producto>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private boolean escribirLista(List<Producto> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int cantidadRegistros() {
        return leerTodos().size();
    }
}
