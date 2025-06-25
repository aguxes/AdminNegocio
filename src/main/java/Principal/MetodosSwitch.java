package Principal;
import Clases.*;
import DataBase.ClienteDAO;
import DataBase.ProductoDAO;
import DataBase.ReportesDAO;
import DataBase.VentaDAO;

import java.util.ArrayList;
import java.util.Scanner;

public class MetodosSwitch {

    public static int menuPrincipal(Scanner scan, ArrayList<Imprimible> listaClientes,ArrayList<Imprimible> listaVentas , ArrayList<Imprimible> listaProcuto, ArrayList<Imprimible> listaReportes,  ClienteDAO daoCliente, VentaDAO daoVenta, ProductoDAO daoProdcuto, ReportesDAO daoReportes) {

        int opcion = 0, bucle = 0;
        try{
            do{

                System.out.println("\n      - - Menu principal - -");
                System.out.println("Elija una categoria para comenzar: " +
                        "\n1)Clientes" +
                        "\n2)Ventas" +
                        "\n3)Inventario" +
                        "\n4)Reportes" +
                        "\n5)Abrir app" +
                        "\n6)Salir");

                opcion = Integer.parseInt(scan.nextLine());

                switch (opcion){
                    case 1:
                        menuClientes(scan, listaClientes, daoCliente);
                        break;
                    case 2:
                        menuVentas(scan, listaVentas, daoVenta);
                        break;
                    case 3:
                        menuProducto(scan, listaProcuto, daoProdcuto);
                        break;
                    case 4:
                        menuReporte(scan, listaReportes, daoReportes);
                        break;
                    case 5:
                        MetodosSwitch.abrirApp();
                        break;
                    case 6:
                        System.out.println("Saliendo...");
                        bucle = 5;
                        break;
                    default:
                        System.out.println("Opcion no valida, del 1 al 4");
                        break;
                }
            }while(opcion != 5);
        }catch(Exception e){
            System.out.println("Opcion no permitida, solo numeros");
        }
        return bucle;
    }

    public static void menuClientes(Scanner scan, ArrayList<Imprimible> listaClientes, ClienteDAO daoCliente){
        while(true){
            try{
                System.out.println("\n      - - Menu Clientes - -" +
                        "\n1) Ver clientes !!!" +
                        "\n2) Agregar nuevo cliente !!!" +
                        "\n3) Modificar datos de un cliente !!!" +
                        "\n4) Eliminar cliente por ID !!!" +
                        "\n5) Buscar cliente por nombre/email !!!" +
                        "\n6)Volver a menu principal");


                int opcion = Integer.parseInt(scan.nextLine());

                switch(opcion){
                    case 1: // Ver clientes
                        daoCliente.imprimirClientes(listaClientes);
                        break;

                    case 2: // Agregar nuevo cliente
                        Cliente nuevo = daoCliente.agregarClientePorConsola(scan);
                        ClienteDAO.insertar(nuevo);
                        break;

                    case 3: // Modificar datos de un cliente
                        daoCliente.modificarClientePorId(scan, listaClientes);
                        break;

                    case 4: // Eliminar cliente por ID
                        daoCliente.eliminarPorId(scan);
                        break;

                    case 5: // Buscar cliente por nombre/email
                        daoCliente.buscarClientePorDato(scan);
                        break;
                    case 6:
                        return;

                }
                }catch(Exception e){
                    System.out.println("Opcion no permitida, solo numeros");
            }
        }
    }

    public static void menuVentas(Scanner scan, ArrayList<Imprimible> listaVentas, VentaDAO daoVenta){
        while(true){
            try{
                System.out.println("\n      - - Menu ventas - -" +
                        "\n1) Ver ventas !!!" +
                        "\n2) Registrar nueva venta !!!" +
                        "\n3) Ver ventas por cliente !!!" +
                        "\n4)Volver al menu principal");


                int opcion = Integer.parseInt(scan.nextLine());

                switch(opcion){
                    case 1: // Ver ventas
                        daoVenta.mostrarTodasLasVentas();
                        break;
                    case 2: // Registrar nueva venta
                        daoVenta.registrarVentaPorConsola(scan, listaVentas);
                        break;
                    case 3: // Ver ventas por cliente
                        daoVenta.mostrarVentasPorCliente(scan);
                        break;
                    case 4:
                        return;
                }

            }catch(Exception e){
                System.out.println("\nOpcion no permitida, solo numeros");
            }
        }
    }

    public static void menuProducto(Scanner scan, ArrayList<Imprimible> listaProducto, ProductoDAO daoProducto) {
        int opcion = 0;
        try {
            do {
                System.out.println("\n      - - Menu Productos - -"+
                        "\n1) Ver productos !!!" +
                        "\n2) Agregar nuevo producto !!!" +
                        "\n3) Modificar producto " +
                        "\n4) Eliminar producto !!!" +
                        "\n5) Buscar producto por ID !!! (agregar buscar por nombre)" +
                        "\n6)Volver a menu principal");

                opcion = Integer.parseInt(scan.nextLine());

                switch(opcion){
                    case 1: // Ver productos
                        daoProducto.mostrarProductos();
                        break;

                    case 2: // Agregar nuevo producto
                        Producto p = daoProducto.agregarProductoPorConsola(scan);
                        daoProducto.insertar(p);
                        break;

                    case 3: // Modificar producto
                        daoProducto.modificarProductoPorId(scan);
                        break;

                    case 4: // Eliminar producto
                        daoProducto.eliminarProductoPorId(scan);
                        break;

                    case 5: // Buscar producto por nombre o código
                        daoProducto.buscarProductoPorNombreOCodigo(scan);
                        break;
                    case 6:
                        return;

                }

            } while (opcion != 6);

        } catch (Exception e) {
            System.out.println("Opcion no permitida, solo numeros");
        }
    }

    public static void menuReporte(Scanner scan, ArrayList<Imprimible> listaReportes, ReportesDAO daoReportes) {
        int opcion = 0;
        try {
            do {
                System.out.println("\n      - - Menu Reportes - -"+
                        "\n1) Reportes (totales, por fecha, por cliente)" +
                        "\n2) Volver al menu principal");

                opcion = Integer.parseInt(scan.nextLine());

                switch(opcion){
                    case 1: // Reportes (totales, por fecha, por cliente)
                        MetodosSwitch.mostrarReportes(scan);
                        break;
                    case 2:
                        return;
                }

            } while (opcion != 6);

        } catch (Exception e) {
            System.out.println("Opcion no permitida, solo numeros");
        }
    }


    public static void abrirApp() {
        System.out.println("Abriendo interfaz gráfica ");
        View.App.main(new String[0]); // llama al método `main` de la clase JavaFX, no entienod mucho
    }

    public static void mostrarReportes(Scanner scan) {
    }
}