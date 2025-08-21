package Clases.Extras;

public class Roles {
    private int rol;
    private String descripcion;

    public Roles(int Rol, String descripcion) {
        this.rol = Rol;
        this.descripcion = descripcion;
    }

    public int getRol() {return rol;}
    public String getDescripcion() {return descripcion;}

    public void setRol(int rol) {this.rol = rol;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    @Override
    public String toString() {return descripcion;}

}
