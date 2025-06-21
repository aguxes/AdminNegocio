package Clases;

public interface Imprimible {

   void imprimir(); //Este es el metodo obligatorio, cada clase que implemente Imprimible tiene que decirle al progrma como se va a comportar la funcion dentro de la clase

    default void imprimirEncabezado() {
        System.out.printf("%-5s %-15s %-22s %-28s %-12s %-15s\n",
                "ID", "Nombre", "Apellido", "Email", "Teléfono", "Localidad");
        System.out.println("---------------------------------------------------------------------------------------------");
    }
}
