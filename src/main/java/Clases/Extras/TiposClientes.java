package Clases.Extras;

public class TiposClientes {
    private int tipo;
    private String descripcion;

    public TiposClientes(int tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
