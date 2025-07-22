package Clases.Extras;

public class Fecha {

    protected int dia;
    protected int mes;
    protected int anio;

    public Fecha(int dia, int mes, int anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public int getDia() {return dia;}
    public int getMes() {return mes;}
    public int getAnio() {return anio;}

    public void setDia(int dia) {this.dia = dia;}
    public void setMes(int mes) {this.mes = mes;}
    public void setAnio(int anio) {this.anio = anio;}
}
