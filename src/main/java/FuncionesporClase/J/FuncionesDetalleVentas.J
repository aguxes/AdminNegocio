import java.io.*;
import java.util.*;

public class ArchivoDetalleVentas {
    private String archivo;

    public ArchivoDetalleVentas(String archivo) {
        this.archivo = archivo;
    }

    public boolean guardar(DetalleVenta detalle) {
        List<DetalleVenta> lista = leerTodos();
        lista.add(detalle);
        return escribirLista(lista);
    }

    public DetalleVenta buscar(int idVenta) {
        return leerTodos().stream()
            .filter(d -> d.getIdVenta() == idVenta)
            .findFirst().orElse(null);
    }

    public DetalleVenta buscarPorLinea(int idVenta, int idLinea) {
        return leerTodos().stream()
            .filter(d -> d.getIdVenta() == idVenta && d.getIdLinea() == idLinea)
            .findFirst().orElse(null);
    }

    public int contarLineas(int idVenta) {
        return (int) leerTodos().stream()
            .filter(d -> d.getIdVenta() == idVenta)
            .count();
    }

    public void modificarDetalle(int idVenta) {
        List<DetalleVenta> lista = leerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdVenta() == idVenta) {
                DetalleVenta nuevo = new DetalleVenta();
                // cargar nuevo con datos actualizados (implementá esto según tu sistema)
                nuevo.setIdVenta(idVenta);
                lista.set(i, nuevo);
                escribirLista(lista);
                System.out.println("Detalle actualizado.");
                return;
            }
        }
        System.out.println("Detalle no encontrado.");
    }

    public void exportarCSV(String nombreCSV) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreCSV))) {
            for (DetalleVenta d : leerTodos()) {
                pw.println(d.getIdVenta() + "," + d.getIdLinea() + "," + d.getIdProducto() +
                           "," + d.getCantidad() + "," + d.getImporte() + "," + d.getEstado());
            }
            System.out.println("Exportado a CSV con éxito.");
        } catch (IOException e) {
            System.out.println("Error al exportar CSV.");
        }
    }

    private List<DetalleVenta> leerTodos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<DetalleVenta>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private boolean escribirLista(List<DetalleVenta> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
