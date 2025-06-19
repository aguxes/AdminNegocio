package Principal;
import Clases.*;

import java.util.ArrayList;

public class MetodosSwitch {

    public static void harcodearDatos(ArrayList<Imprimible> lista){
        // Clientes
        Cliente cliente1 = new Cliente("Juan", 12345678, "Pérez", "juanperez@email.com", 1134567890, "Rosario", 1);
        Cliente cliente2 = new Cliente("María", 23456789, "Gómez", "mariagomez@email.com", 1123456789, "Córdoba", 2);
        Cliente cliente3 = new Cliente("Lucas", 34567890, "Rodríguez", "lucasr@email.com", 1145678901, "Buenos Aires", 3);

        // Empleados
        Empleado emp1 = new Empleado("Sofía", 45678901, "Fernández", "sofia@email.com", 1167890123, "Mendoza", "2020-03-01", 1, "85%", "Sí", 180000);
        Empleado emp2 = new Empleado("Martín", 56789012, "López", "martin@email.com", 1178901234, "Salta", "2019-07-15", 2, "90%", "No", 195000);

        lista.add(cliente1);
        lista.add(cliente2);
        lista.add(cliente3);
        lista.add(emp1);
        lista.add(emp2);
    }

    public static void abrirApp() {
        System.out.println("Abriendo interfaz gráfica ");
        View.App.main(new String[0]); // llama al método `main` de la clase JavaFX, no entienod mucho
    }

    public static void imprimirTodo(ArrayList<Imprimible> lista){
        if(!lista.isEmpty()){
            lista.getFirst().imprimirEncabezado(); //llamo al primer dato del array solo para poner el encabezado, no es importante
            for(Imprimible i: lista) i.imprimir();
        }else{
            System.out.println("Lista Vacia");
        }
    }



}