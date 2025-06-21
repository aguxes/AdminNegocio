package Principal;
import Clases.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MetodosSwitch {

    public static void mostrarMenu() {
        System.out.println("\n    - - MENÚ PRINCIPAL - -\n" +
                "Elija una opción: (si tiene un !!! al final es que esta implementada)" +
                "\n1) Ver clientes !!!" +
                "\n2) Agregar nuevo cliente !!!" +
                "\n3) Modificar datos de un cliente !!!" +
                "\n4) Eliminar cliente por ID !!!" +
                "\n5) Buscar cliente por nombre/email !!!" +
                "\n6) Ver ventas" +
                "\n7) Registrar nueva venta" +
                "\n8) Ver ventas por cliente" +
                "\n9) Ver productos" +
                "\n10) Agregar nuevo producto" +
                "\n11) Modificar producto" +
                "\n12) Eliminar producto" +
                "\n13) Buscar producto por nombre o código" +
                "\n14) Reportes (totales, por fecha, por cliente)" +
                "\n15) Abrir interfaz gráfica" +
                "\n16) Salir");
    }

    public static void abrirApp() {
        System.out.println("Abriendo interfaz gráfica ");
        View.App.main(new String[0]); // llama al método `main` de la clase JavaFX, no entienod mucho
    }

    public static void mostrarReportes(Scanner scan) {
    }
}