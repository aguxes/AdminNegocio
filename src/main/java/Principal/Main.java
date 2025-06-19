package Principal;

import Clases.Cliente;
import Clases.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Imprimible> lista = new ArrayList<Imprimible>();
        MetodosSwitch.harcodearDatos(lista); //pongo datos de prueba, dsp borrar cuando tengamos sql

        Scanner scan = new Scanner(System.in); //esto crea el input, lo llamas poniendo:
        //  int opcion = scan.nextInt();
        //scanner.nextLine(); Siempre dsp de un nextInt() xq deja un salto de linea, con eso se corrige

        //  String opcion = scan.nextLine();

        int opcion = 0;

        do{
            try{ //basicamente es para que si poonen un string en vez de int no se rompa nada
                System.out.println("\n    - - MENU - - \n " +
                        "Elija una de las opciones: " +
                        "\n1)Mostrar todos los datos" +
                        "\n2) agragar" +
                        "\n10)Salir");

                opcion = scan.nextInt();

                switch (opcion) {
                    case 1:
                        MetodosSwitch.imprimirTodo(lista);
                        break;
                    case 10:
                        System.out.println("Saliendo...");
                        break;
                    default :
                        System.out.println("Numero no valido, elija una de las opciones");
                        break;
                }
            }catch(Exception e){
                System.out.println("Opcion no permitida, solo numeros");
            }
        }while(opcion != 10); //puse 10 por poner, la idea es dsp un numero en el menu para salir, como siempre

    }
}