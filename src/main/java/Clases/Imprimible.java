package Clases;

public interface Imprimible {

   void imprimir(); //Este es el metodo obligatorio, cada clase que implemente Imprimible tiene que decirle al progrma como se va a comportar la funcion dentro de la clase

    default void imprimirEncabezado() {
        System.out.printf("%-5s %-10s %-15s %-15s %-10s %-15s %-10s %-10s\n",
                "ID", "DNI", "Nombre", "Apellido", "Género", "Nacionalidad", "Compras", "Tipo");
        System.out.println("---------------------------------------------------------------------------------------------");
    }
}